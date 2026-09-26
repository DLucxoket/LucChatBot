# LucatBot 类设计说明

> 项目：基于 OneBot 协议（QQ）的仿真人即时聊天机器人
> 框架：Kotlin + Spring Boot + Shiro + LangChain4j
> 用途：记录每个类的设计意图、职责边界和相互关系，供开发参考。

---

## 总览

```
LucatBotApplication          Spring Boot 入口
│
├── adapter/                 协议适配层
│   └── onebot/MainPlugin    OneBot 协议接入（Shiro 插件）
│
├── bot/                     机器人核心
│   ├── Bot                  机器人本体抽象
│   ├── llm/                 LLM 调用层
│   │   ├── LLM              抽象接口
│   │   └── OpenAIAPI        OpenAI 协议实现
│   └── agent/               智能体调度层
│       ├── Agent            智能体抽象基类
│       └── DispatcherAgent  智能体调度器（单例）
│
└── model/                   业务数据模型层（与 Shiro DTO 解耦）
    ├── event/               事件模型
    │   ├── Event
    │   └── message/
    │       ├── MessageEvent
    │       ├── FriendMessageEvent
    │       ├── GroupMessageEvent
    │       └── PrivateMessageEvent
    ├── message/             消息模型
    │   ├── Message
    │   ├── ChatMessage
    │   ├── PokeMessage
    │   └── member/          消息片段
    │       ├── MessageMember
    │       ├── TextMessageMember
    │       ├── AtMessageMember
    │       ├── ImageMessageMember
    │       ├── FaceMessageMember
    │       └── MemeMessageMember
    ├── user/                用户模型
    │   ├── User
    │   └── Sex
    ├── group/               群组模型
    │   └── Group
    └── session/             会话模型
        └── Session
```

---

## 1. 根包 `org.dlucxoket.lucatbot`

### LucatBotApplication

| 项目 | 说明 |
|------|------|
| **类型** | Spring Boot 应用入口 |
| **职责** | 启动 Spring 容器，开启组件扫描和自动配置 |
| **关键点** | `@SpringBootApplication` 扫描 `org.dlucxoket.lucatbot` 全包 |

---

## 2. adapter — 协议适配层

> 设计原则：协议相关代码集中在此，与领域核心（bot/、model/）解耦。
> 将来可扩展 `adapter/wechat/`、`adapter/telegram/` 等。

### adapter/onebot/MainPlugin

| 项目 | 说明 |
|------|------|
| **类型** | Shiro BotPlugin 插件 |
| **职责** | 一切 QQ 消息/事件的**第一入口** |
| **注册方式** | `application.yaml` → `shiro.plugin-list` |
| **插件链机制** | 返回 `MESSAGE_IGNORE` → 继续下一个插件；返回 `MESSAGE_BLOCK` → 中断 |
| **当前状态** | 骨架，`onAnyMessage` 直接放行 |
| **设计意图** | 将来在此处完成：消息接收 → 转为 model 层对象 → 交给 Agent 系统处理 → 回复 |
| **现有功能** | `onFriendAddRequest`：自动通过好友请求 |

---

## 3. bot — 机器人核心

### Bot（接口）

| 项目 | 说明 |
|------|------|
| **类型** | 机器人本体抽象接口 |
| **职责** | 表示 LucatBot 机器人自身的统一能力入口——接收外界消息、发出回复 |
| **方法** | `receiveMsg(msg: String)` / `sendMsg(msg: String)` |
| **设计意图** | 不关心底层通信协议（QQ / 微信 / Telegram 等），通信与适配由 `adapter/` 层处理 |
| **当前状态** | 无实现类 |

---

## 4. bot/llm — LLM 调用层

### LLM（接口）

| 项目 | 说明 |
|------|------|
| **类型** | LLM 统一抽象接口 |
| **职责** | 对上层屏蔽具体模型实现，提供 `ask()` 方法 |
| **方法** | `ask(systemPrompt, question): String` 和便捷重载 `ask(question)` |
| **DEFAULT** | `companion object` 中的懒加载全局单例，默认指向 `OpenAIAPI.DEFAULT` |
| **设计意图** | 将来可替换/新增实现（如 Claude、本地 GGUF 等），不影响 Agent 层 |

### OpenAIAPI

| 项目 | 说明 |
|------|------|
| **类型** | `LLM` 的唯一实现，`@Component` |
| **职责** | 通过 OpenAI 兼容协议调用 LLM |
| **支持服务** | OpenAI、Azure OpenAI、Ollama、vLLM、任何 OpenAI 兼容端点 |
| **配置来源** | `application.yaml` → `ai.qwen.*`（base-url / api-key / model-name） |
| **内部机制** | 每次 `ask()` 构建消息列表（SystemMessage + UserMessage）→ 封装 ChatRequest → 调用 → 返回纯文本 |
| **DEFAULT** | `companion object` 中的懒加载手动实例（非 Spring Bean） |
| **注意** | 每次调用都重新创建 `OpenAiChatModel`，低频 OK，高频有性能开销 |

---

## 5. bot/agent — 智能体调度层

### Agent（抽象类）

| 项目 | 说明 |
|------|------|
| **类型** | 智能体抽象基类 |
| **职责** | 定义所有智能体的统一契约 |
| **必填** | `description: String`（功能描述，供 LLM 阅读）、`process(input: String): String`（处理逻辑） |
| **自动生成** | `systemPromptHead`（description 包装为系统提示头）、`shortDescription`（description 首行） |
| **LLM** | 持有 `llm: LLM = LLM.DEFAULT` |
| **继承方式** | 普通类或 Kotlin `object` 均可，`DispatcherAgent` 会自动扫描发现 |
| **设计意图** | 一个 Agent = 一种能力（如搜索、计算、查天气、发消息），可被调度器按需调用 |

### DispatcherAgent（object 单例）

| 项目 | 说明 |
|------|------|
| **类型** | 智能体调度器，Kotlin `object` 单例 |
| **职责** | 根据上下文，自动决定调用哪些 Agent、给它们什么输入 |
| **工作流程** | ① ClassGraph 扫描所有 `Agent` 子类 → ② 过滤可实例化的（排除抽象/接口/自身）→ ③ 构建 "AgentName: 短描述" 列表 → ④ 交给 LLM 决策 |
| **LLM 输出格式** | 每行 `AgentName: 输入`，或 `done` 表示不再调用 |
| **设计意图** | LLM 自动路由：用户说"帮我查天气然后发给XX"→ 调度器输出 `WeatherAgent: 北京天气` + `SendMsgAgent: ...` |
| **注意** | 是 `object`，不能被 Spring 注入；`getInstanceOf()` 优先取 `objectInstance`，否则 `createInstance()` |

---

## 6. model — 业务数据模型层

> 设计原则：**与 OneBot/Shiro 的 DTO 解耦**，业务层只依赖自定义 model，不直接用框架类。

### 6.1 event — 事件模型（按**来源**区分）

#### Event（接口）
- 所有事件的基类（标记接口）
- 统一描述机器人可能收到的各类事件

#### MessageEvent（抽象类）
- 消息类事件的基类
- 持有一个 `Message` 对象（消息内容）
- 子类按来源区分

#### PrivateMessageEvent
- 私聊消息事件（私聊场景基类）
- 是 `FriendMessageEvent` 的父类

#### FriendMessageEvent
- 好友消息事件
- 特指 QQ 好友发来的私聊消息

#### GroupMessageEvent
- 群消息事件
- QQ 群聊中的消息

### 6.2 message — 消息模型（按**内容种类**区分）

#### Message（接口）
- 所有消息的基类（标记接口）
- **关键设计约束**：子类按"内容种类"区分，不按"来源"区分（来源由 Event 表达）

#### ChatMessage
- 对话消息，**复合消息**
- 持有 `List<MessageMember>`，表示一条消息可包含多个片段
- 例如："文字 + @某人 + 图片" = 3 个 MessageMember

#### PokeMessage
- 戳一戳消息
- 非文本类消息，空实现预留扩展

### 6.3 message/member — 消息片段（Message 的最小组成单元）

#### MessageMember（接口）
- 消息片段基类
- 设计意图：将消息拆分为最小可处理单元，便于 Agent 对不同片段做不同处理

#### TextMessageMember
- 文本片段，字段 `text: String`

#### AtMessageMember
- @提及片段，字段 `targetQqNumber: Long`

#### ImageMessageMember
- 图片段，字段 `url: String`

#### FaceMessageMember
- QQ 内置表情片段，字段 `faceId: Int`

#### MemeMessageMember
- 表情包片段，字段 `url: String`
- 与 FaceMessageMember 的区别：Face 是 QQ 内置固定表情，Meme 是自定义/收藏的图片表情包

### 6.4 user — 用户模型

#### User
- 用户实体，一个实例对应一个 QQ 号
- 字段：`qqNumber`、`nickname`、`sex`、`level`、`signature`

#### Sex
- 性别枚举：`MALE`、`FEMALE`、`UNKNOWN`

### 6.5 group — 群组模型

#### Group
- 群组实体
- 字段：`groupNumber`（群号）、`groupName`（群名）

### 6.6 session — 会话模型

#### Session
- 会话上下文容器（空实现，预留）
- 规划用途：聊天历史、Agent 调用链状态、LLM 对话上下文

---

## 7. 类关系图

```
┌──────────────────────────────────────────────────────────────┐
│              adapter/onebot/MainPlugin                       │
│                   (OneBot 消息入口)                           │
│                                                              │
│  onAnyMessage() ──→ 转为 model 层对象 ──→ 交给 Agent 系统     │
└──────────────────────────┬───────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                     DispatcherAgent                          │
│                    (object 单例)                              │
│                                                              │
│  process(input) ──→ 扫描 Agent 子类 ──→ LLM 决策 ──→ 调用计划 │
└──────────────────────────┬───────────────────────────────────┘
                           │
              ┌────────────┼────────────┐
              ▼            ▼            ▼
        ┌──────────┐ ┌──────────┐ ┌──────────┐
        │ Agent A  │ │ Agent B  │ │ Agent C  │ ...
        │(自定义)  │ │(自定义)  │ │(自定义)  │
        └────┬─────┘ └────┬─────┘ └────┬─────┘
             │             │             │
             └─────────────┼─────────────┘
                           │
                           ▼
                    ┌─────────────┐
                    │  LLM 接口   │
                    │  (llm.ask)  │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │ OpenAIAPI   │
                    │(OpenAI协议) │
                    └─────────────┘
```

---

## 8. 待完成事项

| 状态 | 模块 | 待做 |
|------|------|------|
| ⬜ | MainPlugin | 消息接收后转为 model 对象，接入 Agent 系统 |
| ⬜ | model 转换层 | Shiro DTO → 自定义 model 的映射工具 |
| ⬜ | DispatcherAgent | 解析 LLM 输出的调用计划，按序执行 Agent |
| ⬜ | Session | 对话历史 / 上下文管理 |
| ⬜ | Bot | 一个具体实现类（对接 Shiro Bot 发消息） |
| ⬜ | Agent 实现 | 具体的业务 Agent（查询、回复、工具调用等） |
| ⬜ | 测试 | Agent 发现、消息转换、LLM 调用的单元测试 |

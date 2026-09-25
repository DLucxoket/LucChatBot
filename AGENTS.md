# AGENTS.md

## 项目概览

Kotlin Spring Boot 4.0.3 应用——基于 OneBot 协议（QQ）的即时回复聊天机器人，使用 Shiro 框架 + LangChain4j 的 LLM 智能体系统。

## 架构

### 智能体系统（核心设计）

机器人使用自动发现的 Agent 链：

- `Agent`（抽象类）——基类；每个 Agent 有 `description`（LLM 可读）和 `process(input): String`。
- `DispatcherAgent`——单例，通过 **ClassGraph** 在运行时扫描所有 `Agent` 子类，构建描述列表，交给 LLM 决定下一步调用哪些 Agent。输出格式为每行 `AgentName: 输入`，或 `done`。
- 添加新 Agent：在 `bot/agent/` 下创建继承 `Agent` 的类，实现 `description` 和 `process()`。会被自动发现——无需手动注册。

### 消息模型层级

```
Event（接口）
└─ MessageEvent（抽象类，持有 Message）
   ├─ MessageEvent 子类
   └─ message/ 子类型：ChatMessage、PokeMessage 等

Message（接口）
└─ ChatMessage
   └─ message/member/：TextMessageMember、ImageMessageMember、AtMessageMember、FaceMessageMember、MemeMessageMember

User：qqNumber、nickname、sex、level、signature
Session：空占位符
```

### 入口点

`MainPlugin`（Shiro BotPlugin）——目前是骨架；`onAnyMessage` 返回 `MESSAGE_IGNORE`。机器人自动通过好友请求。

### LLM 配置

默认：本地 Ollama（`http://localhost:11434/v1`）+ `qwen2.5`。通过 `application.yaml` 配置：

```yaml
ai.qwen.base-url: http://localhost:11434/v1
ai.qwen.api-key: ollama
ai.qwen.model-name: qwen2.5
```

使用 LangChain4j 的 `OpenAiChatModel`——任何兼容 OpenAI 协议的端点均可。

## 开发命令

```bash
./gradlew run              # 运行机器人（需要 Ollama 或兼容的 LLM 端点）
./gradlew test             # 运行所有测试
./gradlew build            # 构建（编译 + 测试 + jar）
./gradlew compileKotlin    # 仅编译，不跑测试
```

单个测试类：`src/test/kotlin/.../LucatBotApplicationTests.kt`

## 核心依赖

| 库 | 用途 |
|----|------|
| `com.mikuac:shiro:2.5.0` | OneBot 协议（QQ 机器人框架） |
| `dev.langchain4j:langchain4j-open-ai-spring-boot-starter` | OpenAI 兼容 LLM 客户端 |
| `io.github.classgraph:classgraph` | 运行时 Agent 类发现 |

## 容易搞错的地方

- **Agent 发现是自动的**——不要在 `MainPlugin` 或任何配置里手动注册新 Agent。ClassGraph 会扫描 classpath。
- **`LLM.DEFAULT` 是懒加载单例**（不是 Spring bean）——它使用 `OpenAIAPI.DEFAULT`，后者读取 Spring `@Value` 字段。`OpenAIAPI` 本身是 `@Component`，但 `DEFAULT` 伴生对象绕过了 DI。
- **`DispatcherAgent` 是 Kotlin `object`**——不能被注入或实例化；当静态单例使用。
- **Gradle 使用国内镜像**——`gradle-wrapper.properties` 指向 `mirrors.aliyun.com` 下载发行版。
- **Spring Boot 4.0.3 + Kotlin 2.2.21**——版本很新；不要假设 Spring Boot 3.x 的写法。

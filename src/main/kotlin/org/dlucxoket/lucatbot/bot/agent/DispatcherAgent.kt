package org.dlucxoket.lucatbot.bot.agent

import io.github.classgraph.ClassGraph
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

/**
 * 智能体调度器（DispatcherAgent）
 *
 * 核心职责：根据当前上下文，自动决定接下来应调用哪些 Agent，并生成调用计划。
 *
 * 工作流程：
 * 1. 通过 ClassGraph 在运行时扫描 classpath 上所有 [Agent] 子类
 * 2. 过滤出可实例化的类（排除抽象类、接口、自身）
 * 3. 构建 "AgentName: 简要描述" 的列表
 * 4. 将列表 + 上下文交给 LLM，由 LLM 决策输出调用计划
 *
 * LLM 输出格式（由 [description] 约定）：
 * ```
 * Agent1: 给Agent1的输入
 * Agent2: 给Agent2的输入
 * ```
 * 若无需继续调用，LLM 返回 `done`。
 *
 * 注意：这是一个 Kotlin `object`（单例），不能被 Spring 注入或手动实例化。
 */
object DispatcherAgent : Agent() {
    /**
     * 调度器的功能描述
     *
     * 这段描述会被直接放进 LLM 的系统提示中，指导 LLM：
     * - 输出格式：每行 "AgentName: 输入"，不要空行
     * - 只调用确实需要的 Agent，不必全部调用
     * - 若无法准确完成任务的 Agent，不要调用它
     * - 无需继续调用时返回 "done"
     */
    override val description: String = """
        一个Agent调度器，能根据输入的上下文来决定接下来该使用哪些Agent
        输出格式为：
        
        Agent1: 给Agent1的输入
        Agent2: 给Agent2的输入
        
        然后系统将依次调用
        其中给Agent的输入必须能够帮助Agent完成任务，每行之间用一个换行符隔开，不要有空行
        如果某个Agent依照当前上下文的信息无法准确完成任务，不要调用他
        只选择调用你认为该调用的即可，没必要每个都调用
        若不需要再调用了，返回done
    """.trimIndent()

    /**
     * 根据上下文，决策下一步应调用哪些 Agent
     *
     * @param input 用户输入的上下文信息（原始消息、会话历史等）
     * @return LLM 生成的调用计划：每行 "AgentName: 输入参数"，或 "done"
     */
    override fun process(input: String): String {
        /**
         * 第一步：使用 ClassGraph 扫描所有 Agent 子类
         *
         * 过滤条件：
         * - 排除抽象类和接口（只保留可实例化的具体类）
         * - 排除 DispatcherAgent 自身（避免自调用死循环）
         * - 通过 Kotlin 反射确认是 Agent 子类后，转换为 KClass<out Agent>
         */
        val agentClasses: List<KClass<out Agent>> = ClassGraph()
            .enableClassInfo()
            .scan()
            .use { scanResult ->
                scanResult.getSubclasses(Agent::class.java.name)
                    .filter { !it.isAbstract && !it.isInterface }
                    .mapNotNull {
                        val kClass = it.loadClass().kotlin
                        if (kClass.isSubclassOf(Agent::class)) {
                            @Suppress("UNCHECKED_CAST")
                            kClass as KClass<out Agent>
                        } else {
                            null
                        }
                    }
                    .filter { it != this::class }
            }

        /**
         * 第二步：遍历所有 Agent 子类，获取实例并提取简要描述
         *
         * 格式："- AgentName: 短描述"，多行拼接成一个字符串供 LLM 阅读
         */
        val sb = StringBuilder()
        agentClasses.forEach { kClass ->
            val instance = getInstanceOf(kClass)
            sb.append("\n- ${kClass.simpleName}: ${instance.shortDescription}")
        }

        /**
         * 第三步：调用 LLM 进行智能决策
         *
         * 提示词结构：
         * - systemPromptHead：调度器自身身份说明
         * - 可用 Agent 列表：所有被发现的 Agent 及其短描述
         * - 上下文：当前输入的原始内容
         *
         * LLM 返回格式为调用计划文本，原样返回给调用方解析执行。
         */
        val reply = llm.ask("""
            $systemPromptHead
            
            可用的Agent以及描述：$sb
            
            上下文：
            $input
        """.trimIndent())

        return reply
    }

    /**
     * 获取 Agent 类的实例
     *
     * 优先返回 Kotlin `object` 单例（objectInstance），
     * 否则通过无参构造器创建新实例（createInstance）。
     *
     * @param kClass Agent 的 KClass
     * @return 该类的实例
     */
    private fun getInstanceOf(kClass: KClass<out Agent>): Agent {
        return kClass.objectInstance ?: kClass.createInstance()
    }
}

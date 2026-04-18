package org.dlucxoket.lucatbot.bot.agent

import io.github.classgraph.ClassGraph
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

object DispatcherAgent : Agent() {
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
     * 根据上下文，决定接下来使用哪个Agent
     *
     * 通过扫描所有可用的 Agent 子类，构建 Agent 列表及其描述，
     * 然后调用 AI 根据输入上下文智能决策需要调用的 Agent 及其输入参数。
     *
     * @param input 用户输入的上下文信息，用于 AI 决策
     * @return AI 返回的决策结果，格式为每行一个 "Agent名称: 输入参数"，若无需调用则返回 "done"
     */
    override fun process(input: String): String {
        /**
         * 使用 ClassGraph 扫描所有 Agent 子类的 KClass
         * 过滤抽象类和接口，排除当前 DispatcherAgent，获取所有可实例化的 Agent 类
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
         * 遍历所有 Agent 子类，获取实例并构建包含名称和描述的字符串列表
         */
        val sb = StringBuilder()
        agentClasses.forEach { kClass ->
            val instance = getInstanceOf(kClass)
            sb.append("\n- ${kClass.simpleName}: ${instance.shortDescription}")
        }

        /**
         * 调用 AI 进行智能决策，传入系统提示、可用 Agent 列表和上下文信息
         */
        val reply = ai.ask("""
            $systemPromptHead
            
            可用的Agent以及描述：$sb
            
            上下文：
            $input
        """.trimIndent())
        
        return reply
    }

    private fun getInstanceOf(kClass: KClass<out Agent>): Agent {
        return kClass.objectInstance ?: kClass.createInstance()
    }
}
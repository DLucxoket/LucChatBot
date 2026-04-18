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
        若
    """.trimIndent()

    /**
     * 根据上下文，决定接下来使用哪个Agent
     *
     */
    override fun process(input: String): String {
        /** 使用 ClassGraph 扫描所有 Agent 子类的 KClass */
        val agentClasses: List<KClass<out Agent>> = ClassGraph()
            .enableClassInfo() // 启用类信息扫描
            .scan() // 执行扫描
            .use { scanResult ->
                scanResult.getSubclasses(Agent::class.java.name)
                    .filter { !it.isAbstract && !it.isInterface } // 过滤抽象类和接口
                    .mapNotNull { 
                        val kClass = it.loadClass().kotlin
                        if (kClass.isSubclassOf(Agent::class)) {
                            @Suppress("UNCHECKED_CAST")
                            kClass as KClass<out Agent>
                        } else {
                            null
                        }
                    }
                    .filter { it != this::class } // 排除当前类
            }

        //包含所有子类名以及描述的字符串builder
        val sb = StringBuilder()
        agentClasses.forEach { kClass ->
            /** 
             * 获取 Agent 实例：
             * - 如果是 object 单例，使用 objectInstance 获取唯一实例
             * - 如果是普通类，使用 createInstance() 创建新实例
             */
            val instance = getInstanceOf(kClass)
            sb.append("\n- ${kClass.simpleName}: ${instance.shortDescription}")
        }

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
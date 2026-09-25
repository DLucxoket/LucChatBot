package org.dlucxoket.lucatbot.bot.agent

import org.dlucxoket.lucatbot.bot.ai.LLM

/**
 * 智能体接口
 *
 * 用于进行某种操作，返回某些数据，或判断该不该做
 */
abstract class Agent {
    val llm: LLM = LLM.DEFAULT
    abstract val description: String
    val systemPromptHead: String = """
        你的信息：
        $description
        ===================================
    """.trimIndent()
    val shortDescription = description.substringBefore("\n")
    /**
     * 智能体处理
     *
     * @param input 输入数据
     * @return 处理结果
     */
    abstract fun process(input: String): String
}
package org.dlucxoket.lucatbot.bot.ai


/**
 * 大语言模型
 */
interface LLM {
    companion object {
        val DEFAULT: LLM by lazy { OpenAIAPI.DEFAULT }
    }
    fun ask(systemPrompt:String, question: String): String
    fun ask(question: String): String{
        return ask("", question)
    }
}
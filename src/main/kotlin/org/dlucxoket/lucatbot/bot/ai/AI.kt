package org.dlucxoket.lucatbot.bot.ai

interface AI {
    companion object {
        val DEFAULT: AI = LocalQwenAI()
    }
    fun ask(systemPrompt:String, question: String): String
    fun ask(question: String): String{
        return ask("", question)
    }
}
package org.dlucxoket.lucatbot.bot.ai

import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.openai.OpenAiChatModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * 基于 OpenAI 协议的 API实现
 *
 * 支持任何兼容 OpenAI API 格式的服务，包括：
 * - OpenAI GPT 系列
 * - Azure OpenAI
 * - 本地部署的 Qwen、Llama 等（通过 Ollama、vLLM 等）
 * - 其他第三方兼容服务
 */
@Component
class OpenAIAPI: LLM {
    companion object {
        /**
         * 默认实例
         */
        val DEFAULT: OpenAIAPI by lazy { OpenAIAPI() }
    }

    @Value($$"${ai.qwen.base-url:http://localhost:11434/v1}")
    private lateinit var baseUrl: String

    @Value($$"${ai.qwen.api-key:ollama}")
    private lateinit var apiKey: String

    @Value($$"${ai.qwen.model-name:qwen2.5}")
    private lateinit var modelName: String


    /**
     * 创建 OpenAiChatModel 实例
     */
    private fun createModel(): OpenAiChatModel {
        return OpenAiChatModel.builder()
            .baseUrl(baseUrl)
            .apiKey(apiKey)
            .modelName(modelName)
            .build()
    }

    override fun ask(systemPrompt: String, question: String): String {
        val model = createModel()
        
        /**
         * 构建消息列表
         */
        val messages = if (systemPrompt.isNotBlank()) {
            listOf(
                SystemMessage.systemMessage(systemPrompt),
                UserMessage.userMessage(question)
            )
        } else {
            listOf(UserMessage.userMessage(question))
        }
        
        /**
         * 创建聊天请求并执行
         */
        val chatRequest = ChatRequest.builder()
            .messages(messages)
            .build()
        
        val response = model.chat(chatRequest)
        return response.aiMessage().text()
    }
}

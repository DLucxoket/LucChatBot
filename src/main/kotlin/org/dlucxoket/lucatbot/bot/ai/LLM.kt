package org.dlucxoket.lucatbot.bot.ai


/**
 * 大语言模型（LLM）统一抽象接口
 *
 * 设计意图：
 * - 对上层（Agent 等）屏蔽底层模型实现的差异，调用方只需关心 `ask()` 方法
 * - 通过 [LLM.DEFAULT] 提供全局默认实例，避免每次手动传递
 *
 * 实现约定：
 * - `ask(systemPrompt, question)`：带系统提示的完整调用，systemPrompt 为空时退化为纯用户消息
 * - `ask(question)`：便捷重载，等价于 `ask("", question)`
 *
 * 当前唯一实现：[OpenAIAPI]（兼容任意 OpenAI 协议端点）
 */
interface LLM {
    companion object {
        /**
         * 全局默认 LLM 实例（懒加载，线程安全）
         *
         * 注意：这不是 Spring 管理的 Bean，而是通过 `OpenAIAPI.DEFAULT` 直接创建。
         * 这意味着它绕过了 Spring 依赖注入，`@Value` 注入的配置可能在首次调用时
         * 才通过反射生效（取决于 Spring 是否已完成对该 Bean 的后处理）。
         */
        val DEFAULT: LLM by lazy { OpenAIAPI.DEFAULT }
    }

    /**
     * 向 LLM 发起提问
     *
     * @param systemPrompt 系统提示词，用于设定 AI 的角色/行为约束；为空字符串时不发送系统消息
     * @param question     用户输入的实际问题
     * @return LLM 生成的回复文本
     */
    fun ask(systemPrompt:String, question: String): String

    /**
     * 向 LLM 发起提问（便捷重载）
     *
     * 不提供系统提示词，直接将 [question] 作为用户消息发送。
     *
     * @param question 用户输入的实际问题
     * @return LLM 生成的回复文本
     */
    fun ask(question: String): String{
        return ask("", question)
    }
}

package org.dlucxoket.lucatbot.bot.agent

import org.dlucxoket.lucatbot.bot.ai.LLM

/**
 * 智能体（Agent）抽象基类
 *
 * 所有智能体的统一契约。每个 Agent 负责一种能力：
 * - 向 LLM 提供自身的功能描述（[description]）
 * - 接收输入并返回处理结果（[process]）
 *
 * 设计约定：
 * - [description] 会被 [DispatcherAgent] 收集并展示给 LLM，
 *   用于让 LLM 自动决定调用哪些 Agent，因此描述应当清晰说明"我能做什么"
 * - [systemPromptHead] 将 description 包装为 LLM 系统提示的开头，
 *   让 Agent 在被调用时知道自己的身份
 * - [shortDescription] 取 description 的第一行，用于在 Agent 列表中简要展示
 *
 * 继承方式：
 * - 普通类：实现 [description] 和 [process]，Spring 或 ClassGraph 会创建实例
 * - 单例对象：用 Kotlin `object` 关键字，可直接通过 `objectInstance` 获取
 */
abstract class Agent {
    /** 该 Agent 绑定的 LLM 实例（默认使用全局 LLM.DEFAULT） */
    val llm: LLM = LLM.DEFAULT

    /**
     * 智能体功能描述（必填）
     *
     * 会被 DispatcherAgent 收集，以纯文本形式展示给 LLM，供其决策是否调用。
     * 建议用简洁的中文说明：该 Agent 能处理什么类型的任务、适合什么场景。
     */
    abstract val description: String

    /**
     * 传递给 LLM 的系统提示开头
     *
     * 格式："你的信息：\n{description}\n==================================="
     * 作用是让 LLM 在执行 Agent 任务时了解自身身份与能力边界。
     */
    val systemPromptHead: String = """
        你的信息：
        $description
        ===================================
    """.trimIndent()

    /** description 的首行摘要，用于 Agent 列表展示 */
    val shortDescription = description.substringBefore("\n")

    /**
     * 智能体核心处理逻辑（必填）
     *
     * @param input 输入数据（由 DispatcherAgent 或上层调用方提供）
     * @return 处理结果字符串
     */
    abstract fun process(input: String): String
}

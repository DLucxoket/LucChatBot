package org.dlucxoket.lucatbot.model.message.member

/**
 * 消息成员（MessageMember）基类
 *
 * 一条复合消息 [org.dlucxoket.lucatbot.model.message.ChatMessage] 中的单个片段。
 * 按内容类型区分：文本、@提及、图片、表情、表情包等。
 *
 * 设计意图：将消息拆分为最小可处理单元，
 * 便于 Agent 系统对不同类型片段做不同处理。
 */
interface MessageMember {
}

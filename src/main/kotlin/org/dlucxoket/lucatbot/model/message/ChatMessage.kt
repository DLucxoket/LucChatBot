package org.dlucxoket.lucatbot.model.message

/**
 * 对话消息（ChatMessage）
 *
 * 表示一条包含若干消息片段（[MessageMember]）的复合消息。
 * 例如一条 QQ 消息可能是 "文字 + @某人 + 图片"，每个部分是一个 MessageMember。
 *
 * @param messageMembers 消息成员列表，按顺序排列
 */
open class ChatMessage(
    val messageMembers: List<org.dlucxoket.lucatbot.model.message.member.MessageMember>
) : org.dlucxoket.lucatbot.model.message.Message {
}

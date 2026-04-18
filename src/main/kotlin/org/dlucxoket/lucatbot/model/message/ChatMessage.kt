package org.dlucxoket.lucatbot.model.message

/**
 * 对话消息
 */
open class ChatMessage(
    val messageMembers: List<org.dlucxoket.lucatbot.model.message.member.MessageMember>
) : org.dlucxoket.lucatbot.model.message.Message {
}
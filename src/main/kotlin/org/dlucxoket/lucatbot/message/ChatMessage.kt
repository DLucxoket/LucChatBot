package org.dlucxoket.lucatbot.message

import org.dlucxoket.lucatbot.message.member.MessageMember

/**
 * 对话消息
 */
open class ChatMessage(
    val messageMembers: List<MessageMember>
) : Message {
}
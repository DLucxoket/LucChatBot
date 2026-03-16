package org.dlucxoket.lucatbot.message

import org.dlucxoket.lucatbot.message.member.MessageMember

/**
 * 私聊消息
 */
open class PrivateMessage(
    override val messageMembers: List<MessageMember>
) : Message {
}
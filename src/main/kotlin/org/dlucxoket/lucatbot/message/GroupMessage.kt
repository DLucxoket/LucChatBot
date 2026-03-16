package org.dlucxoket.lucatbot.message

import org.dlucxoket.lucatbot.message.member.MessageMember

/**
 * 群组消息
 */
class GroupMessage(
    override val messageMembers: List<MessageMember>
) : Message {
}
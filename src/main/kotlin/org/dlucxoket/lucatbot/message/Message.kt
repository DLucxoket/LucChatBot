package org.dlucxoket.lucatbot.message

import org.dlucxoket.lucatbot.message.member.MessageMember

/**
 * 所有消息的基类
 */
interface Message {
    val messageMembers: List<MessageMember>
}
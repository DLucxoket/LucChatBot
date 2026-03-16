package org.dlucxoket.lucatbot.message

import org.dlucxoket.lucatbot.message.member.MessageMember

/**
 * 好友消息
 */
class FriendMessage(
    messageMembers: List<MessageMember>
) : PrivateMessage(messageMembers) {
}
package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.FriendMessage
import org.dlucxoket.lucatbot.message.GroupMessage
import org.dlucxoket.lucatbot.message.Message

/**
 * 好友消息事件
 */
class FriendMessageEvent(
    override val message: FriendMessage
) : PrivateMessageEvent(message) {
}
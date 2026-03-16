package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.Message

/**
 * 好友消息事件
 */
class FriendMessageEvent(
    message: Message
) : PrivateMessageEvent(message) {
}
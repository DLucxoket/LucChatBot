package org.dlucxoket.lucatbot.model.event.message

/**
 * 好友消息事件
 */
class FriendMessageEvent(
    message: org.dlucxoket.lucatbot.model.message.Message
) : org.dlucxoket.lucatbot.model.event.message.PrivateMessageEvent(message) {
}
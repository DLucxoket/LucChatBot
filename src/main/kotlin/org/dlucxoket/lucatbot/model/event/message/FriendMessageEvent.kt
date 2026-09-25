package org.dlucxoket.lucatbot.model.event.message

/**
 * 好友消息事件
 *
 * 特指来自 QQ 好友的私聊消息（区别于陌生人私聊等）。
 *
 * @param message 好友发送的消息内容
 */
class FriendMessageEvent(
    message: org.dlucxoket.lucatbot.model.message.Message
) : org.dlucxoket.lucatbot.model.event.message.PrivateMessageEvent(message) {
}

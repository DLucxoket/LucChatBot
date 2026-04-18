package org.dlucxoket.lucatbot.model.event.message

/**
 * 私聊消息事件
 */
open class PrivateMessageEvent(
    message: org.dlucxoket.lucatbot.model.message.Message
) : org.dlucxoket.lucatbot.model.event.message.MessageEvent(message) {
}
package org.dlucxoket.lucatbot.model.event.message

/**
 * 消息事件
 */
abstract class MessageEvent(
    val message: org.dlucxoket.lucatbot.model.message.Message
): org.dlucxoket.lucatbot.model.event.Event {
}
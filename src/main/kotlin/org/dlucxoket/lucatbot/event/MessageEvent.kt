package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.Message

/**
 * 消息事件
 */
abstract class MessageEvent(
    val message: Message
): Event {
}
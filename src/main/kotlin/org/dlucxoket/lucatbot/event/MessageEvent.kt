package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.Message

/**
 * 消息事件
 */
interface MessageEvent: Event {
    val message: Message
}
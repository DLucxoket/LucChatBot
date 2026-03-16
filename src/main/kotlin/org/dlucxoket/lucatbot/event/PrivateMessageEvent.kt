package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.Message

/**
 * 私聊消息事件
 */
open class PrivateMessageEvent(
    message: Message
) : MessageEvent(message) {
}
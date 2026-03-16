package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.Message
import org.dlucxoket.lucatbot.message.PrivateMessage

/**
 * 私聊消息事件
 */
open class PrivateMessageEvent(
    override val message: PrivateMessage
) : MessageEvent {
}
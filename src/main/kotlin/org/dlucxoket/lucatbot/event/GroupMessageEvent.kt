package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.Message

/**
 * 群组消息事件
 */
class GroupMessageEvent(
    message: Message
) : MessageEvent(message) {
}
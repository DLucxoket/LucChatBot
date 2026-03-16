package org.dlucxoket.lucatbot.event

import org.dlucxoket.lucatbot.message.GroupMessage

/**
 * 群组消息事件
 */
class GroupMessageEvent(
    override val message: GroupMessage
) : MessageEvent {
}
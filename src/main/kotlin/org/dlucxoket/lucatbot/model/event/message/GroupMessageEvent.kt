package org.dlucxoket.lucatbot.model.event.message

/**
 * 群消息事件
 *
 * 表示在 QQ 群聊中收到的消息。
 *
 * @param message 群消息内容
 */
class GroupMessageEvent(
    message: org.dlucxoket.lucatbot.model.message.Message
) : org.dlucxoket.lucatbot.model.event.message.MessageEvent(message) {
}

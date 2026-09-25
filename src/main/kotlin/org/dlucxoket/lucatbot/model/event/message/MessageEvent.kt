package org.dlucxoket.lucatbot.model.event.message

/**
 * 消息事件——所有消息类事件的抽象基类
 *
 * 持有一个 [org.dlucxoket.lucatbot.model.message.Message] 实例，
 * 表示"某条消息在某个上下文中产生"。
 *
 * 子类按消息来源区分（私聊、群聊、好友等），
 * 消息内容本身由 Message 子类表达（文本、图片、表情等）。
 *
 * @param message 本事件携带的消息对象
 */
abstract class MessageEvent(
    val message: org.dlucxoket.lucatbot.model.message.Message
): org.dlucxoket.lucatbot.model.event.Event {
}

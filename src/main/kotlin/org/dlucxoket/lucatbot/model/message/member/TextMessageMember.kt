package org.dlucxoket.lucatbot.model.message.member

/**
 * 文本消息成员
 *
 * 表示一条消息中的纯文本片段，是 [MessageMember] 最常见的实现。
 *
 * @param text 文本内容
 */
class TextMessageMember(
    val text: String
) : org.dlucxoket.lucatbot.model.message.member.MessageMember {
}

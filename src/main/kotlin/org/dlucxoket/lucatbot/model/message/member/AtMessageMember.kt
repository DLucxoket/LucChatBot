package org.dlucxoket.lucatbot.model.message.member

/**
 * @提及消息成员
 *
 * 表示消息中的 @某人 片段。
 *
 * @param targetQqNumber 被 @ 的目标 QQ 号
 */
class AtMessageMember(
    val targetQqNumber: Long
) : org.dlucxoket.lucatbot.model.message.member.MessageMember {
}

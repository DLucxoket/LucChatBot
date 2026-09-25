package org.dlucxoket.lucatbot.model.message.member

/**
 * QQ 表情消息成员
 *
 * 表示消息中的 QQ 内置表情片段（如 [微笑]、[大哭] 等），
 * 与表情包（MemeMessageMember）不同，QQ 表情是内置的固定编号表情。
 *
 * @param faceId QQ 表情的编号 ID
 */
class FaceMessageMember(
    val faceId: Int
) : org.dlucxoket.lucatbot.model.message.member.MessageMember {
}

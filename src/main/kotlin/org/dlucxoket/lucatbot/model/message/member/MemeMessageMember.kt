package org.dlucxoket.lucatbot.model.message.member

/**
 * 表情包消息成员（Meme）
 *
 * 表示消息中的表情包图片片段，通常是一张用户自定义或收藏的图片，
 * 与 QQ 内置表情（FaceMessageMember）不同。
 *
 * @param url 表情包图片的地址
 */
class MemeMessageMember(
    val url: String
) : org.dlucxoket.lucatbot.model.message.member.MessageMember {
}

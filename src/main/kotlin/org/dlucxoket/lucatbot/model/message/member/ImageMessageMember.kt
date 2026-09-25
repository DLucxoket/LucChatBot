package org.dlucxoket.lucatbot.model.message.member

/**
 * 图片消息成员
 *
 * 表示消息中的图片片段，可包含本地路径、网络 URL 或 Base64 编码的图片数据。
 *
 * @param url 图片地址（网络 URL 或本地路径），具体格式取决于 OneBot 实现
 */
class ImageMessageMember(
    val url: String
) : org.dlucxoket.lucatbot.model.message.member.MessageMember {
}

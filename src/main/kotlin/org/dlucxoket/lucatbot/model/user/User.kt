package org.dlucxoket.lucatbot.model.user

/**
 * 用户实体
 *
 * 一个 [User] 实例对应一个 QQ 号，描述用户的基本资料信息。
 * 数据来源：OneBot 的 get_stranger_info / get_group_member_info 等接口。
 *
 * @param qqNumber   QQ 号（唯一标识）
 * @param nickname   用户昵称
 * @param sex        性别
 * @param level      QQ 等级
 * @param signature  个性签名
 */
class User(
    val qqNumber: Long,
    val nickname: String,
    val sex: org.dlucxoket.lucatbot.model.user.Sex,
    val level: Int,
    val signature: String,
) {
}

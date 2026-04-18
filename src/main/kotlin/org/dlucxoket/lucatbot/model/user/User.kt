package org.dlucxoket.lucatbot.model.user

/**
 * 用户，一个用户实体对应一个QQ号
 */
class User(
    val qqNumber: Long,
    val nickname: String,
    val sex: org.dlucxoket.lucatbot.model.user.Sex,
    val level: Int,
    val signature: String,
) {
}
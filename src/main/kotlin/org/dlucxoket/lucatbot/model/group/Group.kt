package org.dlucxoket.lucatbot.model.group

/**
 * 群组实体
 *
 * 对应一个 QQ 群，描述群的基本信息。
 * 数据来源：OneBot 的 get_group_info 等接口。
 */
class Group(
    /** 群号（唯一标识） */
    val groupNumber: Long,

    /** 群名称 */
    val groupName: String
) {
}

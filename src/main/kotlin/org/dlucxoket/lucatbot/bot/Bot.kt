package org.dlucxoket.lucatbot.bot

/**
 * 机器人本体抽象
 *
 * 表示 LucatBot 这个机器人自身的统一能力入口：
 * 接收外界消息、发出回复。不关心底层通信协议（QQ / 微信 / Telegram 等），
 * 通信与适配由 adapter 层处理。
 *
 * 当前未有实现类，预留为后续多协议/多渠道支持的扩展点。
 */
interface Bot {
    /**
     * 接收一条消息
     *
     * @param msg 消息文本内容
     */
    fun receiveMsg(msg: String)

    /**
     * 发送一条消息
     *
     * @param msg 要发送的消息文本
     */
    fun sendMsg(msg: String)
}

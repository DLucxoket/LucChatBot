package org.dlucxoket.lucatbot.bot

/**
 * 机器人抽象接口
 *
 * 定义机器人收发消息的统一契约。
 * 注意：这里不是 Shiro 框架的 com.mikuac.shiro.core.Bot，
 * 而是本项目自定义的业务层 Bot 抽象，用于屏蔽底层通信协议差异。
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

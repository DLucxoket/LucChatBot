package org.dlucxoket.lucatbot.adapter.onebot

import com.mikuac.shiro.core.Bot
import com.mikuac.shiro.core.BotPlugin
import com.mikuac.shiro.dto.event.message.AnyMessageEvent
import com.mikuac.shiro.dto.event.request.FriendAddRequestEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Shiro 主插件——OneBot 消息处理入口
 *
 * 所有 QQ 消息事件的第一入口。通过 application.yaml 中的
 * `shiro.plugin-list` 配置注册，Shiro 框架会在收到事件时按序调用。
 *
 * 插件链机制：
 * - 返回 MESSAGE_IGNORE → 继续执行 plugin-list 中的下一个插件
 * - 返回 MESSAGE_BLOCK  → 中断插件链，不执行后续插件
 *
 * 当前状态：骨架实现，消息处理尚未接入 Agent 系统。
 */
@Component
internal class MainPlugin: BotPlugin() {
    private val log = LoggerFactory.getLogger(MainPlugin::class.java)

    /**
     * 处理所有类型的消息事件（群消息、私聊消息等）
     *
     * 当前仅返回 MESSAGE_IGNORE（放行给后续插件）。
     * TODO: 在此处接入 DispatcherAgent，将消息内容转发给智能体系统处理。
     *
     * @param bot   Shiro 的 Bot 实例，可用于发送消息、获取群成员等操作
     * @param event 消息事件，包含发送者、消息内容、来源群/私聊等信息
     * @return MESSAGE_IGNORE 表示继续执行后续插件
     */
    override fun onAnyMessage(bot: Bot, event: AnyMessageEvent): Int {
        // TODO: 调用 DispatcherAgent.process(event.message) 处理消息
        // 返回 MESSAGE_IGNORE 执行 plugin-list 下一个插件，返回 MESSAGE_BLOCK 则不执行下一个插件
        return MESSAGE_IGNORE
    }

    /**
     * 处理好友添加请求
     *
     * 自动通过所有好友请求（不填备注）。
     * TODO: 可扩展为需要验证或黑名单机制。
     *
     * @param bot   Bot 实例，用于调用 setFriendAddRequest 响应请求
     * @param event 好友添加请求事件，包含请求者信息和 flag（请求标识）
     * @return 调用 super 实现，通常返回 MESSAGE_IGNORE
     */
    override fun onFriendAddRequest(bot: Bot, event: FriendAddRequestEvent): Int {
        bot.setFriendAddRequest(event.flag, true, null)
        return super.onFriendAddRequest(bot, event)
    }
}

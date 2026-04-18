package org.dlucxoket.lucatbot.onebot

import com.mikuac.shiro.core.Bot
import com.mikuac.shiro.core.BotPlugin
import com.mikuac.shiro.dto.event.message.AnyMessageEvent
import com.mikuac.shiro.dto.event.request.FriendAddRequestEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
internal class MainPlugin: BotPlugin() {
    private val log = LoggerFactory.getLogger(MainPlugin::class.java)


    override fun onAnyMessage(bot: Bot, event: AnyMessageEvent): Int {


        // 返回 MESSAGE_IGNORE 执行 plugin-list 下一个插件，返回 MESSAGE_BLOCK 则不执行下一个插件
        return MESSAGE_IGNORE
    }

    override fun onFriendAddRequest(bot: Bot, event: FriendAddRequestEvent): Int {
        bot.setFriendAddRequest(event.flag, true, null)
        return super.onFriendAddRequest(bot, event)
    }
}

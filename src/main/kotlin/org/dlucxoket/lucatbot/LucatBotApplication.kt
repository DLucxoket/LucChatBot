package org.dlucxoket.lucatbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Spring Boot 应用入口类
 *
 * 标准 Spring Boot 启动入口，标注 @SpringBootApplication 开启：
 * - 组件扫描（扫描 org.dlucxoket.lucatbot 包下所有 @Component 等）
 * - 自动配置
 * - 属性注入（读取 application.yaml）
 *
 * 启动方式：`./gradlew run` 或 `java -jar build/libs/LucatBot-*.jar`
 */
@SpringBootApplication
class LucatBotApplication

/**
 * JVM 程序主入口
 *
 * 启动 Spring Boot 应用上下文。
 * Spring 容器启动后，Shiro BotPlugin 会通过 plugin-list 配置自动注册，
 * 进而连接 OneBot WebSocket 服务。
 */
fun main(args: Array<String>) {
    runApplication<LucatBotApplication>(*args)
}

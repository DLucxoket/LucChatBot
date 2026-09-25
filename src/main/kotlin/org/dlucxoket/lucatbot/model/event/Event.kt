package org.dlucxoket.lucatbot.model.event

/**
 * 所有事件的基类（标记接口）
 *
 * 本项目自定义的事件模型层级根节点，用于统一描述
 * 聊天机器人可能接收到的各类事件（消息事件、请求事件等）。
 *
 * 与 OneBot 协议 / Shiro 框架的事件类相互独立，
 * 业务层只依赖此接口，实现协议解耦。
 */
interface Event {
}

package org.dlucxoket.lucatbot.model.message

/**
 * 所有消息的基类（标记接口）
 *
 * 注意：各子类必须按**消息种类**区分（文本、图片、表情等），
 * 而非按**消息来源**区分（来源由 Event 层表达）。
 *
 * 这一层设计保证：同一条消息内容可复用于不同事件场景。
 */
interface Message {
}

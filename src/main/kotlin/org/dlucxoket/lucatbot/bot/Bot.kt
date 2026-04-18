package org.dlucxoket.lucatbot.bot

interface Bot {
    fun receiveMsg(msg: String)
    fun sendMsg(msg: String)
}
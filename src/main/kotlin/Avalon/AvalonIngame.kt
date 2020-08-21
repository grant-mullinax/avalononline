package Avalon

import CreateUserMessage
import Message
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsMessageContext

class AvalonIngame(private val players: List<AvalonPlayer>) : AvalonState {
    override fun receiveMessage(ctx: WsMessageContext, message: Message) {
        when (message) {
            is CreateUserMessage -> {
                // reconnect
                players.filter { entry ->
                    entry.username == message.username
                }.forEach { entry ->
                    entry.connection = ctx
                }
            }
            else -> println("fail")
        }
    }

    override fun receiveDisconnect(ctx: WsCloseContext) {
    }
}
package Avalon

import Message
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsMessageContext

interface AvalonState {
    fun receiveMessage(ctx: WsMessageContext, message: Message)
    fun receiveDisconnect(ctx: WsCloseContext)
}
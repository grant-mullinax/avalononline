package Avalon

import Message
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsMessageContext

class AvalonGame(val id: String) {
    var state: AvalonState = AvalonLobby()

    fun receiveMessage(ctx: WsMessageContext, message: Message) {
        when (message) {
            // Message("startGame") ->
            else -> state.receiveMessage(ctx, message)
        }
    }

    fun receiveDisconnect(ctx: WsCloseContext) {
        state.receiveDisconnect(ctx)
    }
}
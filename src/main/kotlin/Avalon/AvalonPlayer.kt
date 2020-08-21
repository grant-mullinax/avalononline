package Avalon

import AvalonRole
import io.javalin.websocket.WsContext

data class AvalonPlayer(val username: String, val role: AvalonRole, var connection: WsContext) {

}
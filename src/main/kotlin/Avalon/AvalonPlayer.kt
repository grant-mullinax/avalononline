package Avalon

import AvalonRole
import io.javalin.websocket.WsContext

class AvalonPlayer {
    var connection: WsContext? = null
    val username: String
    val role: AvalonRole

    constructor(username: String, role: AvalonRole) {
        this.role = role
        this.username = username
    }
}
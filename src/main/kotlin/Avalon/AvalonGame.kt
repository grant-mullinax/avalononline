package Avalon

import AvalonRole
import Message
import StartGameMessage
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsMessageContext

class AvalonGame(val id: String) {
    var state: AvalonState = AvalonLobby()

    fun receiveMessage(ctx: WsMessageContext, message: Message) {
        when (message) {
            is StartGameMessage -> {
                val lobby = state
                if (lobby is AvalonLobby) {
                    val players = lobby.players

                    // this is pretty icky... maybe
                    val roles = mutableListOf<AvalonRole>()
                    roles.addAll(Array(message.minionCount) { AvalonRole.MINION })
                    roles.add(AvalonRole.MERLIN)
                    roles.add(AvalonRole.ASSASSIN)
                    roles.addAll(Array(players.size - message.minionCount - 2) { AvalonRole.GOODGUY })


                    this.state = AvalonIngame((players zip roles).map { AvalonPlayer(it.first.first, it.second, it.first.second) })
                }
            }
            else -> state.receiveMessage(ctx, message)
        }
    }

    fun receiveDisconnect(ctx: WsCloseContext) {
        state.receiveDisconnect(ctx)
    }
}
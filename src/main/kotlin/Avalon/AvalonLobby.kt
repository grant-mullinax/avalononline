package Avalon

import ConnectionSuccessMessage
import CreateUserMessage
import Message
import UsernameStatus
import UsernameUpdateMessage
import com.beust.klaxon.Klaxon
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsContext
import io.javalin.websocket.WsMessageContext
import java.util.concurrent.ConcurrentHashMap

class AvalonLobby : AvalonState {
    private val usernameMap = ConcurrentHashMap<String, WsContext>()

    val players: List<Pair<String, WsContext>>
        get() {
            return usernameMap.entries.map { Pair(it.key, it.value) }
        }

    override fun receiveMessage(ctx: WsMessageContext, message: Message) {
        when (message) {
            is CreateUserMessage -> {
                // no double connections plz
                usernameMap.filter { entry ->
                    entry.value.session == ctx.session
                }.forEach { entry ->
                    usernameMap.remove(entry.key)
                }

                // if someone already connected with this username kick them lol
                usernameMap[message.username]?.session?.close()

                usernameMap[message.username] = ctx
                ctx.send(Klaxon().toJsonString(ConnectionSuccessMessage()))

                updateUserStatuses()
            }
            else -> println("fail")
        }
    }

    override fun receiveDisconnect(ctx: WsCloseContext) {
        usernameMap.filter { entry ->
            entry.value.session == ctx.session
        }.forEach { entry ->
            usernameMap.remove(entry.key)
        }

        updateUserStatuses()
    }

    private fun updateUserStatuses() {
        val updateMessage = UsernameUpdateMessage(
            usernameMap.map { entry ->
                UsernameStatus(entry.key, entry.value.session.isOpen)
            }
        )

        val json = Klaxon().toJsonString(updateMessage)

        usernameMap.values.forEach { ws ->
            ws.send(json)
        }
    }
}
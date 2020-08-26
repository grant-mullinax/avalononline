import Avalon.AvalonGame
import com.beust.klaxon.Klaxon
import io.javalin.Javalin
import io.javalin.websocket.WsContext

val games = mutableMapOf<String, AvalonGame>()

val WsContext.gameId: String get() = this.pathParam("game-id")

fun main(args: Array<String>) {
    Javalin.create {
        it.addStaticFiles("/public")
    }.apply {
        ws("/games/:game-id") { ws ->
            ws.onConnect { ctx ->
                println("connection from ${ctx.session.remoteAddress}")

                if (!games.containsKey(ctx.gameId)) {
                    games[ctx.gameId] = AvalonGame(ctx.gameId)
                }
            }

            ws.onClose { ctx ->
                if (!games.containsKey(ctx.gameId)) {
                    // TODO send back error
                    return@onClose
                }

                games[ctx.gameId]!!.receiveDisconnect(ctx)
            }

            ws.onMessage { ctx ->
                if (!games.containsKey(ctx.gameId)) {
                    // TODO send back error
                    return@onMessage
                }

                // println(ctx.message())
                val message = Klaxon().parse<Message>(ctx.message())
                    ?: // TODO send back error
                    return@onMessage

                games[ctx.gameId]!!.receiveMessage(ctx, message)
            }
        }
    }.start(7070)
}

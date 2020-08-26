package Avalon

import CreateUserMessage
import Message
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsMessageContext


class AvalonIngame(private val players: List<AvalonPlayer>) : AvalonState {
    var votingDenials: Int = 0
    val roundResults: List<RoundResult> = TODO()
    val gameState: IngameState
    val proposerIndex: Int = 0

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

    enum class RoundResult {
        GOODGUYWIN, GOODGUYLOSS, UNDECIDED
    }

    enum class VoteResponse {
        YES, NO, UNDECIDED
    }

    enum class MissionResponse {
        PASS, FAIL, UNDECIDED
    }

    sealed class IngameState {
        data class Proposal(
            val proposer: AvalonPlayer
        ) : IngameState()

        data class ProposalVote(
            val proposer: AvalonPlayer,
            val proposedTeam: List<AvalonPlayer>,
            val voteResponses: MutableMap<AvalonPlayer, VoteResponse>
        ) : IngameState()

        data class Mission(
            var missionResponses: MutableMap<AvalonPlayer, MissionResponse>
        ) : IngameState()
    }
}
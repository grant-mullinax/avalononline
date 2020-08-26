import com.beust.klaxon.TypeAdapter
import com.beust.klaxon.TypeFor
import kotlin.reflect.KClass

@TypeFor(field = "type", adapter = MessageTypeAdapter::class)
open class Message(val type: String)
data class CreateUserMessage(val username: String): Message("createUser")
data class StartGameMessage(
    val minionCount: Int
): Message("startGame")

data class IngameStateMessage(
    val possibleRounds: Int,
    val goodguyWins: Int,
    val badguyWins: Int,
    val missionDetails: MissionDetails,
    val votingDenials: Int
)


// change to sealed class
interface MissionDetails
data class VotingDetails(
    val proposedGroup: List<String>,
    val votedUsers: List<String>,
    val proposer: String,
    val haveYouVoted: Boolean
): MissionDetails

class MessageTypeAdapter: TypeAdapter<Message> {
    override fun classFor(type: Any): KClass<out Message> = when(type as String) {
        "createUser" -> CreateUserMessage::class
        "startGame" -> StartGameMessage::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
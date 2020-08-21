import com.beust.klaxon.TypeAdapter
import com.beust.klaxon.TypeFor
import kotlin.reflect.KClass

@TypeFor(field = "type", adapter = MessageTypeAdapter::class)
open class Message(val type: String)
data class CreateUserMessage(val username: String): Message("createUser")

class MessageTypeAdapter: TypeAdapter<Message> {
    override fun classFor(type: Any): KClass<out Message> = when(type as String) {
        "createUser" -> CreateUserMessage::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
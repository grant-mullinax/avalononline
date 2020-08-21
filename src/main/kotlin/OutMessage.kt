data class UsernameStatus(
    val username: String,
    val connected: Boolean
)

data class UsernameUpdateMessage(
    val statuses: Collection<UsernameStatus>,
    val type: String = "usernameUpdate"
)

data class ConnectionSuccessMessage(
    val type: String = "connectionSuccess"
)
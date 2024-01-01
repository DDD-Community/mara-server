package mara.server.domain.user

data class UserRequest(
    val name: String,
    val kaKaoId: String,
    val password: String,
)

class UserDto

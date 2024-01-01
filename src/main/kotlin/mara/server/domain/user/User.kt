package mara.server.domain.user

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import mara.server.domain.board.Board

enum class Role { USER }
@Entity
@Table(name = "writer")
class User(
    val name: String,
    val password: String,
    val kaKaoId: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0L

    @OneToMany(mappedBy = "writer", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val boardMutableList: MutableList<Board> = mutableListOf()
    val boardList: List<Board> get() = boardMutableList.toList()

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
        protected set
}

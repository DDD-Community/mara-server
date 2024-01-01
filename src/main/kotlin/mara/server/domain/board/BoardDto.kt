package mara.server.domain.board

data class BoardRequest(
    var title: String,
    val userId: Long,
)

data class BoardResponse(
    var title: String?,
    val boardId: Long?,
) {
    constructor(board: Board?) : this(
        title = board?.title,
        boardId = board?.boardId,
    )
}

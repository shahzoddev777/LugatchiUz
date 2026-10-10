package shahzod.projects.lugatchiuzdemo.domain.model

data class Word(
    val id: Int,
    val book: Int,
    val unit: Int,
    val english: String,
    val uzbek: String,
    val isFavourite: Boolean
)
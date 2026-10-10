package shahzod.projects.lugatchiuzdemo.presentation.viewmodel

data class WordOfDay(
    val word: String,
    val transcript: String,
    val partOfSpeech: String,
    val translation: String
)

data class HomeUiState(
    val userName: String,
    val wordOfDay: WordOfDay,
    val vocabularyCount: Int,
    val vocabularyProgress: Float,
    val streakDays: Int,
    val streakProgress: Float,
    val dailyDone: Int,
    val dailyTotal: Int,
    val recentWords: List<String>,
    val favouritesCount: Int,
    val collectionsCount: Int
) {
    val dailyProgress: Float
        get() = if (dailyTotal == 0) 0f else dailyDone.toFloat() / dailyTotal

    companion object {
        val Sample = HomeUiState(
            userName = "Aziza",
            wordOfDay = WordOfDay("resilient", "/rɪˈzɪliənt/", "sifat", "chidamli, bardoshli"),
            vocabularyCount = 248,
            vocabularyProgress = 0.62f,
            streakDays = 6,
            streakProgress = 0.86f,
            dailyDone = 12,
            dailyTotal = 20,
            recentWords = listOf("journey", "borrow", "achieve"),
            favouritesCount = 32,
            collectionsCount = 5
        )
    }
}
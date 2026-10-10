package shahzod.projects.lugatchiuzdemo.presentation.viewmodel

import shahzod.projects.lugatchiuzdemo.domain.model.Word


const val FAVOURITE_BOOK = 0   // "Favourite" tabi
const val BOOK_COUNT = 6
const val UNIT_COUNT = 30

data class WordsUiState(
    val selectedBook: Int = 1,
    val selectedUnit: Int = 1,
    val words: List<Word> = emptyList(),
    val expandedKeys: Set<String> = emptySet(),
    val isLoading: Boolean = true
) {
    val isFavouriteTab: Boolean get() = selectedBook == FAVOURITE_BOOK
}

/** Id faqat kitob ichida unikal, Favourite tabida esa kitoblar aralashadi. */
fun Word.key(): String = "$book:$id"

sealed interface WordsEvent {
    data class SelectBook(val book: Int) : WordsEvent
    data class SelectUnit(val unit: Int) : WordsEvent
    data class ToggleFavourite(val word: Word) : WordsEvent
    data class Speak(val word: Word) : WordsEvent
    data class ToggleExpand(val word: Word) : WordsEvent
}
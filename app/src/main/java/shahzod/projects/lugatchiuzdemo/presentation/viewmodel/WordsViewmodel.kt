package shahzod.projects.lugatchiuzdemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shahzod.projects.lugatchiuzdemo.domain.usecase.WordUseCases
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WordsViewModel @Inject constructor(
    private val useCases: WordUseCases
) : ViewModel() {

    private data class Selection(val book: Int, val unit: Int)

    private val selection = MutableStateFlow(Selection(book = 1, unit = 1))
    private val expandedKeys = MutableStateFlow<Set<String>>(emptySet())

    private val wordsFlow = selection.flatMapLatest { sel ->
        if (sel.book == FAVOURITE_BOOK) useCases.getFavourites()
        else useCases.getUnitWords(sel.book, sel.unit)
    }

    val uiState: StateFlow<WordsUiState> =
        combine(selection, expandedKeys, wordsFlow) { sel, expanded, words ->
            WordsUiState(
                selectedBook = sel.book,
                selectedUnit = sel.unit,
                words = words,
                expandedKeys = expanded,
                isLoading = false
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WordsUiState())

    fun onEvent(event: WordsEvent) {
        when (event) {
            is WordsEvent.SelectBook -> {
                expandedKeys.value = emptySet()
                selection.update { it.copy(book = event.book) }
            }
            is WordsEvent.SelectUnit -> {
                expandedKeys.value = emptySet()
                selection.update { it.copy(unit = event.unit) }
            }
            is WordsEvent.ToggleFavourite ->
                viewModelScope.launch { useCases.toggleFavourite(event.word) }
            is WordsEvent.Speak -> useCases.speak(event.word.english)
            is WordsEvent.ToggleExpand -> expandedKeys.update { keys ->
                val k = event.word.key()
                if (k in keys) keys - k else keys + k
            }
        }
    }
}
package shahzod.projects.lugatchiuzdemo.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shahzod.projects.lugatchiuzdemo.domain.model.Word
import shahzod.projects.lugatchiuzdemo.domain.usecase.WordUseCases
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val useCases: WordUseCases
) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("lugatchi_prefs", Context.MODE_PRIVATE)

    private val userNameFlow = MutableStateFlow(prefs.getString("user_name", "Foydalanuvchi") ?: "Foydalanuvchi")
    private val recentWordsFlow = MutableStateFlow(getSavedRecentWords())
    private val dailyDoneFlow = MutableStateFlow(prefs.getInt("daily_done", 8))
    private val dailyTotalFlow = MutableStateFlow(20)
    private val streakDaysFlow = MutableStateFlow(prefs.getInt("streak_days", 5))

    private val sampleWordOfDay = MutableStateFlow(
        WordOfDay(
            word = "resilient",
            transcript = "/rɪˈzɪliənt/",
            partOfSpeech = "sifat",
            translation = "chidamli, bardoshli"
        )
    )

    init {
        loadWordOfDay()
    }

    val uiState: StateFlow<HomeUiState> = combine(
        combine(userNameFlow, sampleWordOfDay, useCases.getFavourites()) { name, word, favs -> Triple(name, word, favs) },
        recentWordsFlow,
        dailyDoneFlow,
        streakDaysFlow
    ) { (name, wordOfDay, favourites), recents, done, streak ->
        HomeUiState(
            userName = name,
            wordOfDay = wordOfDay,
            vocabularyCount = 4000,
            vocabularyProgress = (done * 10 / 4000f).coerceAtLeast(0.05f),
            streakDays = streak,
            streakProgress = (streak % 7) / 7f,
            dailyDone = done,
            dailyTotal = 20,
            recentWords = recents,
            favouritesCount = favourites.size,
            collectionsCount = 6
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeUiState.Sample
    )

    fun speakWordOfDay() {
        useCases.speak(sampleWordOfDay.value.word)
    }

    fun addRecentWord(word: String) {
        val current = recentWordsFlow.value.toMutableList()
        current.remove(word)
        current.add(0, word)
        val trimmed = current.take(6)
        recentWordsFlow.value = trimmed
        saveRecentWords(trimmed)
    }

    fun incrementDailyDone() {
        val next = (dailyDoneFlow.value + 1).coerceAtMost(dailyTotalFlow.value)
        dailyDoneFlow.value = next
        prefs.edit().putInt("daily_done", next).apply()
    }

    fun setUserName(name: String) {
        userNameFlow.value = name
        prefs.edit().putString("user_name", name).apply()
    }

    private fun loadWordOfDay() {
        viewModelScope.launch {
            useCases.getUnitWords(book = 1, unit = 1).collect { words ->
                if (words.isNotEmpty()) {
                    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                    val selected = words[dayOfYear % words.size]
                    sampleWordOfDay.value = WordOfDay(
                        word = selected.english,
                        transcript = "/${selected.english}/",
                        partOfSpeech = "so'z",
                        translation = selected.uzbek
                    )
                }
            }
        }
    }

    private fun getSavedRecentWords(): List<String> {
        val raw = prefs.getString("recent_words", "journey,borrow,achieve") ?: "journey,borrow,achieve"
        return raw.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    private fun saveRecentWords(words: List<String>) {
        prefs.edit().putString("recent_words", words.joinToString(",")).apply()
    }
}

package shahzod.projects.lugatchiuzdemo.domain.repository

import kotlinx.coroutines.flow.Flow
import shahzod.projects.lugatchiuzdemo.domain.model.Word

interface WordRepository {
    fun getUnitWords(book: Int, unit: Int): Flow<List<Word>>
    fun getFavourites(): Flow<List<Word>>
    fun search(query: String): Flow<List<Word>>
    suspend fun setFavourite(book: Int, wordId: Int, isFavourite: Boolean)
}
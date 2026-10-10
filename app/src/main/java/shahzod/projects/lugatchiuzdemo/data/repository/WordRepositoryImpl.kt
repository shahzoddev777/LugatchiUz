package shahzod.projects.lugatchiuzdemo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import shahzod.projects.lugatchiuzdemo.data.local.BookDatabaseProvider
import shahzod.projects.lugatchiuzdemo.data.local.WordDao
import shahzod.projects.lugatchiuzdemo.data.mapper.toDomain
import shahzod.projects.lugatchiuzdemo.domain.model.Word
import shahzod.projects.lugatchiuzdemo.domain.repository.WordRepository
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val provider: BookDatabaseProvider
) : WordRepository {

    override fun getUnitWords(book: Int, unit: Int): Flow<List<Word>> =
        provider.dao(book).getUnitWords(unit)
            .map { list -> list.map { it.toDomain(book) } }

    override fun getFavourites(): Flow<List<Word>> =
        combineAllBooks { book, dao ->
            dao.getFavourites().map { list -> list.map { it.toDomain(book) } }
        }

    override fun search(query: String): Flow<List<Word>> {
        val q = query.trim()
        if (q.isEmpty()) return flowOf(emptyList())
        return combineAllBooks { book, dao ->
            dao.search(q).map { list -> list.map { it.toDomain(book) } }
        }.map { words -> words.sortedBy { it.english.lowercase() }.take(50) }
    }

    override suspend fun setFavourite(book: Int, wordId: Int, isFavourite: Boolean) {
        provider.dao(book).setFavourite(wordId, if (isFavourite) 1 else 0)
    }

    /** 6 ta bazadan kelgan oqimlarni bitta ro'yxatga birlashtiradi (book tartibida). */
    private fun combineAllBooks(
        source: (book: Int, dao: WordDao) -> Flow<List<Word>>
    ): Flow<List<Word>> =
        combine(provider.allDaos().map { (book, dao) -> source(book, dao) }) { lists ->
            lists.toList().flatten()
        }
}
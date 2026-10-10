package shahzod.projects.lugatchiuzdemo.domain.usecase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import shahzod.projects.lugatchiuzdemo.domain.model.Word
import shahzod.projects.lugatchiuzdemo.domain.repository.SpeechPlayer
import shahzod.projects.lugatchiuzdemo.domain.repository.WordRepository
import javax.inject.Inject

interface WordUseCases {
    fun getUnitWords(book: Int, unit: Int): Flow<List<Word>>
    fun getFavourites(): Flow<List<Word>>
    fun search(query: String): Flow<List<Word>>
    suspend fun toggleFavourite(word: Word)
    fun speak(text: String)
}

class WordUseCasesImpl @Inject constructor(
    private val repository: WordRepository,
    private val speechPlayer: SpeechPlayer
) : WordUseCases {

    override fun getUnitWords(book: Int, unit: Int): Flow<List<Word>> =
        repository.getUnitWords(book, unit)

    override fun getFavourites(): Flow<List<Word>> =
        repository.getFavourites()

    override fun search(query: String): Flow<List<Word>> =
        repository.search(query)

    override suspend fun toggleFavourite(word: Word) =
        repository.setFavourite(word.book, word.id, !word.isFavourite)

    override fun speak(text: String) = speechPlayer.speak(text)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindWordUseCases(impl: WordUseCasesImpl): WordUseCases
}
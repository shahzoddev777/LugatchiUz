package shahzod.projects.lugatchiuzdemo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import shahzod.projects.lugatchiuzdemo.data.repository.WordRepositoryImpl
import shahzod.projects.lugatchiuzdemo.domain.repository.WordRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWordRepository(impl: WordRepositoryImpl): WordRepository
}
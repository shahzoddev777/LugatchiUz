package shahzod.projects.lugatchiuzdemo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import shahzod.projects.lugatchiuzdemo.data.tts.AndroidSpeechPlayer
import shahzod.projects.lugatchiuzdemo.domain.repository.SpeechPlayer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TtsModule {
    @Binds
    @Singleton
    abstract fun bindSpeechPlayer(impl: AndroidSpeechPlayer): SpeechPlayer
}
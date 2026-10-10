package shahzod.projects.lugatchiuzdemo.data.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import shahzod.projects.lugatchiuzdemo.domain.repository.SpeechPlayer
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidSpeechPlayer @Inject constructor(
    @ApplicationContext context: Context
) : SpeechPlayer {

    private var ready = false
    private lateinit var tts: TextToSpeech

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                ready = true
            }
        }
    }

    override fun speak(text: String) {
        if (ready) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
    }
}
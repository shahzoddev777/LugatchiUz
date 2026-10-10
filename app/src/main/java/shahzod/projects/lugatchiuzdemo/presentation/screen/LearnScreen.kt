package shahzod.projects.lugatchiuzdemo.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import shahzod.projects.lugatchiuzdemo.domain.model.Word
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.BOOK_COUNT
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.UNIT_COUNT
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.WordsEvent
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.WordsUiState

enum class LearnMode {
    Flashcards, Quiz
}

@Composable
fun LearnScreen(
    state: WordsUiState,
    onEvent: (WordsEvent) -> Unit,
    onCardMastered: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mode by remember { mutableStateOf(LearnMode.Flashcards) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val c = Lugatchi.colors
    val words = state.words

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(c.background)
            .statusBarsPadding()
    ) {
        // Sarlavha va Rejim tanlash
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "O'rganish va Mashq",
                style = Lugatchi.type.title,
                color = c.textPrimary
            )
            Spacer(Modifier.height(12.dp))

            // Mode Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(c.surface)
                    .border(1.dp, c.outline, CircleShape)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (mode == LearnMode.Flashcards) c.primary else Color.Transparent)
                        .clickable { mode = LearnMode.Flashcards; currentIndex = 0; isFlipped = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Kartochkalar",
                        style = Lugatchi.type.button,
                        color = if (mode == LearnMode.Flashcards) c.onPrimary else c.textSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (mode == LearnMode.Quiz) c.primary else Color.Transparent)
                        .clickable { mode = LearnMode.Quiz; currentIndex = 0; isFlipped = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Viktorina (Quiz)",
                        style = Lugatchi.type.button,
                        color = if (mode == LearnMode.Quiz) c.onPrimary else c.textSecondary
                    )
                }
            }
        }

        // Kitob tanlash
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((1..BOOK_COUNT).toList()) { book ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (state.selectedBook == book) c.primary else c.surface)
                        .border(1.dp, if (state.selectedBook == book) Color.Transparent else c.outline, CircleShape)
                        .clickable { onEvent(WordsEvent.SelectBook(book)); currentIndex = 0 }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$book-Kitob",
                        style = Lugatchi.type.button,
                        color = if (state.selectedBook == book) c.onPrimary else c.textPrimary
                    )
                }
            }
        }

        // Unit tanlash
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((1..UNIT_COUNT).toList()) { unit ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (state.selectedUnit == unit) c.primarySoft else c.surface)
                        .clickable { onEvent(WordsEvent.SelectUnit(unit)); currentIndex = 0 }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$unit-Unit",
                        style = Lugatchi.type.caption,
                        fontWeight = if (state.selectedUnit == unit) FontWeight.Bold else FontWeight.Medium,
                        color = if (state.selectedUnit == unit) c.primary else c.textSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (words.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("So'zlar yuklanmoqda...", style = Lugatchi.type.body, color = c.textSecondary)
            }
        } else {
            val safeIndex = currentIndex.coerceIn(0, words.size - 1)
            val currentWord = words[safeIndex]

            when (mode) {
                LearnMode.Flashcards -> {
                    FlashcardView(
                        word = currentWord,
                        currentIndex = safeIndex,
                        total = words.size,
                        isFlipped = isFlipped,
                        onFlip = { isFlipped = !isFlipped },
                        onSpeak = { onEvent(WordsEvent.Speak(currentWord)) },
                        onNext = {
                            onCardMastered()
                            isFlipped = false
                            if (currentIndex < words.size - 1) currentIndex++ else currentIndex = 0
                        },
                        onRepeat = {
                            isFlipped = false
                            if (currentIndex < words.size - 1) currentIndex++ else currentIndex = 0
                        }
                    )
                }
                LearnMode.Quiz -> {
                    QuizView(
                        currentWord = currentWord,
                        allWords = words,
                        currentIndex = safeIndex,
                        total = words.size,
                        onAnswerSelected = { isCorrect ->
                            if (isCorrect) onCardMastered()
                            if (currentIndex < words.size - 1) currentIndex++ else currentIndex = 0
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FlashcardView(
    word: Word,
    currentIndex: Int,
    total: Int,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onSpeak: () -> Unit,
    onNext: () -> Unit,
    onRepeat: () -> Unit
) {
    val c = Lugatchi.colors
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "cardFlip"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress
        Text(
            text = "${currentIndex + 1} / $total kartochka",
            style = Lugatchi.type.caption,
            color = c.textSecondary
        )
        Spacer(Modifier.height(8.dp))
        ProgressBar(
            progress = (currentIndex + 1).toFloat() / total,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Main Card
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = if (isFlipped) c.primarySoft else c.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clickable(onClick = onFlip)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    // Front Face
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ENGLISH",
                            style = Lugatchi.type.label,
                            color = c.textSecondary
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = word.english,
                            style = Lugatchi.type.display,
                            color = c.textPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        IconButton(onClick = onSpeak) {
                            Icon(
                                imageVector = Icons.Outlined.VolumeUp,
                                contentDescription = "Talaffuz",
                                tint = c.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = "Aylantirish uchun bosing",
                            style = Lugatchi.type.caption,
                            color = c.textSecondary
                        )
                    }
                } else {
                    // Back Face (Uzbek)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.graphicsLayer { rotationY = 180f }
                    ) {
                        Text(
                            text = "O'ZBEKCHA TARJIMA",
                            style = Lugatchi.type.label,
                            color = c.primary
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = word.uzbek,
                            style = Lugatchi.type.title,
                            color = c.textPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onRepeat,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = c.surface, contentColor = c.textPrimary),
                border = androidx.compose.foundation.BorderStroke(1.dp, c.outline),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Icon(Icons.Outlined.Refresh, contentDescription = null, tint = c.textSecondary)
                Spacer(Modifier.width(8.dp))
                Text("Takrorlash", style = Lugatchi.type.button)
            }

            Button(
                onClick = onNext,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = c.primary, contentColor = c.onPrimary),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = c.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Bilaman", style = Lugatchi.type.button)
            }
        }
    }
}

@Composable
private fun QuizView(
    currentWord: Word,
    allWords: List<Word>,
    currentIndex: Int,
    total: Int,
    onAnswerSelected: (isCorrect: Boolean) -> Unit
) {
    val c = Lugatchi.colors

    val options = remember(currentWord, allWords) {
        val wrongOptions = allWords.filter { it.id != currentWord.id }.shuffled().take(3).map { it.uzbek }
        (wrongOptions + currentWord.uzbek).shuffled()
    }

    var selectedOption by remember(currentWord) { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Savol ${currentIndex + 1} / $total",
            style = Lugatchi.type.caption,
            color = c.textSecondary
        )
        Spacer(Modifier.height(8.dp))
        ProgressBar(progress = (currentIndex + 1).toFloat() / total, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = c.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, c.outline),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("To'g'ri tarjimani tanlang:", style = Lugatchi.type.caption, color = c.textSecondary)
                Spacer(Modifier.height(12.dp))
                Text(currentWord.english, style = Lugatchi.type.display, color = c.textPrimary)
            }
        }

        Spacer(Modifier.height(16.dp))

        options.forEach { option ->
            val isSelected = selectedOption == option
            val isCorrect = option == currentWord.uzbek

            val bg = when {
                selectedOption == null -> c.surface
                isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                isSelected && !isCorrect -> Color(0xFFE53935).copy(alpha = 0.15f)
                else -> c.surface
            }

            val borderColor = when {
                selectedOption == null -> c.outline
                isCorrect -> Color(0xFF4CAF50)
                isSelected && !isCorrect -> Color(0xFFE53935)
                else -> c.outline
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = bg,
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable(enabled = selectedOption == null) {
                        selectedOption = option
                        val correct = option == currentWord.uzbek
                        onAnswerSelected(correct)
                    }
            ) {
                Text(
                    text = option,
                    style = Lugatchi.type.body,
                    fontWeight = FontWeight.SemiBold,
                    color = c.textPrimary,
                    modifier = Modifier.padding(18.dp)
                )
            }
        }
    }
}

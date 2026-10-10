package shahzod.projects.lugatchiuzdemo.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import shahzod.projects.lugatchiuzdemo.domain.model.Word
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.BOOK_COUNT
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.FAVOURITE_BOOK
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.UNIT_COUNT
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.WordsEvent
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.WordsUiState
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.key

@Composable
fun DictionaryScreen(
    state: WordsUiState,
    onEvent: (WordsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val c = Lugatchi.colors

    val filteredWords = remember(state.words, searchQuery) {
        if (searchQuery.isBlank()) state.words
        else {
            val q = searchQuery.trim().lowercase()
            state.words.filter {
                it.english.lowercase().contains(q) || it.uzbek.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(c.background)
            .statusBarsPadding()
    ) {
        // Sarlavha va Qidiruv
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Essential Lug'at",
                style = Lugatchi.type.title,
                color = c.textPrimary
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search word / So'zni qidirish...", style = Lugatchi.type.body, color = c.textSecondary) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = c.textSecondary) },
                singleLine = true,
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = c.surface,
                    unfocusedContainerColor = c.surface,
                    focusedBorderColor = c.primary,
                    unfocusedBorderColor = c.outline
                )
            )
        }

        // Kitob tanlash (Book 1..6 & Favourites)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                BookChip(
                    title = "Sevimlilar",
                    isSelected = state.selectedBook == FAVOURITE_BOOK,
                    onClick = { onEvent(WordsEvent.SelectBook(FAVOURITE_BOOK)) }
                )
            }
            items((1..BOOK_COUNT).toList()) { book ->
                BookChip(
                    title = "$book-Kitob",
                    isSelected = state.selectedBook == book,
                    onClick = { onEvent(WordsEvent.SelectBook(book)) }
                )
            }
        }

        // Unit tanlash (faqat kitob tanlanganda)
        AnimatedVisibility(
            visible = !state.isFavouriteTab,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items((1..UNIT_COUNT).toList()) { unit ->
                    UnitChip(
                        title = "$unit-Unit",
                        isSelected = state.selectedUnit == unit,
                        onClick = { onEvent(WordsEvent.SelectUnit(unit)) }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // So'zlar ro'yxati
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = c.primary)
            }
        } else if (filteredWords.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (searchQuery.isNotBlank()) "Afsuski so'z topilmadi" else "Hali so'zlar mavjud emas",
                    style = Lugatchi.type.body,
                    color = c.textSecondary
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredWords, key = { it.key() }) { word ->
                    WordCardItem(
                        word = word,
                        isExpanded = word.key() in state.expandedKeys,
                        onExpandToggle = { onEvent(WordsEvent.ToggleExpand(word)) },
                        onSpeak = { onEvent(WordsEvent.Speak(word)) },
                        onFavouriteToggle = { onEvent(WordsEvent.ToggleFavourite(word)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val c = Lugatchi.colors
    val bg = if (isSelected) c.primary else c.surface
    val contentColor = if (isSelected) c.onPrimary else c.textPrimary
    val border = if (isSelected) Color.Transparent else c.outline

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bg)
            .border(1.dp, border, CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            style = Lugatchi.type.button,
            color = contentColor
        )
    }
}

@Composable
private fun UnitChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val c = Lugatchi.colors
    val bg = if (isSelected) c.primarySoft else c.surface
    val contentColor = if (isSelected) c.primary else c.textSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            style = Lugatchi.type.caption,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor
        )
    }
}

@Composable
private fun WordCardItem(
    word: Word,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onSpeak: () -> Unit,
    onFavouriteToggle: () -> Unit
) {
    val c = Lugatchi.colors

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = c.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, c.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onExpandToggle)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word.english,
                        style = Lugatchi.type.cardTitle,
                        color = c.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = word.uzbek,
                        style = Lugatchi.type.body,
                        color = c.primary,
                        maxLines = if (isExpanded) 10 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onSpeak) {
                        Icon(
                            imageVector = Icons.Outlined.VolumeUp,
                            contentDescription = "Talaffuz",
                            tint = c.primary
                        )
                    }

                    IconButton(onClick = onFavouriteToggle) {
                        Icon(
                            imageVector = if (word.isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Sevimli",
                            tint = if (word.isFavourite) Color(0xFFE53935) else c.textSecondary
                        )
                    }
                }
            }

            if (isExpanded) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(c.primarySoft, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kitob: ${word.book} | Unit: ${word.unit}",
                        style = Lugatchi.type.caption,
                        color = c.textSecondary
                    )
                }
            }
        }
    }
}

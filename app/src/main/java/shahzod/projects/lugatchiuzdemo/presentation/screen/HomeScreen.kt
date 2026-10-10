package shahzod.projects.lugatchiuzdemo.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.HomeUiState
import java.util.Calendar

@Composable
fun HomeScreen(
    state: HomeUiState,
    selectedTab: HomeTab = HomeTab.Home,
    onTabSelected: (HomeTab) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onWordOfDayClick: () -> Unit = {},
    onSpeakClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onRecentWordClick: (String) -> Unit = {},
    onFavouritesClick: () -> Unit = {},
    onCollectionsClick: () -> Unit = {}
) {
    var uzToEn by rememberSaveable { mutableStateOf(true) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Lugatchi.colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HomeHeader(
                    greeting = greetingForNow(),
                    name = state.userName,
                    onProfileClick = onProfileClick
                )
            }
            item {
                SearchEntry(
                    fromLang = if (uzToEn) "UZ" else "EN",
                    toLang = if (uzToEn) "EN" else "UZ",
                    onClick = onSearchClick,
                    onSwapClick = { uzToEn = !uzToEn }
                )
            }
            item {
                WordOfDayCard(
                    word = state.wordOfDay,
                    onClick = onWordOfDayClick,
                    onSpeakClick = onSpeakClick
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard("Lug'at", "${state.vocabularyCount}", state.vocabularyProgress, Modifier.weight(1f))
                    StatCard("Ketma-ket kunlar", "${state.streakDays} kun", state.streakProgress, Modifier.weight(1f))
                }
            }
            item {
                DailyWordsCard(
                    done = state.dailyDone,
                    total = state.dailyTotal,
                    progress = state.dailyProgress,
                    onContinueClick = onContinueClick
                )
            }
            if (state.recentWords.isNotEmpty()) {
                item { RecentWords(state.recentWords, onRecentWordClick) }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    QuickTile(Icons.Outlined.FavoriteBorder, "Sevimlilar", state.favouritesCount, onFavouritesClick, Modifier.weight(1f))
                    QuickTile(Icons.Outlined.Email, "To'plamlar", state.collectionsCount, onCollectionsClick, Modifier.weight(1f))
                }
            }
        }

        LugatchiBottomBar(
            selected = selectedTab,
            onSelect = onTabSelected,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

private fun greetingForNow(): String =
    when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Xayrli tong"
        in 12..17 -> "Xayrli kun"
        in 18..22 -> "Xayrli kech"
        else -> "Xayrli tun"
    }

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun HomeScreenPreview() {
    LugatchiTheme(darkTheme = false) { HomeScreen(HomeUiState.Sample) }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun HomeScreenDarkPreview() {
    LugatchiTheme(darkTheme = true) { HomeScreen(HomeUiState.Sample) }
}
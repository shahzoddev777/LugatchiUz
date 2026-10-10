package shahzod.projects.lugatchiuzdemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import shahzod.projects.lugatchiuzdemo.presentation.screen.DictionaryScreen
import shahzod.projects.lugatchiuzdemo.presentation.screen.HomeScreen
import shahzod.projects.lugatchiuzdemo.presentation.screen.HomeTab
import shahzod.projects.lugatchiuzdemo.presentation.screen.LearnScreen
import shahzod.projects.lugatchiuzdemo.presentation.screen.Lugatchi
import shahzod.projects.lugatchiuzdemo.presentation.screen.LugatchiBottomBar
import shahzod.projects.lugatchiuzdemo.presentation.screen.LugatchiTheme
import shahzod.projects.lugatchiuzdemo.presentation.screen.ProfileScreen
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.FAVOURITE_BOOK
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.HomeViewModel
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.WordsEvent
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.WordsViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDarkTheme by rememberSaveable { mutableStateOf(systemDark) }

            LugatchiTheme(darkTheme = isDarkTheme) {
                MainContainer(
                    isDarkTheme = isDarkTheme,
                    onDarkThemeToggle = { isDarkTheme = it }
                )
            }
        }
    }
}

@Composable
fun MainContainer(
    isDarkTheme: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    wordsViewModel: WordsViewModel = hiltViewModel()
) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Home) }

    val homeUiState by homeViewModel.uiState.collectAsState()
    val wordsUiState by wordsViewModel.uiState.collectAsState()

    val colors = Lugatchi.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Crossfade(
            targetState = selectedTab,
            label = "tab_crossfade",
            modifier = Modifier.fillMaxSize()
        ) { tab ->
            when (tab) {
                HomeTab.Home -> {
                    HomeScreen(
                        state = homeUiState,
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                        onProfileClick = { selectedTab = HomeTab.Profile },
                        onSearchClick = { selectedTab = HomeTab.Dictionary },
                        onWordOfDayClick = { selectedTab = HomeTab.Dictionary },
                        onSpeakClick = { homeViewModel.speakWordOfDay() },
                        onContinueClick = { selectedTab = HomeTab.Learn },
                        onRecentWordClick = { word ->
                            wordsViewModel.onEvent(WordsEvent.SelectBook(1))
                            selectedTab = HomeTab.Dictionary
                        },
                        onFavouritesClick = {
                            wordsViewModel.onEvent(WordsEvent.SelectBook(FAVOURITE_BOOK))
                            selectedTab = HomeTab.Dictionary
                        },
                        onCollectionsClick = { selectedTab = HomeTab.Dictionary }
                    )
                }
                HomeTab.Dictionary -> {
                    DictionaryScreen(
                        state = wordsUiState,
                        onEvent = wordsViewModel::onEvent
                    )
                }
                HomeTab.Learn -> {
                    LearnScreen(
                        state = wordsUiState,
                        onEvent = wordsViewModel::onEvent,
                        onCardMastered = { homeViewModel.incrementDailyDone() }
                    )
                }
                HomeTab.Profile -> {
                    ProfileScreen(
                        state = homeUiState,
                        isDarkTheme = isDarkTheme,
                        onDarkThemeToggle = onDarkThemeToggle,
                        onNameChange = { name -> homeViewModel.setUserName(name) },
                        onFavouritesClick = {
                            wordsViewModel.onEvent(WordsEvent.SelectBook(FAVOURITE_BOOK))
                            selectedTab = HomeTab.Dictionary
                        }
                    )
                }
            }
        }

        if (selectedTab != HomeTab.Home) {
            LugatchiBottomBar(
                selected = selectedTab,
                onSelect = { selectedTab = it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

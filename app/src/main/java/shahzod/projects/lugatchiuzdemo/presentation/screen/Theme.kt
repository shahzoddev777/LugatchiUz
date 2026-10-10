package shahzod.projects.lugatchiuzdemo.presentation.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/* ---------- Ranglar (design tokens) ---------- */

@Immutable
data class LugatchiPalette(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val onPrimary: Color,
    val primarySoft: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val outline: Color,
    val track: Color
)

val LightPalette = LugatchiPalette(
    background = Color(0xFFF4F5FB),
    surface = Color.White,
    primary = Color(0xFF3B4CDB),
    onPrimary = Color.White,
    primarySoft = Color(0xFFEEF0FF),
    textPrimary = Color(0xFF12131A),
    textSecondary = Color(0xFF666B7D),
    outline = Color(0xFFE9EAF3),
    track = Color(0xFFE6E8F2)
)

val DarkPalette = LugatchiPalette(
    background = Color(0xFF0E1020),
    surface = Color(0xFF171A2E),
    primary = Color(0xFF4A5AE8),
    onPrimary = Color.White,
    primarySoft = Color(0xFF252A4D),
    textPrimary = Color(0xFFF2F3FA),
    textSecondary = Color(0xFFA3A8C0),
    outline = Color(0xFF262A45),
    track = Color(0xFF2A2F4F)
)

private val LocalPalette = staticCompositionLocalOf { LightPalette }

/* ---------- Tipografiya ---------- */

// Dizayndagi shriftga yaqin: Plus Jakarta Sans yoki Manrope (Google Fonts).
// Ularni res/font ga qo'shgach, shu yerda FontFamily(Font(R.font.xxx, ...)) bilan almashtiring.
val LugatchiFont: FontFamily = FontFamily.Default

object LugatchiType {
    val display = TextStyle(fontFamily = LugatchiFont, fontSize = 44.sp, lineHeight = 48.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
    val title = TextStyle(fontFamily = LugatchiFont, fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold)
    val translation = TextStyle(fontFamily = LugatchiFont, fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.SemiBold)
    val stat = TextStyle(fontFamily = LugatchiFont, fontSize = 30.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold)
    val headline = TextStyle(fontFamily = LugatchiFont, fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold)
    val cardTitle = TextStyle(fontFamily = LugatchiFont, fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold)
    val body = TextStyle(fontFamily = LugatchiFont, fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium)
    val caption = TextStyle(fontFamily = LugatchiFont, fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium)
    val label = TextStyle(fontFamily = LugatchiFont, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
    val button = TextStyle(fontFamily = LugatchiFont, fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold)
}

/* ---------- Kirish nuqtalari ---------- */

object Lugatchi {
    val colors: LugatchiPalette
        @Composable @ReadOnlyComposable get() = LocalPalette.current
    val type = LugatchiType
}

@Composable
fun LugatchiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalPalette provides if (darkTheme) DarkPalette else LightPalette) {
        MaterialTheme(content = content)
    }
}
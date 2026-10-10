package shahzod.projects.lugatchiuzdemo.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.WordOfDay

/* ---------- Umumiy bloklar ---------- */

@Composable
fun LugatchiCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val c = Lugatchi.colors
    Surface(
        modifier = modifier,
        shape = shape,
        color = c.surface,
        border = BorderStroke(1.dp, c.outline)
    ) {
        Box(
            if (onClick != null) Modifier.clickable(
                role = Role.Button,
                onClick = onClick
            ) else Modifier
        ) {
            content()
        }
    }
}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val c = Lugatchi.colors
    val p = progress.coerceIn(0f, 1f)
    Box(
        modifier
            .height(8.dp)
            .clip(CircleShape)
            .background(c.track)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(p, 0f..1f) }
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(p)
                .clip(CircleShape)
                .background(c.primary)
        )
    }
}

/* ---------- Sarlavha ---------- */

@Composable
fun HomeHeader(
    greeting: String,
    name: String,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("$greeting,", style = Lugatchi.type.body, color = c.textSecondary)
            Text(
                name,
                style = Lugatchi.type.title,
                color = c.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(c.primarySoft)
                .clickable(role = Role.Button, onClick = onProfileClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = "Profil",
                tint = c.primary,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

/* ---------- Qidiruv (Home da faqat kirish nuqtasi) ---------- */

@Composable
fun SearchEntry(
    fromLang: String,
    toLang: String,
    onClick: () -> Unit,
    onSwapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    LugatchiCard(modifier.fillMaxWidth(), shape = CircleShape, onClick = onClick) {
        Row(
            Modifier
                .heightIn(min = 60.dp)
                .padding(start = 22.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = null,
                tint = c.textSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "So'z yoki tarjimani qidiring",
                style = Lugatchi.type.body,
                color = c.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Row(
                Modifier
                    .heightIn(min = 44.dp)
                    .clip(CircleShape)
                    .background(c.primarySoft)
                    .clickable(
                        onClickLabel = "Tilni almashtirish",
                        role = Role.Button,
                        onClick = onSwapClick
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(fromLang, style = Lugatchi.type.button, color = c.primary)
                Icon(
                    Icons.Outlined.SwapHoriz,
                    contentDescription = null,
                    tint = c.primary,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(18.dp)
                )
                Text(toLang, style = Lugatchi.type.button, color = c.primary)
            }
        }
    }
}

/* ---------- Kunning so'zi ---------- */

@Composable
fun WordOfDayCard(
    word: WordOfDay,
    onClick: () -> Unit,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    val shape = RoundedCornerShape(28.dp)
    Box(
        modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = shape,
                ambientColor = c.primary.copy(alpha = 0.25f),
                spotColor = c.primary.copy(alpha = 0.35f)
            )
            .clip(shape)
            .background(c.primary)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(24.dp)
    ) {
        Column(Modifier.padding(end = 56.dp)) {
            Text(
                "KUNNING SO'ZI",
                style = Lugatchi.type.label,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(14.dp))
            Text(word.word, style = Lugatchi.type.display, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(
                "${word.transcript} · ${word.partOfSpeech}",
                style = Lugatchi.type.body,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(14.dp))
            Text(word.translation, style = Lugatchi.type.translation, color = Color.White)
        }
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .clickable(role = Role.Button, onClick = onSpeakClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.VolumeUp,
                contentDescription = "Talaffuzni eshitish",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/* ---------- Statistika ---------- */

@Composable
fun StatCard(label: String, value: String, progress: Float, modifier: Modifier = Modifier) {
    val c = Lugatchi.colors
    LugatchiCard(modifier) {
        Column(Modifier.padding(20.dp)) {
            Text(label, style = Lugatchi.type.caption, color = c.textSecondary)
            Spacer(Modifier.height(4.dp))
            Text(value, style = Lugatchi.type.stat, color = c.textPrimary)
            Spacer(Modifier.height(12.dp))
            ProgressBar(progress, Modifier.fillMaxWidth())
        }
    }
}

/* ---------- Kundalik so'zlar ---------- */

@Composable
fun DailyWordsCard(
    done: Int,
    total: Int,
    progress: Float,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    LugatchiCard(modifier.fillMaxWidth()) {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(c.primarySoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.School,
                    contentDescription = null,
                    tint = c.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("Kundalik so'zlar", style = Lugatchi.type.cardTitle, color = c.textPrimary)
                Text(
                    "$done / $total kartochka",
                    style = Lugatchi.type.caption,
                    color = c.textSecondary
                )
                Spacer(Modifier.height(8.dp))
                ProgressBar(progress, Modifier.fillMaxWidth())
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = onContinueClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = c.primary,
                    contentColor = c.onPrimary
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text("Davom", style = Lugatchi.type.button)
            }
        }
    }
}

/* ---------- Yaqinda ko'rilgan ---------- */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecentWords(
    words: List<String>,
    onWordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Yaqinda ko'rilgan", style = Lugatchi.type.headline, color = c.textPrimary)
        Spacer(Modifier.height(12.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            words.forEach { w ->
                LugatchiCard(shape = CircleShape, onClick = { onWordClick(w) }) {
                    Text(
                        text = w,
                        style = Lugatchi.type.cardTitle,
                        color = c.textPrimary,
                        modifier = Modifier.padding(horizontal = 22.dp, vertical = 14.dp)
                    )
                }
            }
        }
    }
}

/* ---------- Sevimlilar / To'plamlar ---------- */

@Composable
fun QuickTile(
    icon: ImageVector,
    label: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    LugatchiCard(modifier, onClick = onClick) {
        Row(
            Modifier
                .heightIn(min = 56.dp)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = c.primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text(
                label,
                style = Lugatchi.type.cardTitle,
                color = c.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text("$count", style = Lugatchi.type.cardTitle, color = c.textSecondary)
        }
    }
}
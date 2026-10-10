package shahzod.projects.lugatchiuzdemo.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class HomeTab(val label: String, val icon: ImageVector) {
    Home("Bosh sahifa", Icons.Outlined.Home),
    Dictionary("Lug'at", Icons.Outlined.MenuBook),
    Learn("O'rganish", Icons.Outlined.School),
    Profile("Profil", Icons.Outlined.Person)
}

/** Suzuvchi "pill" navigatsiya paneli. Chaqirganda navigationBarsPadding() bering. */
@Composable
fun LugatchiBottomBar(
    selected: HomeTab,
    onSelect: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    val shape = RoundedCornerShape(36.dp)
    Row(
        modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = shape,
                ambientColor = c.primary.copy(alpha = 0.12f),
                spotColor = c.primary.copy(alpha = 0.20f)
            )
            .clip(shape)
            .background(c.surface)
            .border(1.dp, c.outline, shape)
            .padding(8.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeTab.values().forEach { tab ->
            BottomBarItem(
                tab = tab,
                isSelected = tab == selected,
                onClick = { onSelect(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    tab: HomeTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    val tint = if (isSelected) c.primary else c.textSecondary
    Column(
        modifier
            .clip(RoundedCornerShape(28.dp))
            .background(if (isSelected) c.primarySoft else Color.Transparent)
            .selectable(selected = isSelected, role = Role.Tab, onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(tab.icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Text(
            tab.label,
            style = Lugatchi.type.caption,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = tint,
            maxLines = 1
        )
    }
}
package shahzod.projects.lugatchiuzdemo.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import shahzod.projects.lugatchiuzdemo.presentation.viewmodel.HomeUiState

@Composable
fun ProfileScreen(
    state: HomeUiState,
    isDarkTheme: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit,
    onNameChange: (String) -> Unit,
    onFavouritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    var showEditNameDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(state.userName) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(c.background)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Mening Profilim",
            style = Lugatchi.type.title,
            color = c.textPrimary
        )

        Spacer(Modifier.height(24.dp))

        // User Avatar & Name Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = c.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, c.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(c.primarySoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = c.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.userName,
                        style = Lugatchi.type.headline,
                        color = c.textPrimary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Essential 4000 Words foydalanuvchisi",
                        style = Lugatchi.type.caption,
                        color = c.textSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(c.primarySoft)
                        .clickable(role = Role.Button) {
                            tempName = state.userName
                            showEditNameDialog = true
                        }
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Ismni o'zgartirish",
                        tint = c.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(
                icon = Icons.Outlined.Star,
                title = "Lug'at zaxirasi",
                value = "${state.vocabularyCount} so'z",
                modifier = Modifier.weight(1f)
            )

            ProfileStatCard(
                icon = Icons.Outlined.Star,
                title = "Ketma-ket kun",
                value = "${state.streakDays} kun",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Sozlamalar",
            style = Lugatchi.type.headline,
            color = c.textPrimary
        )

        Spacer(Modifier.height(12.dp))

        // Settings List
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = c.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, c.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Dark mode item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = c.primary)
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Tungi rejim (Dark Mode)",
                        style = Lugatchi.type.body,
                        color = c.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = onDarkThemeToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = c.onPrimary,
                            checkedTrackColor = c.primary
                        )
                    )
                }

                // Favourites item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onFavouritesClick)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, tint = c.primary)
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Sevimlilar ro'yxati",
                        style = Lugatchi.type.body,
                        color = c.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${state.favouritesCount} so'z",
                        style = Lugatchi.type.caption,
                        color = c.textSecondary
                    )
                }

                // Info item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = c.primary)
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Ilova haqida",
                        style = Lugatchi.type.body,
                        color = c.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "v1.0.0",
                        style = Lugatchi.type.caption,
                        color = c.textSecondary
                    )
                }
            }
        }
    }

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("Ismingizni kiriting") },
            text = {
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    singleLine = true,
                    label = { Text("Ism") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (tempName.isNotBlank()) onNameChange(tempName)
                        showEditNameDialog = false
                    }
                ) {
                    Text("Saqlash")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Bekor qilish")
                }
            }
        )
    }
}

@Composable
private fun ProfileStatCard(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val c = Lugatchi.colors
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = c.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, c.outline),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = c.primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(10.dp))
            Text(title, style = Lugatchi.type.caption, color = c.textSecondary)
            Spacer(Modifier.height(2.dp))
            Text(value, style = Lugatchi.type.cardTitle, color = c.textPrimary)
        }
    }
}

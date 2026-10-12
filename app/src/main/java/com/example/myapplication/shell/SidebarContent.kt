package com.example.myapplication.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.navigation.SidebarItem
import com.example.myapplication.navigation.SidebarSection
import com.example.myapplication.ui.theme.*

private val SidebarBg = Color(0xFF050A18)
private val SelectedBg = Color(0xFF172033)
private val SelectedBorder = Color(0xFF3A4766)

@Composable
fun SidebarContent(
    user: LoginResponse?,
    sections: List<SidebarSection>,
    selectedRoute: String?,
    onItemClick: (SidebarItem) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .background(SidebarBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Brand
        Text(
            text = "Playable Craft",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        )
        HorizontalDivider(color = FieldStroke)

        // ── Sections ────────────────────────────────────────────
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            sections.forEachIndexed { index, section ->
                if (index > 0) Spacer(Modifier.height(18.dp))
                Text(
                    text = section.title,
                    color = TextLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
                )
                section.items.forEach { item ->
                    SidebarItemRow(
                        item = item,
                        selected = item.route == selectedRoute,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }

        // ── Footer: user + logout ───────────────────────────────
        HorizontalDivider(color = FieldStroke)
        Column(Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
            if (user != null) {
                Column(Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Text(
                        user.name,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        user.role.lowercase().replaceFirstChar { it.uppercase() },
                        color = TextLabel,
                        fontSize = 12.sp
                    )
                }
            }
            FooterRow(label = "Log out", onClick = onLogout)
        }
    }
}

@Composable
private fun SidebarItemRow(
    item: SidebarItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()   // visible focus ring for TV remote / D-pad

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(shape)
            .background(if (selected) SelectedBg else Color.Transparent)
            .border(
                1.dp,
                when {
                    focused -> TextPrimary
                    selected -> SelectedBorder
                    else -> Color.Transparent
                },
                shape
            )
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp)
    ) {
        Icon(
            item.icon,
            contentDescription = null,
            tint = if (selected) TextPrimary else TextLabel,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            item.label,
            color = if (selected) TextPrimary else TextSecondary,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FooterRow(label: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, if (focused) TextPrimary else Color.Transparent, shape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp)
    ) {
        Icon(
            Icons.AutoMirrored.Outlined.Logout,
            contentDescription = null,
            tint = TextLabel,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(label, color = TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

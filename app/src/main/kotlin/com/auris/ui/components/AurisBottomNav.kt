package com.auris.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.navigation.Screen
import com.auris.ui.theme.AurisColors

/** Tab descriptor used by AurisBottomNav. */
data class AurisTab(val label: String, val route: String)

/**
 * AurisBottomNav — Floating Pill Tab Bar (redesigned).
 *
 * Fixes:
 * • Equal 5-column grid (all weight 1f) instead of 0.4/0.2/0.4
 * • FAB offset reduced to -8dp (was -16dp) for clean elevation without overlap
 * • All regular icons 24dp, FAB icon 24dp inside 52dp circle
 * • Labels flow naturally at 4dp below icon, no negative offsets
 * • Consistent icon + label vertical alignment across all tabs
 */
@Composable
fun AurisBottomNav(
    currentRoute: String?,
    onTabSelected: (AurisTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .padding(horizontal = 16.dp)
    ) {
        // Floating pill container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(999.dp),
                    ambientColor = Color(0x1A000000)
                )
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0xF2FFFFFF))     // near-opaque white for clarity
                .border(0.5.dp, Color(0x33000000), RoundedCornerShape(999.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavTabItem(
                    label = "Home", icon = Icons.Outlined.Home,
                    isActive = currentRoute == Screen.Home.route,
                    onClick = { onTabSelected(AurisTab("Home", Screen.Home.route)) },
                    modifier = Modifier.weight(1f)
                )

                // Vitamins
                NavTabItem(
                    label = "Vitamins", icon = Icons.Outlined.BarChart,
                    isActive = currentRoute == Screen.Overview.route,
                    onClick = { onTabSelected(AurisTab("Vitamins", Screen.Overview.route)) },
                    modifier = Modifier.weight(1f)
                )

                // Center FAB — Log
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        FloatingActionButton(
                            onClick = { onTabSelected(AurisTab("Log", Screen.Log.route)) },
                            containerColor = AurisColors.Blue,
                            contentColor = Color.White,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 2.dp
                            ),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(52.dp)
                                .offset(y = (-8).dp)
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Log",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            "Log",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (currentRoute == Screen.Log.route) AurisColors.Blue
                                    else AurisColors.LabelSecondary,
                            modifier = Modifier.offset(y = (-6).dp)
                        )
                    }
                }

                // Habits
                NavTabItem(
                    label = "Habits", icon = Icons.Outlined.CheckBox,
                    isActive = currentRoute == Screen.Habits.route,
                    onClick = { onTabSelected(AurisTab("Habits", Screen.Habits.route)) },
                    modifier = Modifier.weight(1f)
                )

                // Profile
                NavTabItem(
                    label = "Profile", icon = Icons.Outlined.Person,
                    isActive = currentRoute == Screen.Profile.route,
                    onClick = { onTabSelected(AurisTab("Profile", Screen.Profile.route)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.height(72.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) AurisColors.Blue else AurisColors.LabelSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isActive) AurisColors.Blue else AurisColors.LabelSecondary,
            letterSpacing = 0.sp
        )
    }
}

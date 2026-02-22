package com.auris.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.ui.theme.AurisColors

data class Habit(
    val id: Int,
    val title: String,
    val metric: String,
    val icon: ImageVector,
    val color: Color,
    val bg: Color,
    val category: String,
    val done: Boolean,
    val streak: Int
)

/**
 * HabitItem — iOS grouped list cell (synced to JSX HabitItem).
 *
 * • No standalone border/clip — lives inside a grouped white card
 * • 36dp icon square, rounded-[10dp]
 * • 17sp normal weight title (line-through when done)
 * • 24dp check circle, 1.5dp border
 * • bg-white normal / bg-[#F2F2F7]/50 done
 * • Simple active:scale(0.96) feel — no spring animation
 */
@Composable
fun HabitItem(
    habit: Habit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(if (habit.done) Color(0x80F2F2F7) else Color.White)
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icon square — 36dp, rounded-[10dp]
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(habit.color.copy(alpha = 0.12f))
        ) {
            Icon(habit.icon, contentDescription = habit.title,
                tint = habit.color,
                modifier = Modifier.size(20.dp))
        }

        // Title + metric + streak
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text           = habit.title,
                fontSize       = 17.sp,
                fontWeight     = FontWeight.Normal,   // JSX: font-normal
                color          = if (habit.done) AurisColors.LabelSecondary else AurisColors.LabelPrimary,
                textDecoration = if (habit.done) TextDecoration.LineThrough else TextDecoration.None,
                letterSpacing  = (-0.2).sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(habit.metric, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    color = AurisColors.LabelSecondary, letterSpacing = 0.1.sp)
                if (habit.streak > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(AurisColors.Orange.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Icon(Icons.Filled.LocalFireDepartment, contentDescription = null,
                            tint = AurisColors.Orange, modifier = Modifier.size(10.dp))
                        Text("${habit.streak}", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                            color = AurisColors.Orange)
                    }
                }
            }
        }

        // Check circle — 24dp, 1.5dp border
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(if (habit.done) AurisColors.Green else Color.Transparent)
                .then(
                    if (!habit.done) Modifier.border(1.5.dp, Color(0x4D3C3C43), CircleShape)
                    else Modifier
                )
        ) {
            if (habit.done) {
                Icon(Icons.Filled.Check, contentDescription = "Done",
                    tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
    }
}

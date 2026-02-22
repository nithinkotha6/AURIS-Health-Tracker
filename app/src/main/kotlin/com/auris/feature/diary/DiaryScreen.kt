package com.auris.feature.diary

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.ui.components.*
import com.auris.ui.theme.AurisColors

/* ─────────────────────────────────────────────────────────
   HABITS SCREEN — synced to auris-hig-final.jsx HabitsView
   1. Large title + date
   2. Calendar strip (white chips, active = blue)
   3. Daily Goal card (solid white, no orb)
   4. Grouped habit rows inside white cards with inset separators
*/
@Composable
fun DiaryScreen() {
    val calendarDays = listOf(
        Triple("M", 21, false),
        Triple("T", 22, false),
        Triple("W", 23, false),
        Triple("T", 24, true),
        Triple("F", 25, false),
        Triple("S", 26, false),
    )

    val initialHabits = listOf(
        Habit(1, "Drink Water",  "2 / 3 Liters", Icons.Outlined.WaterDrop,  AurisColors.Blue,   AurisColors.Blue.copy(alpha=0.12f),   "Morning", true,  12),
        Habit(2, "Morning Run",  "5 km",          Icons.Outlined.FitnessCenter, AurisColors.Green, AurisColors.Green.copy(alpha=0.12f), "Morning", false, 4),
        Habit(3, "Read Book",    "20 Pages",      Icons.Outlined.MenuBook,    AurisColors.Purple, AurisColors.Purple.copy(alpha=0.12f),"Evening", true,  21),
        Habit(4, "No Sugar",     "All day",       Icons.Outlined.Coffee,      AurisColors.Orange, AurisColors.Orange.copy(alpha=0.12f),"All Day", false, 1),
    )
    var habits by remember { mutableStateOf(initialHabits) }

    val completed    = habits.count { it.done }
    val progressPct  = if (habits.isEmpty()) 0 else (completed * 100 / habits.size)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AurisColors.BgPrimary)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)    // px-6
            .padding(bottom = 130.dp)
            .padding(top = 40.dp)           // pt-10
    ) {
        // ── Header ───────────────────────────────────────────────
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
            modifier              = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, bottom = 20.dp)
        ) {
            Column {
                Text("Habits", fontSize = 34.sp, fontWeight = FontWeight.Bold,
                    color = AurisColors.LabelPrimary, letterSpacing = (-0.5).sp)
                Text("October 24, Thursday", fontSize = 15.sp,
                    color = AurisColors.LabelSecondary, fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 0.dp))
            }
            // Header icon — circular bg per JSX
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0x1F767680))   // bg-[#7676801F]
            ) {
                Icon(Icons.Outlined.CheckBox, contentDescription = null,
                    tint = AurisColors.Blue, modifier = Modifier.size(18.dp))
            }
        }

        // ── Calendar strip (JSX: white chips, active = blue) ──────
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            calendarDays.forEach { (day, num, isActive) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .width(44.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .then(
                            if (isActive) Modifier.background(AurisColors.Blue)
                            else Modifier
                                .shadow(2.dp, RoundedCornerShape(14.dp), ambientColor = Color(0x0D000000))
                                .background(Color.White)
                        )
                ) {
                    Text(day, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                        color = if (isActive) Color.White.copy(0.80f) else AurisColors.LabelSecondary,
                        modifier = Modifier.padding(bottom = 4.dp))
                    Text("$num", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                        color = if (isActive) Color.White else AurisColors.LabelPrimary)
                }
            }
        }

        // ── Daily Goal card (solid white, no orb) ────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(20.dp), ambientColor = Color(0x0D000000))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("Your Daily Goal", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                        color = AurisColors.LabelPrimary, letterSpacing = (-0.2).sp)
                    Spacer(Modifier.height(2.dp))
                    Text("Almost there! Keep it up.",
                        fontSize = 15.sp, fontWeight = FontWeight.Normal,
                        color = AurisColors.LabelSecondary)
                }
                RingWidget(pct = progressPct, color = AurisColors.Green, size = 60.dp, strokeWidth = 8.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("$completed", fontSize = 15.sp, fontWeight = FontWeight.Bold,
                            color = AurisColors.LabelPrimary)
                        Text("/${habits.size}", fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = AurisColors.LabelSecondary)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Morning habits (grouped white card) ─────────────────
        HabitGroupedSection(
            title   = "Morning",
            habits  = habits.filter { it.category == "Morning" },
            onToggle = { id -> habits = habits.map { if (it.id == id) it.copy(done = !it.done) else it } }
        )

        Spacer(Modifier.height(24.dp))

        // ── Evening & All Day (grouped white card) ──────────────
        HabitGroupedSection(
            title   = "Evening & All Day",
            habits  = habits.filter { it.category != "Morning" },
            onToggle = { id -> habits = habits.map { if (it.id == id) it.copy(done = !it.done) else it } }
        )
    }
}

/**
 * Grouped habit section: section header + white card containing all habits
 * with inset separators (matching JSX grouped list pattern).
 */
@Composable
private fun HabitGroupedSection(
    title: String,
    habits: List<Habit>,
    onToggle: (Int) -> Unit
) {
    // Section header — 15sp semibold, secondary color, normal case
    Text(
        text       = title,
        fontSize   = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color      = AurisColors.LabelSecondary,
        letterSpacing = (-0.2).sp,
        modifier   = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )

    // Grouped white card containing all habit rows
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp), ambientColor = Color(0x0D000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {
        Column {
            habits.forEachIndexed { idx, habit ->
                HabitItem(habit = habit, onToggle = { onToggle(habit.id) })
                // Inset separator (60dp left inset to clear icon)
                if (idx < habits.lastIndex) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .padding(start = 60.dp)
                            .background(AurisColors.Separator)
                    )
                }
            }
        }
    }
}

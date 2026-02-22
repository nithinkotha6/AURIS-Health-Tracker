package com.auris.feature.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.auris.domain.model.Habit
import com.auris.domain.model.HabitRecurrence
import com.auris.ui.components.GlassCard
import com.auris.ui.theme.AurisColors

/** HabitScreen — list habits, add, complete (Phase 11). */
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AurisColors.BgPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 120.dp)
        ) {
            Text(
                text = "Habits",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = AurisColors.LabelPrimary
            )
            Text(
                text = "Track daily habits and streaks",
                fontSize = 14.sp,
                color = AurisColors.LabelSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(habits, key = { it.habitId }) { habit ->
                    HabitRow(
                        habit = habit,
                        onComplete = { viewModel.completeHabit(habit.habitId) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.addHabit("New habit", HabitRecurrence.DAILY) },
            containerColor = AurisColors.Blue,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add habit")
        }
    }
}

@Composable
private fun HabitRow(
    habit: Habit,
    onComplete: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onComplete() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = AurisColors.LabelPrimary
                )
                Text(
                    text = "${habit.streakCurrent} day streak · best ${habit.streakBest}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AurisColors.LabelSecondary
                )
            }
            Text(
                text = habit.recurrence.name,
                style = MaterialTheme.typography.labelSmall,
                color = AurisColors.LabelTertiary
            )
        }
    }
}

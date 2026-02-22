package com.auris.feature.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.model.Habit
import com.auris.domain.model.HabitRecurrence
import com.auris.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    val habits: StateFlow<List<Habit>> = habitRepository.getAllHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addHabit(name: String, recurrence: HabitRecurrence = HabitRecurrence.DAILY) {
        viewModelScope.launch {
            habitRepository.addHabit(
                Habit(
                    habitId = UUID.randomUUID().toString(),
                    name = name,
                    iconName = "check_circle",
                    colorHex = "#007AFF",
                    recurrence = recurrence,
                    reminderMinutes = null,
                    streakCurrent = 0,
                    streakBest = 0
                )
            )
        }
    }

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habitId)
        }
    }

    fun completeHabit(habitId: String) {
        viewModelScope.launch {
            habitRepository.completeHabit(habitId, java.time.LocalDate.now())
        }
    }
}

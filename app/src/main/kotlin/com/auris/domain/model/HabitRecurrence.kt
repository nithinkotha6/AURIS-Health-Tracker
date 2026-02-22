package com.auris.domain.model

/**
 * HabitRecurrence — when a habit is due (Phase 11).
 */
enum class HabitRecurrence {
    DAILY,      // Every day
    WEEKDAYS,   // Mon–Fri
    WEEKENDS,   // Sat–Sun
    CUSTOM      // Custom days (stored separately if needed)
}

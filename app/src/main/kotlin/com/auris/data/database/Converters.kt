package com.auris.data.database

import androidx.room.TypeConverter
import com.auris.domain.model.DeficiencyLevel
import com.auris.domain.model.HabitRecurrence
import com.auris.domain.model.MealType
import com.auris.domain.model.NutrientId
import java.time.Instant
import java.time.LocalDate

/**
 * Converters — Room type converters for domain enums and java.time types.
 */
class Converters {

    // ── LocalDate ↔ String (ISO-8601: "2026-02-21") ─────────────────
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // ── Instant ↔ Long (epoch millis) ────────────────────────────────
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    // ── MealType ↔ String ────────────────────────────────────────────
    @TypeConverter
    fun fromMealType(type: MealType?): String? = type?.name

    @TypeConverter
    fun toMealType(value: String?): MealType? = value?.let { MealType.valueOf(it) }

    // ── NutrientId ↔ String ──────────────────────────────────────────
    @TypeConverter
    fun fromNutrientId(id: NutrientId?): String? = id?.name

    @TypeConverter
    fun toNutrientId(value: String?): NutrientId? = value?.let { NutrientId.valueOf(it) }

    // ── DeficiencyLevel ↔ String ─────────────────────────────────────
    @TypeConverter
    fun fromDeficiencyLevel(level: DeficiencyLevel?): String? = level?.name

    @TypeConverter
    fun toDeficiencyLevel(value: String?): DeficiencyLevel? = value?.let { DeficiencyLevel.valueOf(it) }

    // ── HabitRecurrence ↔ String ─────────────────────────────────────
    @TypeConverter
    fun fromHabitRecurrence(r: HabitRecurrence?): String? = r?.name

    @TypeConverter
    fun toHabitRecurrence(value: String?): HabitRecurrence? = value?.let { HabitRecurrence.valueOf(it) }
}

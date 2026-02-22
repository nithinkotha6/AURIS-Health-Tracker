package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.auris.domain.model.MealType
import com.auris.domain.model.ParsedFoodItem
import java.time.Instant
import java.time.LocalDate

/**
 * FoodEntryEntity — Room entity for persisted food log entries.
 *
 * Maps to/from domain [ParsedFoodItem].
 * Indexed by date for efficient daily queries.
 */
@Entity(
    tableName = "food_entries",
    indices = [Index(value = ["date"])]
)
data class FoodEntryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    val entryId: Long = 0,

    @ColumnInfo(name = "food_name")
    val foodName: String,

    @ColumnInfo(name = "meal_type")
    val mealType: MealType,

    @ColumnInfo(name = "calories_kcal")
    val caloriesKcal: Float,

    @ColumnInfo(name = "protein_g")
    val proteinG: Float,

    @ColumnInfo(name = "carbs_g")
    val carbsG: Float,

    @ColumnInfo(name = "fat_g")
    val fatG: Float,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "logged_at")
    val loggedAt: Instant = Instant.now(),

    @ColumnInfo(name = "source")
    val source: String = "manual",

    @ColumnInfo(name = "confirmed_by_user")
    val confirmedByUser: Boolean = true,

    @ColumnInfo(name = "source_note")
    val sourceNote: String = "",

    @ColumnInfo(name = "requires_confirmation")
    val requiresConfirmation: Boolean = false
) {
    /** Convert to domain model */
    fun toDomain(): ParsedFoodItem = ParsedFoodItem(
        name                 = foodName,
        calories             = caloriesKcal,
        proteinG             = proteinG,
        carbsG               = carbsG,
        fatG                 = fatG,
        mealType             = mealType,
        requiresConfirmation = requiresConfirmation,
        sourceNote           = sourceNote
    )

    companion object {
        /** Convert from domain model */
        fun fromDomain(item: ParsedFoodItem, date: LocalDate = LocalDate.now()): FoodEntryEntity =
            FoodEntryEntity(
                foodName             = item.name,
                mealType             = item.mealType,
                caloriesKcal         = item.calories,
                proteinG             = item.proteinG,
                carbsG               = item.carbsG,
                fatG                 = item.fatG,
                date                 = date,
                sourceNote           = item.sourceNote,
                requiresConfirmation = item.requiresConfirmation
            )
    }
}

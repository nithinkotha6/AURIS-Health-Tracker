package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.auris.domain.model.NutrientId

/**
 * NutritionReferenceEntity — static reference table for nutrient RDA values.
 *
 * Seeded on first DB creation via [NutritionReferenceSeed].
 * Read-only after initial population.
 */
@Entity(tableName = "nutrition_reference")
data class NutritionReferenceEntity(
    @PrimaryKey
    @ColumnInfo(name = "nutrient_id")
    val nutrientId: NutrientId,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "short_name")
    val shortName: String,

    @ColumnInfo(name = "unit")
    val unit: String,

    @ColumnInfo(name = "rda_male")
    val rdaMale: Float,

    @ColumnInfo(name = "rda_female")
    val rdaFemale: Float,

    @ColumnInfo(name = "upper_limit")
    val upperLimit: Float? = null
)

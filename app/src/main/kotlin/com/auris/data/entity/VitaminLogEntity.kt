package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.auris.domain.model.DeficiencyLevel
import com.auris.domain.model.NutrientId
import com.auris.domain.model.VitaminStatus
import java.time.LocalDate

/**
 * VitaminLogEntity — Room entity for daily vitamin status snapshots.
 *
 * One row per nutrient per day. Upserted when food log changes.
 * Composite unique constraint on (date, nutrient_id).
 */
@Entity(
    tableName = "vitamin_logs",
    indices = [Index(value = ["date", "nutrient_id"], unique = true)]
)
data class VitaminLogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    val logId: Long = 0,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "nutrient_id")
    val nutrientId: NutrientId,

    @ColumnInfo(name = "raw_intake")
    val rawIntake: Float,

    @ColumnInfo(name = "effective_intake")
    val effectiveIntake: Float,

    @ColumnInfo(name = "adjusted_rda")
    val adjustedRda: Float,

    @ColumnInfo(name = "percent_complete")
    val percentComplete: Int,

    @ColumnInfo(name = "deficiency_level")
    val deficiencyLevel: DeficiencyLevel,

    @ColumnInfo(name = "display_value")
    val displayValue: String,

    @ColumnInfo(name = "source_hint")
    val sourceHint: String = ""
) {
    /** Convert to domain model */
    fun toDomain(): VitaminStatus = VitaminStatus(
        nutrientId      = nutrientId,
        rawIntake       = rawIntake,
        effectiveIntake = effectiveIntake,
        adjustedRda     = adjustedRda,
        deficiencyLevel = deficiencyLevel,
        displayValue    = displayValue,
        sourceHint      = sourceHint
    )

    companion object {
        /** Convert from domain model */
        fun fromDomain(status: VitaminStatus, date: LocalDate = LocalDate.now()): VitaminLogEntity {
            val fraction = status.percentFraction
            return VitaminLogEntity(
                date            = date,
                nutrientId      = status.nutrientId,
                rawIntake       = status.rawIntake,
                effectiveIntake = status.effectiveIntake,
                adjustedRda     = status.adjustedRda,
                percentComplete = (fraction * 100).toInt(),
                deficiencyLevel = status.deficiencyLevel,
                displayValue    = status.displayValue,
                sourceHint      = status.sourceHint
            )
        }
    }
}

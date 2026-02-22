package com.auris.data.database.seed

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.auris.data.entity.NutritionReferenceEntity
import com.auris.domain.model.NutrientId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

/**
 * NutritionReferenceSeed — RoomDatabase.Callback that seeds the
 * nutrition_reference table with all 19 nutrient RDA values on first creation.
 */
class NutritionReferenceSeed(
    private val daoProvider: Provider<com.auris.data.dao.NutritionReferenceDao>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val dao = daoProvider.get()
            if (dao.count() == 0) {
                dao.insertAll(createSeedData())
            }
        }
    }

    private fun createSeedData(): List<NutritionReferenceEntity> =
        NutrientId.entries.map { nutrient ->
            NutritionReferenceEntity(
                nutrientId = nutrient,
                name       = nutrient.displayName,
                shortName  = nutrient.shortName,
                unit       = nutrient.unit,
                rdaMale    = nutrient.rdaMale,
                rdaFemale  = nutrient.rdaFemale,
                upperLimit = nutrient.upperLimit
            )
        }
}

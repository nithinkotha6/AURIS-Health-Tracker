package com.auris.data.repository

import com.auris.domain.model.NutrientId
import com.auris.domain.model.VitaminStatus
import com.auris.domain.repository.FoodRepository
import com.auris.domain.repository.VitaminRepository
import com.auris.domain.usecase.ApplyBurnAdjustmentsUseCase
import com.auris.domain.usecase.CalcVitaminStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FakeVitaminRepositoryImpl — in-memory implementation of [VitaminRepository].
 *
 * Phase 5: recalculates vitamin status reactively when the food log changes.
 * Phase 6: replaced by [RoomVitaminRepositoryImpl].
 *
 * Observes [FoodRepository] for changes and runs [CalcVitaminStatusUseCase]
 * to keep vitamin status in sync with the food log.
 */
@Singleton
class FakeVitaminRepositoryImpl @Inject constructor(
    private val foodRepository: FoodRepository,
    private val calcVitaminStatus: CalcVitaminStatusUseCase
) : VitaminRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _todayStatus = MutableStateFlow(dummyVitaminSeed())

    init {
        // Observe food log changes and recalculate automatically
        scope.launch {
            foodRepository.getTodayFoodLog().collect { foods ->
                _todayStatus.value = calcVitaminStatus(foods)
            }
        }
    }

    override fun getTodayVitaminStatus(): Flow<List<VitaminStatus>> =
        _todayStatus.asStateFlow()

    override suspend fun recalculate(
        burn: ApplyBurnAdjustmentsUseCase.BurnData?
    ) {
        // FakeImpl: burn data is ignored — always recalculates from food log only
        val foods = foodRepository.getTodayFoodLog().first()
        _todayStatus.value = calcVitaminStatus(foods)
    }

    override fun getVitaminStatusByDate(date: LocalDate): Flow<List<VitaminStatus>> {
        // In the fake implementation, only today's data is available
        return foodRepository.getFoodLogByDate(date).map { foods ->
            calcVitaminStatus(foods)
        }
    }
}

/** Pre-seeded dummy vitamin values for the demo/preview state. */
private fun dummyVitaminSeed(): List<VitaminStatus> = listOf(
    VitaminStatus.fromPercent(NutrientId.VIT_A,     72,  "648 mcg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B1,    55,  "0.66 mg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B2,    88,  "1.14 mg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B3,    61,  "9.8 mg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B5,    80,  "4.0 mg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B6,    46,  "0.60 mg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B7,    33,  "10 mcg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B9,    58,  "232 mcg"),
    VitaminStatus.fromPercent(NutrientId.VIT_B12,   91,  "2.2 mcg"),
    VitaminStatus.fromPercent(NutrientId.VIT_C,     44,  "40 mg"),
    VitaminStatus.fromPercent(NutrientId.VIT_D,     27,  "4 mcg"),
    VitaminStatus.fromPercent(NutrientId.VIT_E,     68,  "10.2 mg"),
    VitaminStatus.fromPercent(NutrientId.VIT_K,     53,  "64 mcg"),
    VitaminStatus.fromPercent(NutrientId.IRON,      75,  "6 mg"),
    VitaminStatus.fromPercent(NutrientId.CALCIUM,   49,  "490 mg"),
    VitaminStatus.fromPercent(NutrientId.MAGNESIUM, 60,  "252 mg"),
    VitaminStatus.fromPercent(NutrientId.ZINC,      82,  "9 mg"),
    VitaminStatus.fromPercent(NutrientId.PROTEIN,   71,  "40 g"),
    VitaminStatus.fromPercent(NutrientId.COLLAGEN,  38,  "950 mg"),
)

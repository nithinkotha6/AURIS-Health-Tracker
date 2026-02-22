package com.auris.data.repository

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

    private val _todayStatus = MutableStateFlow<List<VitaminStatus>>(
        calcVitaminStatus(emptyList()) // initial: all zeros
    )

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

    override suspend fun recalculate(burn: ApplyBurnAdjustmentsUseCase.BurnData?) {
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

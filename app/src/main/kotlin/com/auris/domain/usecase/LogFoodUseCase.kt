package com.auris.domain.usecase

import com.auris.domain.model.ParsedFoodItem
import com.auris.domain.repository.FoodRepository
import com.auris.domain.repository.VitaminRepository
import javax.inject.Inject

/**
 * LogFoodUseCase — orchestrates food logging transaction.
 *
 * 1. Validates the input
 * 2. Persists via FoodRepository
 * 3. Triggers vitamin recalculation
 */
class LogFoodUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
    private val vitaminRepository: VitaminRepository
) {

    /**
     * @param item The food item to log
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(item: ParsedFoodItem): Result<Unit> = runCatching {
        require(item.name.isNotBlank()) { "Food name must not be blank" }
        foodRepository.logFood(item)
        // Recalculate without burn adjustments; Health Connect sync (Phase 10)
        // can refine RDAs via SyncHealthConnectUseCase.
        vitaminRepository.recalculate()
    }
}

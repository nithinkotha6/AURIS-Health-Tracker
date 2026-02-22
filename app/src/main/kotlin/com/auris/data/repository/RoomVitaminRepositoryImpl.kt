package com.auris.data.repository

import com.auris.data.dao.FoodEntryDao
import com.auris.data.dao.VitaminLogDao
import com.auris.data.entity.VitaminLogEntity
import com.auris.domain.model.VitaminStatus
import com.auris.domain.repository.VitaminRepository
import com.auris.domain.usecase.ApplyBurnAdjustmentsUseCase
import com.auris.domain.usecase.CalcVitaminStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * RoomVitaminRepositoryImpl — Room-backed implementation of [VitaminRepository].
 *
 * Phase 6: replaces [FakeVitaminRepositoryImpl].
 * Observes food log changes from [FoodEntryDao] and recalculates vitamin
 * status, persisting results to [VitaminLogDao].
 * Phase 10: optionally applies burn adjustments using Health Connect data.
 */
@Singleton
class RoomVitaminRepositoryImpl @Inject constructor(
    private val vitaminLogDao: VitaminLogDao,
    private val foodEntryDao: FoodEntryDao,
    private val calcVitaminStatus: CalcVitaminStatusUseCase,
    private val applyBurnAdjustments: ApplyBurnAdjustmentsUseCase
) : VitaminRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        // Auto-recalculate when food log changes (without burn data; Health
        // Connect sync can refine via explicit recalculate(burn) calls).
        scope.launch {
            val today = LocalDate.now()
            foodEntryDao.getByDate(today).collect { entities ->
                val foods = entities.map { it.toDomain() }
                val statuses = calcVitaminStatus(foods)
                val logEntities = statuses.map { VitaminLogEntity.fromDomain(it, today) }
                vitaminLogDao.deleteByDate(today)
                vitaminLogDao.upsertAll(logEntities)
            }
        }
    }

    override fun getTodayVitaminStatus(): Flow<List<VitaminStatus>> {
        val today = LocalDate.now()
        return vitaminLogDao.getByDate(today).map { entities ->
            if (entities.isEmpty()) {
                // Return zero-state for all nutrients when no data logged yet
                calcVitaminStatus(emptyList())
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun recalculate(
        burn: ApplyBurnAdjustmentsUseCase.BurnData?
    ) {
        val today = LocalDate.now()
        val foods = foodEntryDao.getByDateOnce(today).map { it.toDomain() }

        // Base vitamin status from food log
        val baseStatuses = calcVitaminStatus(foods)

        // Optionally apply burn adjustments from Health Connect
        val finalStatuses = if (burn != null) {
            applyBurnAdjustments(baseStatuses, burn)
        } else {
            baseStatuses
        }

        val logEntities = finalStatuses.map { VitaminLogEntity.fromDomain(it, today) }
        vitaminLogDao.deleteByDate(today)
        vitaminLogDao.upsertAll(logEntities)
    }

    override fun getVitaminStatusByDate(date: LocalDate): Flow<List<VitaminStatus>> {
        return vitaminLogDao.getByDate(date).map { entities ->
            if (entities.isEmpty()) {
                calcVitaminStatus(emptyList())
            } else {
                entities.map { it.toDomain() }
            }
        }
    }
}


package com.auris.domain.repository

import com.auris.domain.usecase.ApplyBurnAdjustmentsUseCase

/**
 * HealthConnectRepository — abstraction over Android Health Connect.
 *
 * Phase 10:
 * - Reads today's steps, active calories burned, and last night's sleep.
 * - Exposes a compact [ApplyBurnAdjustmentsUseCase.BurnData] payload that can
 *   be consumed by domain use cases to adjust nutrient RDAs.
 */
interface HealthConnectRepository {

    /**
     * Reads today's activity/sleep metrics from Health Connect.
     *
     * @return [ApplyBurnAdjustmentsUseCase.BurnData] when Health Connect is
     *         available and permissions are granted; `null` otherwise.
     */
    suspend fun readTodayBurnData(): ApplyBurnAdjustmentsUseCase.BurnData?

    /**
     * @return true when Health Connect is available on this device and all
     *         required permissions have been granted; false otherwise.
     */
    suspend fun hasAllPermissions(): Boolean
}



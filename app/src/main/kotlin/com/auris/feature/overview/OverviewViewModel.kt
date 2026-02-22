package com.auris.feature.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.data.dao.VitaminLogDao
import com.auris.domain.model.DeficiencyLevel
import com.auris.domain.model.NutrientId
import com.auris.domain.model.NutrientTrendSeries
import com.auris.ui.theme.AurisColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/** Stats summary for Overview screen (Phase 11). */
data class OverviewStats(
    val weeklyAvgProteinPct: Float = 0f,
    val bestDay: LocalDate? = null,
    val worstDay: LocalDate? = null,
    val daysWithData: Int = 0
)

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val vitaminLogDao: VitaminLogDao
) : ViewModel() {

    private val _trendSeries = MutableStateFlow<List<NutrientTrendSeries>>(emptyList())
    val trendSeries: StateFlow<List<NutrientTrendSeries>> = _trendSeries.asStateFlow()

    private val _stats = MutableStateFlow(OverviewStats())
    val stats: StateFlow<OverviewStats> = _stats.asStateFlow()

    init {
        viewModelScope.launch { loadTrends() }
    }

    fun refresh() {
        viewModelScope.launch { loadTrends() }
    }

    private suspend fun loadTrends() {
        val today = LocalDate.now()
        val fromDate = today.minusDays(6)
        val entities = vitaminLogDao.getFromDateOnce(fromDate)

        // Build 7-day series per nutrient (oldest to newest)
        val dates = (0..6).map { today.minusDays(6L - it) }
        val byNutrient = entities.groupBy { it.nutrientId }

        val series = NutrientId.entries.map { nutrientId ->
            val perDay = dates.map { date ->
                byNutrient[nutrientId]?.find { it.date == date }?.percentComplete?.toFloat() ?: 0f
            }
            val latestPct = perDay.lastOrNull() ?: 0f
            val level = DeficiencyLevel.of(latestPct / 100f)
            NutrientTrendSeries(
                nutrientId = nutrientId,
                name = nutrientId.displayName,
                color = level.color,
                deficiencyLevel = level,
                percentByDay = perDay
            )
        }
        _trendSeries.value = series

        // Simple stats: use protein for weekly avg and best/worst day
        val proteinSeries = series.find { it.nutrientId == NutrientId.PROTEIN }
        val (avgPct, bestDay, worstDay) = if (proteinSeries != null && proteinSeries.percentByDay.any { it > 0 }) {
            val indexed = dates.zip(proteinSeries.percentByDay).filter { it.second > 0 }
            val avg = indexed.map { it.second }.average().toFloat()
            val best = indexed.maxByOrNull { it.second }?.first
            val worst = indexed.minByOrNull { it.second }?.first
            Triple(avg, best, worst)
        } else Triple(0f, null, null)
        _stats.value = OverviewStats(
            weeklyAvgProteinPct = avgPct,
            bestDay = bestDay,
            worstDay = worstDay,
            daysWithData = entities.map { it.date }.distinct().size
        )
    }
}

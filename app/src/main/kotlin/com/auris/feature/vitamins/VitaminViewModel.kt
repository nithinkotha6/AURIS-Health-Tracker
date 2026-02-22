package com.auris.feature.vitamins

import androidx.lifecycle.ViewModel
import com.auris.domain.model.NutrientId
import com.auris.domain.model.VitaminStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * VitaminViewModel
 * ─────────────────
 * Phase 2-3: hardcoded test data covering all 5 deficiency levels.
 * Phase 5: injects VitaminRepository and collects real flows.
 */
@HiltViewModel
class VitaminViewModel @Inject constructor() : ViewModel() {

    private val _vitamins = MutableStateFlow(createTestData())
    val vitamins: StateFlow<List<VitaminStatus>> = _vitamins.asStateFlow()

    /** Top 4 nutrients by lowest percentFraction — used on Home screen */
    val topFour: StateFlow<List<VitaminStatus>> = MutableStateFlow(
        createTestData().sortedBy { it.percentFraction }.take(4)
    )

    private fun createTestData(): List<VitaminStatus> = listOf(
        VitaminStatus.fromPercent(NutrientId.VIT_D,     12, "1.8 mcg",    "• Critical"),
        VitaminStatus.fromPercent(NutrientId.VIT_B12,   22, "0.53 mcg",   "• Deficient"),
        VitaminStatus.fromPercent(NutrientId.IRON,      30, "2.4 mg",     "• Deficient"),
        VitaminStatus.fromPercent(NutrientId.VIT_K,     38, "45.6 mcg",   "• Deficient"),
        VitaminStatus.fromPercent(NutrientId.VIT_A,     45, "405 mcg",    "• Low"),
        VitaminStatus.fromPercent(NutrientId.MAGNESIUM, 52, "218 mg",     "• Low"),
        VitaminStatus.fromPercent(NutrientId.CALCIUM,   55, "550 mg",     "• Low"),
        VitaminStatus.fromPercent(NutrientId.VIT_B9,    58, "232 mcg",    "• Low"),
        VitaminStatus.fromPercent(NutrientId.ZINC,      62, "5.0 mg",     "• Adequate"),
        VitaminStatus.fromPercent(NutrientId.VIT_B6,    65, "0.85 mg",    "• Adequate"),
        VitaminStatus.fromPercent(NutrientId.VIT_B1,    68, "0.82 mg",    "• Adequate"),
        VitaminStatus.fromPercent(NutrientId.VIT_B2,    70, "0.91 mg",    "• Adequate"),
        VitaminStatus.fromPercent(NutrientId.VIT_B3,    72, "11.5 mg",    "• Adequate"),
        VitaminStatus.fromPercent(NutrientId.VIT_C,     78, "70.2 mg",    "• Adequate"),
        VitaminStatus.fromPercent(NutrientId.VIT_B5,    80, "4.0 mg",     "• Optimal"),
        VitaminStatus.fromPercent(NutrientId.VIT_E,     82, "12.3 mg",    "• Optimal"),
        VitaminStatus.fromPercent(NutrientId.VIT_B7,    85, "25.5 mcg",   "• Optimal"),
        VitaminStatus.fromPercent(NutrientId.PROTEIN,   88, "49.3 g",     "• Optimal"),
        VitaminStatus.fromPercent(NutrientId.COLLAGEN,  91, "2275 mg",    "• Optimal")
    )
}

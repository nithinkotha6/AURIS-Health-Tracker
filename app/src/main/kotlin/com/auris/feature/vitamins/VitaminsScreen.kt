package com.auris.feature.vitamins

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.auris.domain.model.NutrientId
import com.auris.domain.model.VitaminStatus
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import com.auris.ui.components.VitaminBarRow
import com.auris.ui.theme.AurisColors

/* ─────────────────────────────────────────────────────────
   VITAMINS SCREEN — HIG StatsView (grouped list)
   White bgSecondary card with 0.5px separators,
   colored icon squares, 4px animated bars, LOW badge.
*/
@Composable
fun VitaminsScreen(
    navController: NavController,
    vm: VitaminViewModel = hiltViewModel()
) {
    val vitamins by vm.vitamins.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AurisColors.BgPrimary)
            .windowInsetsPadding(WindowInsets.statusBars)   // safe area inset
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)     // consistent horizontal padding
            .padding(bottom = 130.dp)        // floating nav clearance
    ) {
        // ── Header ─────────────────────────────────────────────
        Column(modifier = Modifier.padding(start = 4.dp, top = 24.dp, bottom = 20.dp)) {
            Text("Vitamins", fontSize = 34.sp, fontWeight = FontWeight.Bold,
                color = AurisColors.LabelPrimary, letterSpacing = (-0.5).sp)
            Spacer(Modifier.height(8.dp))
            Text("Today's Intake · ${getTodayLabel()}", fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = AurisColors.LabelSecondary, letterSpacing = (-0.1).sp)
        }

        // ── Grouped white list card ─────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AurisColors.BgSecondary)
                .border(1.dp, AurisColors.Separator, RoundedCornerShape(20.dp))
        ) {
            Column {
                vitamins.forEachIndexed { idx, vitamin ->
                    VitaminBarRow(
                        name       = vitamin.nutrientId.displayName,
                        icon       = iconForNutrient(vitamin.nutrientId),
                        iconColor  = colorForNutrient(vitamin.nutrientId),
                        percent    = (vitamin.percentFraction * 100).toInt(),
                        amount     = vitamin.displayValue,
                        unit       = vitamin.nutrientId.unit,
                        isLow      = vitamin.percentFraction < 0.40f,
                        isLast     = idx == vitamins.lastIndex
                    )
                    if (idx < vitamins.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .padding(start = 64.dp)   // inset past icon
                                .background(Color(0x173C3C43))
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

private fun getTodayLabel(): String {
    val cal = java.util.Calendar.getInstance()
    val months = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
    return "${months[cal.get(java.util.Calendar.MONTH)]} ${cal.get(java.util.Calendar.DAY_OF_MONTH)}"
}

/** Maps NutrientId to a Lucide-equivalent Material icon */
fun iconForNutrient(id: NutrientId): ImageVector = when (id) {
    NutrientId.VIT_D    -> Icons.Outlined.WbSunny
    NutrientId.VIT_A    -> Icons.Outlined.Visibility
    NutrientId.VIT_B12  -> Icons.Outlined.Biotech
    NutrientId.VIT_C    -> Icons.Outlined.Eco
    NutrientId.VIT_E    -> Icons.Outlined.Shield
    NutrientId.VIT_B7   -> Icons.Outlined.Science
    NutrientId.IRON     -> Icons.Outlined.WaterDrop
    NutrientId.CALCIUM  -> Icons.Outlined.Grain
    NutrientId.MAGNESIUM-> Icons.Outlined.ElectricBolt
    NutrientId.ZINC     -> Icons.Outlined.Hexagon
    NutrientId.VIT_K    -> Icons.Outlined.Medication
    NutrientId.VIT_B9   -> Icons.Outlined.FavoriteBorder
    NutrientId.VIT_B6   -> Icons.Outlined.Memory
    NutrientId.VIT_B1   -> Icons.Outlined.BatterySaver
    NutrientId.VIT_B2   -> Icons.Outlined.Star
    NutrientId.VIT_B3   -> Icons.Outlined.LocalFireDepartment
    NutrientId.VIT_B5   -> Icons.Outlined.AutoAwesome
    NutrientId.PROTEIN  -> Icons.Outlined.FitnessCenter
    NutrientId.COLLAGEN -> Icons.Outlined.Face
}

/** Semantic icon color per nutrient — matches HIG StatsView */
fun colorForNutrient(id: NutrientId): Color = when (id) {
    NutrientId.VIT_D    -> AurisColors.Orange
    NutrientId.VIT_A    -> Color(0xFFFF6B81)
    NutrientId.VIT_B12  -> AurisColors.Indigo
    NutrientId.VIT_C    -> AurisColors.Green
    NutrientId.VIT_E    -> AurisColors.Blue
    NutrientId.VIT_B7   -> AurisColors.Purple
    NutrientId.IRON     -> AurisColors.Red
    NutrientId.CALCIUM  -> Color(0xFF8E8E93)
    NutrientId.MAGNESIUM-> AurisColors.Orange
    NutrientId.ZINC     -> AurisColors.Indigo
    NutrientId.VIT_K    -> AurisColors.Teal
    NutrientId.VIT_B9   -> AurisColors.Pink
    NutrientId.VIT_B6   -> AurisColors.Purple
    NutrientId.VIT_B1   -> AurisColors.Yellow
    NutrientId.VIT_B2   -> AurisColors.Green2
    NutrientId.VIT_B3   -> AurisColors.Orange
    NutrientId.VIT_B5   -> AurisColors.Blue
    NutrientId.PROTEIN  -> AurisColors.Green
    NutrientId.COLLAGEN -> Color(0xFFFF6B81)
}

fun unitForNutrient(id: NutrientId): String = when (id) {
    NutrientId.VIT_D, NutrientId.VIT_A -> "mcg"
    NutrientId.VIT_C, NutrientId.VIT_E, NutrientId.IRON, NutrientId.MAGNESIUM,
    NutrientId.ZINC,  NutrientId.VIT_K, NutrientId.VIT_B6, NutrientId.VIT_B1,
    NutrientId.VIT_B2,NutrientId.VIT_B3,NutrientId.VIT_B5 -> "mg"
    NutrientId.VIT_B12,NutrientId.VIT_B7,NutrientId.VIT_B9 -> "mcg"
    NutrientId.CALCIUM -> "mg"
    NutrientId.PROTEIN -> "g"
    NutrientId.COLLAGEN -> "mg"
}

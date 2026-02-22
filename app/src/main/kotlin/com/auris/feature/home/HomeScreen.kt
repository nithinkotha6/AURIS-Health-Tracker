package com.auris.feature.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.auris.ui.components.*
import com.auris.ui.theme.AurisColors

/* ─────────────────────────────────────────────────────────
   HOME SCREEN — Apple HIG layout (synced to auris-hig-final.jsx)
   ────────────────────────────────────────────────────────*/
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    syncViewModel: SyncViewModel = hiltViewModel()
) {
    val totalCalories by viewModel.totalCalories.collectAsStateWithLifecycle()
    val totalProtein  by viewModel.totalProtein.collectAsStateWithLifecycle()
    val foodLog       by viewModel.todayFoodLog.collectAsStateWithLifecycle()
    val nudge         by viewModel.nudge.collectAsStateWithLifecycle()
    val burnData      by syncViewModel.lastBurnData.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Trigger Health Connect sync on foreground (Phase 10).
    DisposableEffect(lifecycleOwner, syncViewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                syncViewModel.onForeground()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    var timeFilter by remember { mutableStateOf("Day") }

    // Derive macro display values from real data
    val proteinGoal  = 150
    val carbsGoal    = 280
    val fatGoal      = 65
    val calorieGoal  = 2500
    val burnCalories = (burnData?.activeCalories ?: 0f).toInt()
    val caloriesLeft = (calorieGoal - totalCalories + burnCalories).coerceAtLeast(0)
    val proteinPct   = ((totalProtein / proteinGoal) * 100).toInt().coerceIn(0, 100)
    val totalCarbs   = foodLog.map { it.carbsG }.sum()
    val totalFat     = foodLog.map { it.fatG }.sum()
    val carbsPct     = ((totalCarbs / carbsGoal) * 100).toInt().coerceIn(0, 100)
    val fatPct       = ((totalFat / fatGoal) * 100).toInt().coerceIn(0, 100)
    val caloriePct   = ((totalCalories.toFloat() / calorieGoal) * 100).toInt().coerceIn(0, 100)

    // Derived HC display values (null = HC not connected yet)
    val stepsCount   = burnData?.steps
    val stepsLabel   = stepsCount?.let { "%,d".format(it) } ?: "--"
    val stepsPct     = stepsCount?.let { (it.toFloat() / 10_000 * 100).toInt().coerceIn(0, 100) } ?: 0
    val sleepHours   = burnData?.sleepHours
    val sleepLabel   = sleepHours?.let {
        val h = it.toInt()
        val m = ((it - h) * 60).toInt()
        if (m > 0) "${h}h ${m}m" else "${h}h"
    } ?: "--"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AurisColors.BgPrimary)
            .windowInsetsPadding(WindowInsets.statusBars)   // safe area inset (time + camera)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)     // px-6 = 24dp
            .padding(bottom = 130.dp)        // pb-[130px] in JSX for floating nav clearance
            .padding(top = 24.dp)            // pt-6 = 24dp (matches VitaminsScreen)
    ) {
        // ── 1. Large title ─────────────────────────────────────────
        Text(
            text       = "Hi, Mr. Kotha",
            fontSize   = 34.sp,
            fontWeight = FontWeight.Bold,
            color      = AurisColors.LabelPrimary,
            letterSpacing = (-0.5).sp,
            modifier   = Modifier.padding(bottom = 20.dp)
        )

        // ── 2. Pastel Gradient AI Search Bar ───────────────────────
        // Outer: 2px pastel gradient border pill, inner: white/70% blurred pill
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(999.dp), ambientColor = Color(0x0D000000))
                .clip(RoundedCornerShape(999.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(AurisColors.PastelBlue, AurisColors.PastelPurple, AurisColors.PastelGreen)
                    )
                )
                .padding(2.dp)    // gradient border thickness
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xB3FFFFFF))   // white/70%
                    .padding(horizontal = 16.dp)
            ) {
                Icon(Icons.Filled.Mic, contentDescription = null,
                    tint = AurisColors.LabelSecondary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text("Ask AI – How's my protein intake this week?",
                    fontSize = 15.sp, fontWeight = FontWeight.Medium,
                    color = AurisColors.LabelSecondary,
                    letterSpacing = (-0.2).sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── 2.5 Predictive Nudge ────────────────────────────────────
        nudge?.let {
            com.auris.feature.home.components.PredictiveNudgeCard(nudge = it)
            Spacer(Modifier.height(24.dp))
        }

        // Phase 12: Wellbeing feedback (e.g. show after 9 PM when implemented)
        com.auris.feature.home.components.WellbeingFeedbackCard()

        Spacer(Modifier.height(8.dp))   // Adjusted from mb-8=32dp to accommodate nudge

        // ── 3. Hero Row: Water | Body | Steps ──────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)            // h-[380px] in JSX
                .padding(horizontal = 4.dp) // px-1
        ) {
            // Water column (left)
            LiquidColumn(
                label       = "Water",
                value       = "2.5",
                unit        = "Liter",
                pct         = 75,
                gradTop     = Color(0xFF60A5FA),    // #60A5FA
                gradBot     = Color(0xFF2563EB),    // #2563EB
                accentColor = AurisColors.Blue,
                icon        = Icons.Outlined.WaterDrop,
                modifier    = Modifier.align(Alignment.CenterStart)
            )

            // Human body silhouette (center)
            BodySilhouette(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(220.dp)           // w-[220px] in JSX
                    .fillMaxHeight()
            )

            // Steps column (right)
            LiquidColumn(
                label       = "Steps",
                value       = stepsLabel,
                unit        = "Steps",
                pct         = stepsPct,
                gradTop     = Color(0xFF86EFAC),    // #86EFAC
                gradBot     = Color(0xFF16A34A),    // #16A34A
                accentColor = AurisColors.Green,
                icon        = Icons.Filled.DirectionsWalk,
                modifier    = Modifier.align(Alignment.CenterEnd)
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── 4. iOS Segmented Control Filter ─────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0x1F767680))   // #7676801F
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Day", "Week", "Month", "Year").forEach { tab ->
                val isSelected = timeFilter == tab
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .then(
                            if (isSelected) Modifier
                                .shadow(3.dp, RoundedCornerShape(6.dp), ambientColor = Color(0x1F000000))
                                .background(Color.White)
                            else Modifier
                        )
                        .clickable { timeFilter = tab }
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text       = tab,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = if (isSelected) AurisColors.LabelPrimary else AurisColors.LabelSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── 5. Staggered Cards (solid white, no glass) ──────────────

        // Row 1: Steps ring + Sleep
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Steps Ring card
            WhiteCard(modifier = Modifier.weight(1f).aspectRatio(1f)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(20.dp)
                ) {
                    RingWidget(pct = stepsPct, color = AurisColors.Green, size = 70.dp, strokeWidth = 8.dp) {
                        Icon(Icons.Filled.DirectionsWalk, contentDescription = null,
                            tint = AurisColors.Green, modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(stepsLabel, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                        color = AurisColors.LabelPrimary, letterSpacing = (-0.2).sp)
                    Text("Steps", fontSize = 12.sp, fontWeight = FontWeight.Medium,
                        color = AurisColors.LabelSecondary)
                }
            }

            // Sleep card
            WhiteCard(modifier = Modifier.weight(1f).aspectRatio(1f)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(20.dp)
                ) {
                    Icon(Icons.Filled.Bedtime, contentDescription = null,
                        tint = AurisColors.Purple, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(20.dp))
                    Text(sleepLabel, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                        color = AurisColors.LabelPrimary, letterSpacing = (-0.2).sp)
                    Text("Sleep", fontSize = 12.sp, fontWeight = FontWeight.Medium,
                        color = AurisColors.LabelSecondary)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Row 2: Heart Rate card (ECG waveform)
        WhiteCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Heart rate", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                        color = AurisColors.LabelPrimary, letterSpacing = (-0.2).sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("131", fontSize = 15.sp, fontWeight = FontWeight.Bold,
                                color = AurisColors.Red)
                            Spacer(Modifier.width(4.dp))
                            Text("high", fontSize = 12.sp, fontWeight = FontWeight.Medium,
                                color = AurisColors.LabelSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("65", fontSize = 15.sp, fontWeight = FontWeight.Bold,
                                color = AurisColors.LabelPrimary)
                            Spacer(Modifier.width(4.dp))
                            Text("avg", fontSize = 12.sp, fontWeight = FontWeight.Medium,
                                color = AurisColors.LabelSecondary)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                // ECG Waveform
                EcgWaveform(modifier = Modifier.fillMaxWidth().height(70.dp))
                Spacer(Modifier.height(8.dp))
                // X-axis labels
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    listOf("12 AM", "4 AM", "8 AM", "12 PM", "4 PM", "8 PM", "11 PM").forEach {
                        Text(it, fontSize = 10.sp, fontWeight = FontWeight.Medium,
                            color = AurisColors.LabelTertiary)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Row 3: Weight + Readiness/Recovery
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Weight card
            WhiteCard(modifier = Modifier.weight(1f).heightIn(min = 140.dp)) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Weight", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                            color = AurisColors.LabelPrimary, letterSpacing = (-0.2).sp)
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(AurisColors.Red)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("103", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                            color = AurisColors.LabelPrimary, letterSpacing = (-0.5).sp)
                        Spacer(Modifier.width(4.dp))
                        Text("kg", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                            color = AurisColors.LabelSecondary,
                            modifier = Modifier.padding(bottom = 3.dp))
                    }
                }
            }

            // Readiness + Recovery arcs
            WhiteCard(modifier = Modifier.weight(1f).heightIn(min = 140.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    ArcStat(label = "Readiness", value = 57, color = Color(0xFFFFCC00))
                    ArcStat(label = "Recovery",  value = 77, color = AurisColors.Green)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Row 4: Water bar — flat white card with inline thin bar
        WhiteCard(modifier = Modifier.fillMaxWidth().height(52.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                Text("Water", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                    color = AurisColors.LabelPrimary, letterSpacing = (-0.2).sp,
                    modifier = Modifier.width(60.dp))
                // Thin 4dp progress bar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0x143C3C43))
                ) {
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { visible = true }
                    val animW by animateFloatAsState(
                        targetValue   = if (visible) 0.70f else 0f,
                        animationSpec = tween(1200, easing = FastOutSlowInEasing),
                        label         = "waterBar"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animW)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(AurisColors.Teal, AurisColors.Blue)
                                )
                            )
                    )
                }
                Text("3.5 L", fontSize = 15.sp, fontWeight = FontWeight.Normal,
                    color = AurisColors.LabelSecondary)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Row 5: Calories donut + Macros + Streak (connected to real data)
        WhiteCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                // Calories donut — live from repository
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                ) {
                    RingWidget(
                        pct         = caloriePct,
                        color       = AurisColors.Orange,
                        size        = 100.dp,
                        strokeWidth = 8.dp
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("%,d".format(caloriesLeft), fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                color = AurisColors.LabelPrimary, letterSpacing = (-0.5).sp)
                            Text("kcal left", fontSize = 11.sp, fontWeight = FontWeight.Medium,
                                color = AurisColors.LabelSecondary, letterSpacing = 0.1.sp)
                        }
                    }
                }

                // Macros + streak — live from repository
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    listOf(
                        Triple("Protein", Pair("${totalProtein.toInt()}g", "${proteinGoal}g"),  Pair(proteinPct, AurisColors.Blue)),
                        Triple("Carbs",   Pair("${totalCarbs.toInt()}g", "${carbsGoal}g"), Pair(carbsPct, AurisColors.Green)),
                        Triple("Fat",     Pair("${totalFat.toInt()}g", "${fatGoal}g"),   Pair(fatPct, AurisColors.Orange)),
                    ).forEach { (label, amounts, bar) ->
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                    color = AurisColors.LabelPrimary)
                                Row {
                                    Text(amounts.first, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                        color = AurisColors.LabelPrimary)
                                    Text(" / ${amounts.second}", fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AurisColors.LabelSecondary)
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color(0x143C3C43))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(bar.first / 100f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(bar.second)
                                )
                            }
                        }
                    }

                    // Streak
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = AurisColors.Separator
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Filled.LocalFireDepartment, null,
                                tint = AurisColors.Orange, modifier = Modifier.size(14.dp))
                            Text("Streak", fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                color = AurisColors.LabelSecondary)
                        }
                        Text("12 days", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                            color = AurisColors.Orange)
                    }
                }
            }
        }
    }
}

/* ── WhiteCard — solid white card matching JSX bg-white rounded-[20px] ── */
@Composable
fun WhiteCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x0D000000)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White),
        content = content
    )
}

/* ── Human body silhouette ────────────────────────────────────────── */
@Composable
fun BodySilhouette(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val scaleX = size.width / 140f
        val scaleY = size.height / 320f

        with(drawContext.canvas) {
            val paint = androidx.compose.ui.graphics.Paint().apply {
                color = Color(0xFFE2E8F0)
                style = androidx.compose.ui.graphics.PaintingStyle.Fill
            }
            // Head
            val headPath = Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        left   = (70 - 14) * scaleX,
                        top    = (35 - 19) * scaleY,
                        right  = (70 + 14) * scaleX,
                        bottom = (35 + 19) * scaleY
                    )
                )
            }
            drawPath(headPath, paint)

            // Neck
            val neckPaint = androidx.compose.ui.graphics.Paint().apply { color = Color(0xFFCBD5E1) }
            drawRect(
                paint  = neckPaint,
                left   = 64 * scaleX, top    = 50 * scaleY,
                right  = 76 * scaleX, bottom = 60 * scaleY
            )

            // Torso
            val torsoPaint = androidx.compose.ui.graphics.Paint().apply { color = Color(0xFFD1D9E6) }
            val torsoPath = Path().apply {
                moveTo(52 * scaleX, 65 * scaleY)
                cubicTo(62 * scaleX, 58 * scaleY, 78 * scaleX, 58 * scaleY, 88 * scaleX, 65 * scaleY)
                cubicTo(96 * scaleX, 75 * scaleY, 96 * scaleX, 90 * scaleY, 92 * scaleX, 105 * scaleY)
                cubicTo(88 * scaleX, 120 * scaleY, 84 * scaleX, 140 * scaleY, 82 * scaleX, 145 * scaleY)
                cubicTo(78 * scaleX, 150 * scaleY, 62 * scaleX, 150 * scaleY, 58 * scaleX, 145 * scaleY)
                cubicTo(56 * scaleX, 140 * scaleY, 52 * scaleX, 120 * scaleY, 48 * scaleX, 105 * scaleY)
                cubicTo(44 * scaleX, 90 * scaleY, 44 * scaleX, 75 * scaleY, 52 * scaleX, 65 * scaleY)
                close()
            }
            drawPath(torsoPath, torsoPaint)

            // Left arm
            val armPaint = androidx.compose.ui.graphics.Paint().apply { color = Color(0xFFCBD5E1) }
            val leftArm = Path().apply {
                moveTo(52 * scaleX, 65 * scaleY)
                cubicTo(40 * scaleX, 70 * scaleY, 34 * scaleX, 95 * scaleY, 32 * scaleX, 115 * scaleY)
                lineTo(26 * scaleX, 160 * scaleY)
                cubicTo(25 * scaleX, 170 * scaleY, 30 * scaleX, 175 * scaleY, 34 * scaleX, 170 * scaleY)
                cubicTo(38 * scaleX, 160 * scaleY, 42 * scaleX, 125 * scaleY, 46 * scaleX, 105 * scaleY)
                close()
            }
            drawPath(leftArm, armPaint)

            // Right arm
            val rightArm = Path().apply {
                moveTo(88 * scaleX, 65 * scaleY)
                cubicTo(100 * scaleX, 70 * scaleY, 106 * scaleX, 95 * scaleY, 108 * scaleX, 115 * scaleY)
                lineTo(114 * scaleX, 160 * scaleY)
                cubicTo(115 * scaleX, 170 * scaleY, 110 * scaleX, 175 * scaleY, 106 * scaleX, 170 * scaleY)
                cubicTo(102 * scaleX, 160 * scaleY, 98 * scaleX, 125 * scaleY, 94 * scaleX, 105 * scaleY)
                close()
            }
            drawPath(rightArm, armPaint)

            // Left leg
            val legPaint = androidx.compose.ui.graphics.Paint().apply { color = Color(0xFFD1D9E6) }
            val leftLeg = Path().apply {
                moveTo(58 * scaleX, 145 * scaleY)
                cubicTo(54 * scaleX, 160 * scaleY, 48 * scaleX, 220 * scaleY, 48 * scaleX, 260 * scaleY)
                cubicTo(46 * scaleX, 275 * scaleY, 48 * scaleX, 285 * scaleY, 54 * scaleX, 285 * scaleY)
                cubicTo(58 * scaleX, 285 * scaleY, 62 * scaleX, 270 * scaleY, 62 * scaleX, 260 * scaleY)
                cubicTo(62 * scaleX, 220 * scaleY, 68 * scaleX, 165 * scaleY, 70 * scaleX, 148 * scaleY)
                close()
            }
            drawPath(leftLeg, legPaint)

            // Right leg
            val rightLeg = Path().apply {
                moveTo(82 * scaleX, 145 * scaleY)
                cubicTo(86 * scaleX, 160 * scaleY, 92 * scaleX, 220 * scaleY, 92 * scaleX, 260 * scaleY)
                cubicTo(94 * scaleX, 275 * scaleY, 92 * scaleX, 285 * scaleY, 86 * scaleX, 285 * scaleY)
                cubicTo(82 * scaleX, 285 * scaleY, 78 * scaleX, 270 * scaleY, 78 * scaleX, 260 * scaleY)
                cubicTo(78 * scaleX, 220 * scaleY, 72 * scaleX, 165 * scaleY, 70 * scaleX, 148 * scaleY)
                close()
            }
            drawPath(rightLeg, legPaint)

            // Feet
            val footPaint = androidx.compose.ui.graphics.Paint().apply { color = Color(0xFFBDC6D9) }
            val leftFoot = Path().apply {
                moveTo(48 * scaleX, 280 * scaleY)
                cubicTo(40 * scaleX, 285 * scaleY, 40 * scaleX, 295 * scaleY, 50 * scaleX, 295 * scaleY)
                lineTo(58 * scaleX, 295 * scaleY)
                cubicTo(62 * scaleX, 295 * scaleY, 62 * scaleX, 285 * scaleY, 54 * scaleX, 280 * scaleY)
                close()
            }
            drawPath(leftFoot, footPaint)
            val rightFoot = Path().apply {
                moveTo(92 * scaleX, 280 * scaleY)
                cubicTo(100 * scaleX, 285 * scaleY, 100 * scaleX, 295 * scaleY, 90 * scaleX, 295 * scaleY)
                lineTo(82 * scaleX, 295 * scaleY)
                cubicTo(78 * scaleX, 295 * scaleY, 78 * scaleX, 285 * scaleY, 86 * scaleX, 280 * scaleY)
                close()
            }
            drawPath(rightFoot, footPaint)
        }
    }
}

/* ── ECG Waveform ────────────────────────────────────────────────── */
@Composable
fun EcgWaveform(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val scaleX = w / 180f
        val scaleY = h / 80f

        val points = listOf(
            0f to 40f, 20f to 40f, 25f to 30f, 30f to 60f, 35f to 15f,
            40f to 45f, 45f to 40f, 60f to 40f, 65f to 25f, 70f to 55f,
            75f to 20f, 80f to 40f, 95f to 40f, 100f to 20f, 105f to 70f,
            110f to 10f, 115f to 50f, 120f to 40f, 140f to 40f, 145f to 35f,
            150f to 50f, 155f to 40f, 180f to 40f
        )

        val path = Path().apply {
            points.forEachIndexed { idx, (x, y) ->
                val sx = x * scaleX
                val sy = y * scaleY
                if (idx == 0) moveTo(sx, sy) else lineTo(sx, sy)
            }
        }

        drawPath(
            path  = path,
            brush = Brush.horizontalGradient(listOf(AurisColors.Red, AurisColors.Red2)),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 2.dp.toPx(),
                cap   = StrokeCap.Round,
                join  = StrokeJoin.Round
            )
        )
    }
}

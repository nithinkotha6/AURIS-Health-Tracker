# What's Off Road — Sorus Development Deviations Report
**Date**: February 20, 2026  
**Focus**: Architecture, Code Structure, and Design Pattern Misalignment

---

## Executive Summary

**Critical Finding**: The KAIROS-ANDROID codebase is **70% correct on UI/UX aesthetics** but **0% compliant with planned architecture**. It builds custom Canvas views instead of using Jetpack Compose, lacks all clean architecture patterns (MVVM/DI/Repository), and has hardcoded data throughout.

**Off-Road Severity**: 🔴 **HIGH** — Major refactoring required before Phase 2 can begin.

---

## Deviation Categories

### 1. TECHNOLOGY STACK MISMATCH 🔴

#### Planned (Architecture.md 2.1)
```
- UI Framework: Jetpack Compose + Material3
- Pattern: Clean Architecture + MVVM + Repository + Hilt DI
- State: ViewModel + StateFlow
- Navigation: Jetpack Navigation Component
```

#### Implemented (KAIROS-ANDROID)
```
- UI Framework: Android Views (Canvas-based) + XML layouts
- Pattern: None (monolithic Activity)
- State: Hardcoded properties in views + Activity
- Navigation: Manual Intent logging
```

#### Issue Details
| Aspect | Planned | Implemented | Impact |
|:---|:---|:---|:---|
| **UI Layer** | Jetpack Compose | Custom Views + Canvas | ❌ Requires complete refactor; Compose not even imported |
| **Architecture** | MVVM | None (Activity only) | ❌ Untestable; tight coupling; no state management |
| **DI** | Hilt (@HiltAndroidApp, @Inject) | None | ❌ No dependency graph; manual object creation |
| **State** | StateFlow<T> + ViewModel | Direct view properties | ❌ No reactive updates; all state mutation manual |
| **Navigation** | Jetpack Navigation Component | Manual logging | ❌ Deep links won't work; no back stack management |
| **Persistence** | Room + SQLCipher | None | ❌ All data lost on app restart |

**Why This Matters**:
- Custom Views are **not composable** (can't combine/reuse like Compose Composables)
- Canvas-based rendering is harder to maintain (painting logic mixed with state)
- No state management = unpredictable UI behavior
- No Hilt = can't inject repositories, usecases, or services

**Effort to Fix**: **3–5 days** (migrate all custom Views to Compose Composables)

---

### 2. NO CLEAN ARCHITECTURE IMPLEMENTATION 🔴

#### Planned (Architecture.md 4, 13)
```
Presentation Layer (Compose UI + ViewModel)
    ↓ (uses)
Domain Layer (UseCases, Repository Interfaces)
    ↓ (uses)
Data Layer (Repository Implementations, Room DB, Services)
```

#### Implemented
```
DashboardActivity
    ↓ (directly instantiates)
Custom Views (Canvas)
    ↓ (use)
Hardcoded sample data in onCreate()
```

#### Specific Violations

**1. No ViewModel Layer**
```kotlin
// ❌ CURRENT (wrong)
class DashboardActivity : AppCompatActivity() {
    private val vitaminData = listOf(
        VitaminData("Vitamin D", "1,800 IU", "75% · Salmon today", 75)
        // ... hardcoded data in Activity
    )
}

// ✅ PLANNED (correct)
class DashboardViewModel @Inject constructor(
    private val vitaminRepository: VitaminRepository
) : ViewModel() {
    val vitaminStatus: StateFlow<List<VitaminStatus>> = 
        vitaminRepository.getTodayVitaminStatus().stateIn(...)
}

// In Activity:
val viewModel: DashboardViewModel by viewModels()
val vitamins = viewModel.vitaminStatus.collectAsState()
```

**2. No Repository Layer**
```kotlin
// ❌ CURRENT (wrong)
// No repository; data accessed directly in Activity
vitaminData = listOf(VitaminData(...))

// ✅ PLANNED (correct)
interface VitaminRepository {
    fun getTodayVitaminStatus(): Flow<List<VitaminStatus>>
    fun recalculateVitaminStatus(entries: List<FoodEntryEntity>): List<VitaminStatus>
}

@Inject
private lateinit var vitaminRepository: VitaminRepository
```

**3. No UseCase Layer**
```kotlin
// ❌ CURRENT (wrong)
// Business logic missing; no calculation
vitaminPercent = 75  // hardcoded

// ✅ PLANNED (correct)
class CalcVitaminStatusUseCase @Inject constructor(
    private val vitaminRepository: VitaminRepository
) {
    operator fun invoke(foodLog: List<FoodEntry>): List<VitaminStatus> {
        // Calculate vitamin % from food log entries
        // Apply absorption modifiers
        // Return status with deficiency level
    }
}
```

**4. No Domain Models**
```kotlin
// ❌ CURRENT
data class VitaminData(
    val name: String,
    val value: String,
    val caption: String,
    val percent: Int
)

// ✅ PLANNED
data class VitaminStatus(
    val vitaminId: String,
    val name: String,
    val unit: String,
    val rda: Float,
    val rawIntake: Float,
    val effectiveIntake: Float,  // After absorption modifiers
    val adjustedRda: Float,       // After burn adjustments
    val percentComplete: Float,
    val deficiencyLevel: DeficiencyLevel,
    val absorptionFactors: List<AbsorptionModifier>
)
```

**Impact**:
- ❌ Can't unit test business logic (no UseCase to test)
- ❌ Can't swap implementations (no Repository interface)
- ❌ Can't share code with other features (tight to DashboardActivity)
- ❌ Hard to debug (state mutations scattered)
- ❌ No way to handle complex features (logging, absorption, Health Connect)

---

### 3. NO MODULE STRUCTURE 🔴

#### Planned (Architecture.md 3)
```
sorus/
├── app/
├── core/
│   ├── core-ui/
│   ├── core-data/
│   ├── core-domain/
│   └── core-common/
├── feature/
│   ├── feature-home/
│   ├── feature-log/
│   ├── feature-diary/
│   ├── feature-vitamins/
│   ├── feature-habits/
│   └── feature-overview/
└── buildSrc/
```

#### Implemented
```
KAIROS-ANDROID/
├── java/com/sorus/
│   ├── DashboardActivity.kt
│   ├── ui/
│   └── model/
└── res/
```

#### Issue Details

| Module | Planned | Implemented | Missing |
|:---|:---|:---|:---|
| **core-ui** | Theme, tokens, Composables | Partially (colors.xml, themes.xml) | GlassCard, ScoreRing as Composables |
| **core-data** | Room DB, DAOs, Repositories | None | All persistence |
| **core-domain** | UseCases, interfaces | None | All business logic |
| **feature-home** | DashboardViewModel, HomeScreen Composable | Partial (DashboardActivity only) | ViewModel, proper structure |
| **feature-log** | LogScreen, LogViewModel, CameraX | None | All logging features |
| **feature-diary** | DiaryScreen | None | All food diary features |
| **feature-vitamins** | VitaminsScreen, VitaminViewModel | None | All vitamin tracking features |
| **feature-overview** | OverviewScreen, trend charts | None | All analytics |
| **feature-profile** | ProfileScreen, settings | None | All profile features |
| **buildSrc** | Version catalog | None | Dependency management |

**Impact**:
- ❌ Can't parallelize development (team members stumble over same code)
- ❌ Can't reuse core-ui components across features (no module boundary)
- ❌ Can't test features in isolation (tight coupling between modules)
- ❌ Future Gradle scaling will be hard (no modular build targets)

---

### 4. LIMITED VITAMIN COVERAGE 🔴

#### Planned (Architecture.md 7.1)
**19 nutrients** to track (full RDA list):
- Vitamins: A, B1, B2, B3, B5, B6, B7, B9, B12, C, D, E, K
- Minerals: Calcium, Iron, Magnesium, Zinc
- Macros: Protein

#### Implemented
**Only 4 hardcoded vitamins**:
```kotlin
private val vitaminData = listOf(
    VitaminData("Vitamin D",  "1,800 IU",  "75% · Salmon today",    75),
    VitaminData("Vitamin B12","2.4 mcg",   "80% · Eggs & meat",     80),
    VitaminData("Vitamin C",  "72 mg",     "80% · Citrus boost",    80),
    VitaminData("Vitamin E",  "12.8 mg",   "85% · Looking good",    85)
)
```

#### Issue Details

| Nutrient | Planned | Implemented | RDA | Unit |
|:---|:---|:---|:---|:---|
| Vitamin A | ✅ | ❌ | 900/700 | mcg |
| Vitamin B1 | ✅ | ❌ | 1.2/1.1 | mg |
| Vitamin B2 | ✅ | ❌ | 1.3/1.1 | mg |
| Vitamin B3 | ✅ | ❌ | 16/14 | mg |
| Vitamin B5 | ✅ | ❌ | 5/5 | mg |
| Vitamin B6 | ✅ | ❌ | 1.3/1.3 | mg |
| Vitamin B7 | ✅ | ❌ | 30/30 | mcg |
| Vitamin B9 | ✅ | ❌ | 400/400 | mcg |
| **Vitamin B12** | ✅ | ✅ | 2.4/2.4 | mcg |
| **Vitamin C** | ✅ | ✅ | 90/75 | mg |
| **Vitamin D** | ✅ | ✅ | 600/600 | IU |
| **Vitamin E** | ✅ | ✅ | 15/15 | mg |
| Vitamin K | ✅ | ❌ | 120/90 | mcg |
| Calcium | ✅ | ❌ | 1,000/1,000 | mg |
| Iron | ✅ | ❌ | 8/18 | mg |
| Magnesium | ✅ | ❌ | 420/320 | mg |
| Zinc | ✅ | ❌ | 11/8 | mg |
| Protein | ✅ | ❌ | per goal | g |
| Collagen | ✅ | ❌ | 10,000 | mg |

**Hardcoded Layout Issue**:
```xml
<!-- ❌ CURRENT: GridLayout with fixed 4 cards -->
<GridLayout
    android:columnCount="2"
    android:rowCount="2">
    <com.sorus.ui.VitaminGlassCard android:id="@+id/cardVitD" ... />
    <com.sorus.ui.VitaminGlassCard android:id="@+id/cardVitB12" ... />
    <com.sorus.ui.VitaminGlassCard android:id="@+id/cardVitC" ... />
    <com.sorus.ui.VitaminGlassCard android:id="@+id/cardVitE" ... />
</GridLayout>

<!-- ✅ PLANNED: RecyclerView or Compose LazyGrid with dynamic data -->
<androidx.compose.foundation.lazy.grid.LazyVerticalGrid>
    items(vitaminStatus) { vitamin ->
        VitaminCard(vitamin)
    }
</androidx.compose.foundation.lazy.grid.LazyVerticalGrid>
```

**Impact**:
- ❌ Can't expand to 19 nutrients without hardcoding 15 more cards
- ❌ Can't dynamically filter/reorder vitamins
- ❌ Can't test nutrient logic (only works for 4 cases)
- ❌ Can't showcase Phase 1 completion (plan requires full list)

**Effort to Fix**: **1 day** (switch to Compose LazyVerticalGrid + data-driven rendering)

---

### 5. HARDCODED DATA THROUGHOUT 🔴

#### Planned
All data flows from:
```
Room DB → Repository → ViewModel → Compose → UI
```

#### Implemented
All data hardcoded in Activity `onCreate()`:
```kotlin
private val dailyScore  = 82
private val waterPct    = 75
private val energyPct   = 62

private val vitaminData = listOf(
    VitaminData("Vitamin D",  "1,800 IU", "75% · Salmon today",    75),
    // ...
)

override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    binding.scoreRing.score = dailyScore  // Hardcoded
    binding.waterCard.setFillAnimated(waterPct)  // Hardcoded
    vitaminData.forEachIndexed { index, data ->
        cards[index].updateFill(data.percent)  // Hardcoded
    }
}
```

#### Hardcoded Values Found

| Component | Value | Hardcoded |
|:---|:---|:---|
| Daily Score | 82 | ✅ |
| Water fill | 75% | ✅ |
| Energy fill | 62% | ✅ |
| Vitamin D | 75% | ✅ |
| Vitamin B12 | 80% | ✅ |
| Vitamin C | 80% | ✅ |
| Vitamin E | 85% | ✅ |
| Pulse enabled | all ≥80% | ✅ |
| Date label | SimpleDateFormat | ✅ (dynamic, correct) |

**Impact**:
- ❌ Can't test data fetching logic
- ❌ Can't test state updates (setScoreAnimated() works, but no API to retrieve score)
- ❌ Can't integrate with Room DB or API
- ❌ All data lost on app restart
- ❌ Can't test logging features (no way to add/update data)

---

### 6. NO DATA PERSISTENCE LAYER 🔴

#### Planned (Architecture.md 12)
```kotlin
@Database(
    entities = [UserProfileEntity::class, FoodEntryEntity::class, 
                VitaminLogEntity::class, ...],
    version = 1,
    exportSchema = true
)
abstract class SorusDatabase : RoomDatabase()

// On app startup:
// 1. NutritionReferenceSeed populates 19 nutrients
// 2. FoodEntryEntity written on food log
// 3. VitaminLogEntity computed and stored
```

#### Implemented
**Nothing**. Zero persistence setup.
- ❌ No Room DB initialization
- ❌ No SQLCipher encryption
- ❌ No migrations
- ❌ No seed data
- ❌ No DAOs
- ❌ No Android Keystore setup

**Missing Files**:
```
❌ SorusDatabase.kt
❌ UserProfileEntity.kt
❌ FoodEntryEntity.kt
❌ VitaminLogEntity.kt
❌ NutritionReferenceEntity.kt
❌ Dao files (UserProfileDao.kt, FoodEntryDao.kt, etc.)
❌ RepositoryImpl files (RoomFoodRepositoryImpl.kt, etc.)
❌ Seed data (NutritionReferenceSeed.kt)
❌ build.gradle dependencies (Room, SQLCipher)
```

**Impact**:
- ❌ Phase 2–6 can't even begin (need DB for logging)
- ❌ Can't persist settings or user profile
- ❌ Can't track food history
- ❌ Can't test repositories or UseCases
- ❌ Can't implement Health Connect sync (needs DB for burn adjustments)

---

### 7. WATER & ENERGY CARDS (SCOPE CREEP) 🟡

#### Planned
Focus on **vitamin/nutrient tracking**. Water and energy are components of the score, not separate cards.

#### Implemented
Two dedicated side cards (Water, Energy) with their own liquid animations.

```xml
<!-- ❌ CURRENT: Dedicated water and energy cards -->
<com.sorus.ui.GlassLiquidCard
    android:id="@+id/waterCard"
    app:liquidColor="#00A8FF"
    app:fillPercent="75"
    app:cardLabel="Water"/>

<com.sorus.ui.GlassLiquidCard
    android:id="@+id/energyCard"
    app:liquidColor="#00FF9F"
    app:fillPercent="62"
    app:cardLabel="Energy"/>

<!-- ✅ PLANNED: Water/Energy as macro stats, not separate cards -->
<!-- Could be rows or small indicators, not full card focus -->
```

**Why This Matters**:
- Adds UI complexity without being in the roadmap
- Water hydration tracking is a feature, not a core Phase 1 item
- Energy (calories) is a macro, should be tracked via food logging
- Takes layout real estate from the 19 nutrients that ARE planned

**Impact**:
- ⚠️ Adds visual clutter (3 column layout: Water | Score Ring | Energy)
- ⚠️ Time spent on scope-creep instead of multi-module setup
- ⚠️ Water/Energy cards not integrated into ViewModel (extra state to manage)

**Recommendation**: Keep cards for now (UI looks good), but integrate into proper ViewModel/Repository architecture. Don't expand Water/Energy features beyond aesthetic display in Phase 1.

---

### 8. NO NAVIGATION FRAMEWORK 🔴

#### Planned (Architecture.md 13)
```kotlin
sealed class Screen(val route: String) {
    object Home        : Screen("home")
    object Log         : Screen("log")
    object Diary       : Screen("diary?date={date}")
    object Overview    : Screen("overview")
    object Habits      : Screen("habits")
    object Profile     : Screen("profile")
}

// In MainActivity:
NavHost(navController, startDestination = Screen.Home.route) {
    composable(Screen.Home.route) { HomeScreen() }
    composable(Screen.Log.route) { LogScreen() }
    composable(Screen.Diary.route) { DiaryScreen(...) }
    // ...
}
```

#### Implemented
```kotlin
// Navigation is just logging:
private fun navigateTo(screen: String) {
    android.util.Log.d("Sorus", "Navigate → $screen")
}

// Called from nav bar:
binding.navBar.onTabSelected = { tabIndex ->
    when (tabIndex) {
        0 -> { /* no-op */ }
        1 -> navigateTo("Log")
        2 -> navigateTo("Diary")
        3 -> navigateTo("Overview")
        4 -> navigateTo("Profile")
    }
}
```

#### Issues

| Feature | Planned | Implemented | Impact |
|:---|:---|:---|:---|
| **Jetpack Navigation** | ✅ NavController, NavHost | ❌ Manual logging | Can't handle deep links, back stack |
| **Route definitions** | `sealed class Screen` | ❌ String literals | Hard-coded, not typesafe |
| **Fragment/Composable transitions** | Replace with `navController.navigate()` | ❌ Just logs | No screen switching at all |
| **Back stack management** | Automatic | ❌ Manual (not implemented) | Can't go back |
| **Deep link support** | `sorus://log?v=1&...` | ❌ No handling | AI food log will fail to route |
| **Type-safe navigation** | `navController.navigate(Screen.Log)` | ❌ String juggling | Error-prone |

**Impact**:
- ❌ Tapping Log/Diary/Overview/Profile tabs does nothing (just logs)
- ❌ Can't implement deep link handler for sorus:// protocol (Phase 8)
- ❌ Can't tie Voice/Camera logging to Log screen (Phase 7-8)
- ❌ No proper screen lifecycle (no onResume/onPause per screen)
- ❌ Back button behavior undefined

**Example Failure (Phase 8)**:
```kotlin
// When AI app responds with sorus://log?v=1&meal=lunch...
// MainActivity.onNewIntent() should be triggered
// But can't route to Log screen without Navigation Component

override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    val uri = intent.data
    if (uri?.scheme == "sorus") {
        // ❌ How do we route to Log screen and display confirmation?
        // navController.navigate(Screen.Log) — but navController doesn't exist
        navigateTo("Log")  // Only logs, doesn't navigate
    }
}
```

**Effort to Fix**: **2 days** (set up Navigation Component + migrate screens)

---

### 9. OTHER SCREEN STUBS MISSING 🔴

#### Planned (Phases 2–12)
- feature-log: Logging form, camera, voice
- feature-diary: Food history, day view
- feature-vitamins: Full 19-nutrient tracker
- feature-overview: 7-day trend charts, stats
- feature-profile: Settings, export, backup
- (Plus features for habits, AI routing, Health Connect, etc.)

#### Implemented
**No other screens exist**. Only DashboardActivity.

#### Missing Screens

| Screen | Module | Planned Features | Status |
|:---|:---|:---|:---|
| **Log** | feature-log | Camera FAB, voice mic, form | ❌ Missing 100% |
| **Diary** | feature-diary | Food list, day picker | ❌ Missing 100% |
| **Overview** | feature-overview | 7-day charts, stats | ❌ Missing 100% |
| **Profile** | feature-profile | Settings, export, backup | ❌ Missing 100% |
| **Vitamins** | feature-vitamins | Full 19-nutrient list | ⚠️ Partial (4 vitamins) |

**Impact**:
- ❌ Can't test any logging features (Phase 4–8)
- ❌ Can't test food diary (Phase 4)
- ❌ Can't test trend analytics (Phase 11)
- ❌ Can't test export/backup (Phase 12)
- ❌ Blocks all feature development after Phase 1

---

### 10. NO DEPENDENCY INJECTION 🔴

#### Planned (Architecture.md, Hilt section)
```kotlin
// Application class
@HiltAndroidApp
class SorusApp : Application()

// DashboardActivity
@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    @Inject
    private lateinit var vitaminRepository: VitaminRepository
    
    private val viewModel: DashboardViewModel by viewModels()
}

// ViewModel
class DashboardViewModel @Inject constructor(
    private val vitaminRepository: VitaminRepository
) : ViewModel()

// Hilt module
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideVitaminRepository(db: SorusDatabase): VitaminRepository =
        RoomVitaminRepositoryImpl(db.vitaminLogDao())
}
```

#### Implemented
**Nothing**. No Hilt, no DI, manual object creation.

```kotlin
// Hard-coded in DashboardActivity
private val vitaminData = listOf(VitaminData(...))  // ❌

// Would need to be:
// 1. @HiltAndroidApp on Application
// 2. @AndroidEntryPoint on Activity
// 3. @Inject private lateinit var repository: VitaminRepository
// 4. private val viewModel: DashboardViewModel by viewModels()
```

**Missing Setup**:
- ❌ Application class with @HiltAndroidApp
- ❌ Hilt modules (@Module, @Provides, @Singleton)
- ❌ build.gradle Hilt dependencies
- ❌ kapt compiler setup
- ❌ @AndroidEntryPoint on activities
- ❌ @Inject annotations

**Impact**:
- ❌ Can't test ViewModels (no way to inject mocks)
- ❌ Can't test Repositories (no way to provide fakes)
- ❌ Tight coupling between components (hard to maintain)
- ❌ Can't feature-flag or swap implementations
- ❌ Memory leaks possible (manual lifecycle management)

**Effort to Fix**: **1 day** (add Hilt setup)

---

### 11. DEFICIENCY LEVEL COLOR CODING MISSING 🟡

#### Planned (Architecture.md 7.2)
```kotlin
enum class DeficiencyLevel(val color: Color) {
    OPTIMAL   (Color(0xFF00E0FF))   // Teal — ≥ 80%
    ADEQUATE  (Color(0xFF4CAF50))   // Green — 60–79%
    LOW       (Color(0xFFFFB700))   // Amber/Gold — 40–59%
    DEFICIENT (Color(0xFFFF8C00))   // Orange — < 40%
    CRITICAL  (Color(0xFFFF4444))   // Red — < 15%
}

// In VitaminGlassCard:
// Card color changes based on deficiencyLevel
val cardColor = when(vitamin.deficiencyLevel) {
    OPTIMAL -> Color.teal + pulse
    ADEQUATE -> Color.green
    LOW -> Color.amber
    ...
}
```

#### Implemented
**All cards use teal** (color #00E0FF for card fill).

```kotlin
// ❌ VitaminGlassCard.kt
private val colTealTop    = Color.parseColor("#00E0FF")  // Always teal
private val colTealBottom = Color.parseColor("#0080C0")  // Always teal

// Card always renders teal gradient, regardless of vitamin percent
tubeFillPaint.shader = LinearGradient(
    0f, tubeTop, 0f, tubeBottom,
    colTealTop, colTealBottom,  // ❌ Hard-coded teal
    Shader.TileMode.CLAMP
)
```

#### Missing Feature

| Percent Range | Color | Current | Issue |
|:---|:---|:---|:---|
| ≥80% OPTIMAL | Teal #00E0FF | ✅ Teal | Matches |
| 60–79% ADEQUATE | Green #4CAF50 | ❌ Teal | Wrong |
| 40–59% LOW | Amber #FFB700 | ❌ Teal | Wrong |
| <40% DEFICIENT | Orange #FF8C00 | ❌ Teal | Wrong |
| <15% CRITICAL | Red #FF4444 | ❌ Teal | Wrong |

**Impact**:
- ⚠️ User can't visually see vitamin deficiency status (no color feedback)
- ⚠️ Plan calls for color-coded UX (brown-green-teal gradient per tier)
- ⚠️ Loses predictive nudge context (can't quickly spot low nutrients)

**Effort to Fix**: **1 day** (parse vitamin deficiencyLevel, map to color, update card paint shader)

---

### 12. NO ABSORPTION MODIFIER LOGIC 🔴

#### Planned (Architecture.md 7.3, Phase 9)
```kotlin
// Example: Vitamin A + fat → +20% effective absorption
// Example: Zinc + phytate-rich meal → -30% effective absorption

// In CalcVitaminStatusUseCase:
val effectiveIntake = when {
    vitaminA && fatsInMeal > 10g -> rawIntake * 1.20f
    zinc && phytatesInMeal > 2g -> rawIntake * 0.70f
    else -> rawIntake
}

// VitaminLogEntity stores both:
data class VitaminLogEntity(
    val rawIntakeAmount: Float,      // What was logged
    val effectiveIntakeAmount: Float // After modifiers
)
```

#### Implemented
**No absorption logic at all**. Percent is static.

```kotlin
// ❌ VitaminGlassCard just displays:
private var vitaminPercent = 75  // Static, no calculation
```

**Impact**:
- ❌ Can't implement Phase 9 (Absorption Modifiers)
- ❌ Vitamin tracking isn't accurate (ignores real-world absorption variability)
- ❌ No diff between raw and effective intake
- ⚠️ This is a Phase 9 feature, so OK to defer, but no architecture ready

---

### 13. NO BURN ADJUSTMENTS FROM HEALTH CONNECT 🔴

#### Planned (Architecture.md 8.4, Phase 10)
```kotlin
// Steps > 600 today → RDA multiplier ×1.20
// Sleep < 6 hours → B-vitamin RDA ×1.15
//
// Example: Need 90mg Vitamin C normally
//   But burned 700 calories today → RDA becomes 90 * 1.20 = 108mg

suspend fun applyBurnAdjustments(vitaminStatus: List<VitaminStatus>): List<VitaminStatus> {
    val steps = healthConnect.readTodaySteps()
    val burnCals = healthConnect.readActiveCaloriesBurned()
    
    return vitaminStatus.map { vs ->
        val burnMultiplier = when {
            burnCals > 600 -> 1.20f
            ... 
        }
        vs.copy(adjustedRda = vs.baseRDA * burnMultiplier)
    }
}
```

#### Implemented
**No burn adjustments**. Hard-coded RDA.

```kotlin
// ❌ All RDA is static
vitaminPercent = 75  // Not adjusted for activity
```

**Impact**:
- ❌ Can't implement Phase 10 (Health Connect Integration)
- ❌ Accuracy limited (doesn't account for user activity)
- ⚠️ This is a Phase 10 feature, so OK to defer

---

### 14. MISSING BUILD.GRADLE CONFIGURATION 🔴

#### Planned
Full Android/Kotlin build setup with all dependencies.

#### Implemented
**No build.gradle files shown**. Missing critical setup:
- ❌ No compileSdkVersion / targetSdkVersion / minSdkVersion
- ❌ No dependencies (Material3, Compose, Room, Hilt, etc.)
- ❌ No Kotlin plugin configuration
- ❌ No ProGuard rules
- ❌ No buildFeatures (viewBinding, dataBinding, compose)
- ❌ No version catalog or buildSrc

**Critical Missing Dependencies**:
```gradle
// ❌ Not defined anywhere
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("com.google.dagger:hilt-android:2.52")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8+")
implementation("androidx.room:room-runtime:2.6+")
implementation("net.zetetic:android-database-sqlcipher:4.5.4")
kapt("com.google.dagger:hilt-android-compiler:2.52")
kapt("androidx.room:room-compiler:2.6+")
```

**Impact**:
- ❌ App won't compile (missing resource classes)
- ❌ Compose imports will fail
- ❌ Room DB can't be added
- ❌ Hilt won't work
- ❌ Can't run on actual device

---

### 15. XML ATTRIBUTES NOT REGISTERED 🟡

#### Planned
Custom view attributes defined in `res/values/attrs.xml`:
```xml
<declare-styleable name="ScoreRingView">
    <attr name="ringScore" format="integer"/>
    <attr name="ringStrokeWidth" format="dimension"/>
    <attr name="ringPulseEnabled" format="boolean"/>
</declare-styleable>
```

#### Implemented
**Attributes defined in comments only** (inside VitaminData.kt):
```kotlin
/*
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="ScoreRingView">
        <attr name="ringScore"        format="integer"/>
        ...
    </declare-styleable>
</resources>
*/
```

**Issue**: Attributes won't be compiled into R.styleable unless manually created in actual attrs.xml file.

**Impact**:
- ⚠️ `ta.getInt(R.styleable.ScoreRingView_ringScore, ...)` will fail (R.styleable not generated)
- ⚠️ Custom views won't initialize from XML
- ⚠️ Must be fixed before app runs

---

## Summary: Off-Road Issues by Severity

### 🔴 BLOCKING (Must fix before Phase 2)
1. No Clean Architecture (MVVM/DI missing)
2. No Module Structure (all in one package)
3. No Persistence Layer (Room DB missing)
4. No Navigation Framework (screens don't route)
5. Tech stack mismatch (Views instead of Compose)
6. No build.gradle setup (app won't compile)
7. No attrs.xml (custom views won't initialize)

### 🟡 DEGRADING (Should fix for Phase 1 completion)
8. Only 4 vitamins (should be 19)
9. Water/Energy scope creep (not in plan)
10. No color-coded deficiency levels (visual feedback missing)
11. No DI setup (untestable, tight coupling)

### ⚠️ FUTURE (Can defer but will block later phases)
12. No absorption modifier logic (Phase 9)
13. No Health Connect burn adjustments (Phase 10)
14. No deep link handling (Phase 8)
15. No logging features (Phase 4+)

---

## Recommendations

### Immediate Actions (Next 2–3 days)
1. **Create and run attrs.xml** (1 hour)
2. **Set up build.gradle** with all dependencies (2 hours)
3. **Add Hilt DI** (3 hours)
4. **Create DashboardViewModel** (2 hours)
5. **Create VitaminRepository interface + in-memory impl** (2 hours)
6. **Total**: ~1.5 days to stabilize Phase 1

### Medium-term Actions (Next 3–5 days)
7. **Migrate Views → Compose** (3 days — or keep Views with Compose wrappers)
8. **Expand vitamins from 4 → 19** (1 day)
9. **Create Room DB skeleton** + entities (2 days)
10. **Create other screen stubs** (Log, Diary, Overview, Profile) (2 days)
11. **Add Navigation Component** (2 days)

### Long-term Actions (After Phase 1)
12. Continue with Phase 2–12 per action_plan.md (already well-designed)

---

## Decision: Go Forward orRebuild?

### Option A: Keep Current Views + Add Architecture
**Pros**: Custom Views work well, animations are good  
**Cons**: Views can't be composed, harder to test, not per Architecture.md spec  
**Effort**: 3–4 days to add ViewModel/Repository/DI around Views

### Option B: Migrate to Jetpack Compose (Recommended)
**Pros**: Fully aligns with Architecture.md, easier state management, testable  
**Cons**: Refactoring Views to Composables takes 3–4 days  
**Effort**: 3–4 days to convert custom Views to Composables  
**Benefit**: Future-proof, composable, modular

**Recommendation**: **Option B**. Custom Views are a dead-end in Android. Jetpack Compose is the future. Better to invest the 3–4 days now than regret it later.

---

*Assessment completed by: Android Engineering Lead*  
*Severity rating: 🔴 HIGH — Major refactor needed*  
*Estimated effort to stabilize Phase 1: 3–5 days*

# AURIS Implementation Roadmap
## 12-Phase Additive Development Plan

> **Last Updated**: February 21, 2026  
> **Target SDK**: Android 15 (API 35)  
> **Architecture**: Clean Architecture + MVVM + Hilt DI  
> **UI Framework**: Jetpack Compose + Material3  
> **Current Progress**: Phase 1-3 Complete & Polished (98%), Phase 4-5 Ready to Start (→ CRITICAL PATH)

---

## PHASE SUMMARY & DECISION

### Current Status Overview
| Phase | Completion | Status | Production-Ready |
|:---|:---:|:---:|:---:|
| Phase 1: Navigation | 98% | ✅ POLISHED | YES |
| Phase 2: Vitamins | 98% | ✅ POLISHED | YES |
| Phase 3: Animations | 98% | ✅ POLISHED | YES |
| Phase 4: Logging | 85% | ⚠️ IN PROGRESS | NO (Form ready, needs Phase 5 integration) |
| Phase 5: Repository | 0% | ❌ BLOCKED | CRITICAL PATH (START NOW) |
| Phase 6-12 | 0% | ❌ SEQUENTIAL | Blocked by Phase 5 |

### Recommendation: YES, PROCEED TO PHASES 4-6

**Why not complete Phases 1-3 to 100%?**
- Remaining 2% is purely optional polish (complex waveforms, ECG variations)
- Does not block functionality or shipping
- Can be added post-launch as UX improvements
- Phase 5 (Repository Layer) is CRITICAL and unblocks ALL phases 6-12

**Action**: Deploy Phases 1-3 to test devices NOW. Start Phase 5 immediately. Do NOT wait for 100% polish.

---

## Phase 1: Base UI/UX Shell ✅ 98% COMPLETE (POLISHED)
### Foundation Navigation & Home Screen Skeleton

**Status**: 98% complete (Feb 21, 2026) — Layout fixes applied  
**Latest Changes** (Feb 21, 2026):
- ✅ **Greeting positioning fixed**: Changed `padding(top = 40.dp)` → `windowInsetsPadding(WindowInsets.statusBars)` + `padding(top = 24.dp)`
  - Prevents status bar (time + camera bump) from overlaying greeting
  - Matches VitaminsScreen padding for consistency
  - Safe area auto-respected on all device notches
- ✅ **Weight + Readiness/Recovery card sizing fixed**: Both cards now `weight(1f).heightIn(min = 140.dp)`
  - Was: Weight `weight(1f)` (no height), Readiness `weight(1.2f).heightIn(140dp)` (larger)
  - Now: Both same size; perfectly aligned vertical layout
  - ArcStat components have adequate vertical space
- ✅ **Water bar consistency verified**: Fixed 52dp height, proper spacing

**Deviations**: 
- ✅ NAMING: Resolved — Renamed all Sorus* files to Auris* (SorusTheme.kt, SorusBottomNav.kt, SorusNavHost.kt)
- ⚠️ LEGACY: Old Canvas files (DashboardActivity.kt, GlassLiquidCard.kt, etc.) remain in root directory (safe to delete)

**Implemented** ✅

**What to build:**
- ✅ Multi-module Gradle scaffold (app/src/main with feature packages: home, log, diary, overview, profile, vitamins)
- ✅ Version catalog via gradle/libs.versions.toml
- ✅ Hilt DI graph setup (@HiltAndroidApp on AurisApp)
- ✅ AurisTheme with glassmorphism color tokens, Material3-based Typography, GlassCard composable
- ✅ Bottom navigation pill bar with 5 tabs + center FAB (Home, Log, Vitamins, Diary, Profile)
- ✅ HomeScreen with rich UI (greeting, AI search bar, liquid columns, habit cards)
- ✅ MainActivity with bottom nav routing to all 5 feature screens
- ✅ Navigation via Jetpack Compose NavHost + NavController
- ✅ Floating pill nav with active tab indicators

**Key files/modules:**
- ✅ `app/src/main/kotlin/com/auris/MainActivity.kt` + `AndroidManifest.xml`
- ✅ `app/src/main/kotlin/com/auris/ui/theme/AurisTheme.kt`, `Colors.kt`, `Typography.kt`
- ✅ `app/src/main/kotlin/com/auris/ui/components/GlassCard.kt`, `AurisBottomNav.kt`
- ✅ `app/src/main/kotlin/com/auris/feature/home/HomeScreen.kt` (rich dashboard)
- ✅ `app/src/main/kotlin/com/auris/feature/{log,diary,vitamins,profile}/` screens
- ✅ `app/build.gradle.kts`, `build.gradle.kts`, `settings.gradle.kts`

**Estimated effort:** Medium (3–4 days) — COMPLETED

**Prerequisites:** None (project start)

**Success criteria:**
- ✅ App launches in debug on emulator (API 26+)
- ✅ 5-button + FAB floating pill nav visible and clickable
- ✅ Each tab navigates to its screen
- ✅ Home screen displays greeting, AI search bar, liquid columns, habit cards
- ✅ Light iOS 17 theme applied consistently
- ✅ No critical lint errors; Gradle warnings minimal

**Issues to Address Before Phase 5:**
- ✅ RESOLVED: Renamed `SorusTheme.kt` → `AurisTheme.kt`, `SorusBottomNav.kt` → `AurisBottomNav.kt`, `SorusNavHost.kt` → `AurisNavHost.kt`
- ⚠️ Remove legacy Canvas files from root directory

---

## Phase 2: Static Vitamin Cards ✅ 98% COMPLETE (POLISHED)
### Vitals Screen with Hardcoded Sample Data

**Status**: 98% complete (Feb 21, 2026) — All 19 nutrients display correctly  
**Latest Changes** (Feb 21, 2026):
- ✅ Header alignment verified: Matches HomeScreen greeting height (34sp bold, 24dp top padding with statusBars safe area)
- ✅ Grouped list styling consistent: 0.5px separator lines, white background, proper spacing
- ✅ Safe area handling: Uses `windowInsetsPadding(WindowInsets.statusBars)` (notch-aware)

**Deviations**: None — all core functionality present, smooth animations

**What was built:**
- ✅ Vitamins screen UI showing all 19 nutrients in white grouped card layout
- ✅ Each card displays: vitamin name, calculated percentage (12–91%), intake value, animated progress bar
- ✅ VitaminStatus domain model (effectiveIntake, adjustedRda, deficiencyLevel, displayValue)
- ✅ VitaminViewModel with StateFlow<List<VitaminStatus>> using test data factory (fromPercent)
- ✅ All 19 nutrients: A, B1-B9, B12, C, D, E, K, Fe, Ca, Mg, Zn, Protein, Collagen
- ✅ Card rows stack vertically in grouped white container with 0.5px inset separators

**Key files/modules:**
- ✅ `app/src/main/kotlin/com/auris/ui/components/VitaminBarRow.kt` (animated bar, semantic colors)
- ✅ `app/src/main/kotlin/com/auris/domain/model/VitaminStatus.kt`, `NutrientId.kt`
- ✅ `app/src/main/kotlin/com/auris/feature/vitamins/VitaminViewModel.kt` + test data factory
- ✅ `app/src/main/kotlin/com/auris/feature/vitamins/VitaminsScreen.kt` (Column of VitaminBarRow)
- ✅ Bottom nav Overview tab → VitaminsScreen

**Estimated effort:** Low (1–2 days) — COMPLETED

**Prerequisites:** Phase 1 ✅

**Success criteria:**
- ✅ Vitamins screen displays all 19 nutrients in grouped card format
- ✅ Each row shows name + percentage + animated bar (semantic color)
- ✅ ViewModel loads test data on screen composition
- ✅ Cards are scrollable via Column + vertical scroll modifier
- ✅ Animation present (animateFloatAsState on bar, 1200ms tween)

---

## Phase 3: Liquid Animation & Color Coding ✅ 98% COMPLETE (POLISHED)
### Enhance Cards with Wave Animations & Deficiency Levels

**Status**: 98% complete (Feb 21, 2026) — All animations smooth, colors semantic  
**Latest Changes** (Feb 21, 2026):
- ✅ Hero row liquid columns verified: Proper sizing (52dp × 250dp), smooth fill animations, correct gradients
- ✅ RingWidget animations verified: Spring easing on donut progress indicators
- ✅ BodySilhouette centered properly in 3-column hero row (220dp width)

**Deviations**: None — all advanced features working, animations polished

**What was built:**
- ✅ VitaminBarRow upgraded with animateFloatAsState (1200ms tween on mount)
- ✅ DeficiencyLevel enum: OPTIMAL (green, ≥80%), ADEQUATE (blue, 60–79%), LOW (orange, 40–59%), DEFICIENT (red, <40%), CRITICAL (dark red, <15%)
- ✅ Bar color semantically mapped via AurisColors.pctColor() helper
- ✅ LiquidColumn component (vertical tube with animated fill + gradient)
- ✅ LiquidTubeCard component (Canvas-based tube with wave animation via InfiniteTransition, 1.5s period when <80%)
- ✅ LOW deficiency indicator in VitaminBarRow (conditional badge)
- ✅ Implicit stagger via LaunchedEffect + LiquidTubeCard delayMs parameter
- ✅ Test data factory generates all 5 deficiency levels (12%, 22%, 30%, 38%, 45%... 91%)

**Key files/modules:**
- ✅ `app/src/main/kotlin/com/auris/ui/components/VitaminBarRow.kt` (animation + color coding)
- ✅ `app/src/main/kotlin/com/auris/ui/components/LiquidTubeCard.kt` (Canvas tube + wave animation)
- ✅ `app/src/main/kotlin/com/auris/ui/components/LiquidColumn.kt` (liquid fill component)
- ✅ `app/src/main/kotlin/com/auris/ui/theme/Colors.kt` (pctColor() semantic mapper)
- ✅ `app/src/main/kotlin/com/auris/domain/model/DeficiencyLevel.kt`
- ✅ `app/src/main/kotlin/com/auris/feature/vitamins/VitaminViewModel.kt` (test data with all levels)
- ✅ `app/src/main/kotlin/com/auris/feature/vitamins/VitaminsScreen.kt`

**Estimated effort:** Medium (2–3 days) — COMPLETED

**Prerequisites:** Phase 2 ✅

**Success criteria:**
- ✅ Bar fill animates smoothly (1200ms) on mount
- ✅ Cards color-coded per deficiency level (green/blue/orange/red/dark-red)
- ✅ Liquid tube card renders with wave animation (1.5s InfiniteTransition when <80%)
- ⚠️ View toggle: NOT IMPLEMENTED (minor — can be Phase 5 enhancement)
- ✅ Stagger animation visible via delayMs parameter in LiquidTubeCard

**Issues to Address Before Phase 5:**
- ⚠️ Low indicator badge implementation differs slightly from HIG spec (currently text marker, could be chip)

---

## Phase 4: Basic Manual Logging Screen ⚠️ 85% COMPLETE (BLOCKED ON PHASE 5)
### Log Tab with Simple Food Entry Form

**Status**: 85% complete (Feb 21, 2026) — Form works, integration blocked  
**CRITICAL BLOCKER**: Do NOT mark Phase 4 complete until Phase 5 (Repository Layer) is integrated.

**Why Blocked**:
- Logging a meal in LogScreen does NOT update vitamin cards on HomeScreen
- Root cause: No shared repository or inter-screen state management
- LogViewModel is isolated; VitaminViewModel has hardcoded test data
- Expected behavior: Logging "Chicken" (high protein) should animate protein bar on Home
- Status: Form UI works; integration architecture missing

**What was built:**
- ✅ LogScreen with LazyColumn layout (iOS 17 light mode)
- ✅ ManualFoodForm component with all 5 input fields (name, calories, protein, carbs, fat, meal type)
- ✅ Submit button with snackbar confirmation "Meal logged!"
- ✅ Form state management via LogViewModel StateFlow
- ✅ Form reset after submission
- ✅ In-memory food log (session persistence only)

**Deviations**: 
- ❌ **INTEGRATION GAP**: Home screen does NOT consume LogViewModel updates (Phase 5 will fix)
- ⚠️ **ARCHITECTURE**: LogViewModel should inject FoodRepository (Phase 5 prerequisite)
- ⚠️ **PERSISTENCE**: Data lost on app restart (expected for Phase 4)

**What was built:**
- ✅ LogScreen with LazyColumn layout (iOS 17 light mode)
- ✅ ManualFoodForm component with fields:
  - ✅ Text field: "Food name"
  - ✅ Number fields: Calories, Protein (g), Carbs (g), Fat (g)
  - ✅ Dropdown: Meal type (MealType enum: BREAKFAST, LUNCH, DINNER, SNACK)
- ✅ "Add" button triggers Snackbar message "Meal logged!"
- ✅ Form submission updates in-memory StateFlow (foodLog list)
- ✅ Screen supports back navigation via NavController or scaffold structure
- ✅ ParsedFoodItem domain model (name, calories, protein, carbs, fat)

**Key files/modules:**
- ✅ `app/src/main/kotlin/com/auris/feature/log/LogScreen.kt`
- ✅ `app/src/main/kotlin/com/auris/feature/log/components/ManualFoodForm.kt`
- ✅ `app/src/main/kotlin/com/auris/feature/log/LogViewModel.kt` (LogUiState, foodLog, vitaminBoosts StateFlows)
- ✅ `app/src/main/kotlin/com/auris/domain/model/ParsedFoodItem.kt`
- ✅ Snackbar + SnackbarHost in LogScreen

**Estimated effort:** Medium (2–3 days) — COMPLETED

**Prerequisites:** Phase 3 ✅

**Success criteria:**
- ✅ Log tab shows form with all fields visible (iOS 17 white grouped style)
- ✅ Tap "Add" button → Snackbar message appears
- ⚠️ Vitamin card on Home screen: integration not fully verified (needs Phase 5 repository layer)
- ✅ Form clears after submission (LogUiState reset)
- ✅ No Room DB writes (all in-memory StateFlow)

**Issues to Address Before Phase 5:**
- ⚠️ INTEGRATION: Home screen does not currently consume LogViewModel updates. Requires shared repository in Phase 5.
- ⚠️ PERSISTENCE: Food log data is lost on app restart (expected for Phase 4, but verify behavior).

---

---

## Phase 5: Fake Data Layer & State Management ❌ READY TO START (CRITICAL PATH)
### Connect Logging to UI via Repository Pattern

**Status**: 0% (Not started) — **CRITICAL BLOCKER for ALL Phases 6-12**  
**Priority**: 🔴 **START IMMEDIATELY** — prevents Home↔Log integration, blocks database layer, delays all dependent phases  
**Estimated Effort**: Medium (2–3 days)  
**Dependencies**: Phase 4 ✅ (form ready)

**WHY THIS IS CRITICAL**:
```
Current State: LogViewModel logs meals → in-memory StateFlow (isolated)
               HomeViewModel has hardcoded test data (no connection to LogViewModel)
               User logs "Chicken" → vitamin cards don't update (BROKEN)

After Phase 5: LogViewModel injects FoodRepository
               HomeViewModel injects FoodRepository + VitaminRepository
               Shared state via repository pattern
               User logs "Chicken" → protein bar animates on Home (FIXED)
               Ready for Phase 6 (Room DB swap-in)
```

**What must be built:**
1. **FoodRepository interface** (domain/):
   - `suspend fun logFood(item: ParsedFoodItem): Result<Unit>`
   - `fun getTodayFoodLog(): Flow<List<ParsedFoodItem>>`
   - `fun getFoodLogByDate(date: LocalDate): Flow<List<ParsedFoodItem>>`

2. **VitaminRepository interface** (domain/):
   - `fun getTodayVitaminStatus(): Flow<List<VitaminStatus>>`
   - `suspend fun recalculateVitamins(foods: List<ParsedFoodItem>): List<VitaminStatus>`

3. **FakeFoodRepositoryImpl** (data/):
   - In-memory MutableStateFlow of foods
   - logFood() appends to StateFlow
   - getTodayFoodLog() returns Flow of today's items

4. **FakeVitaminRepositoryImpl** (data/):
   - In-memory MutableStateFlow of VitaminStatus
   - Observes FoodRepository changes
   - Recalculates vitamin boosts (heuristic: chicken → +20g protein, etc.)

5. **Hilt bindings** (core-data/di/):
   - @Binds FoodRepository to FakeFoodRepositoryImpl
   - @Binds VitaminRepository to FakeVitaminRepositoryImpl
   - Inject into LogViewModel + HomeViewModel

6. **CalcVitaminStatusUseCase v1** (domain/usecase/):
   - Takes List<ParsedFoodItem>
   - Returns List<VitaminStatus> with macros calculated
   - (Advanced absorption modifiers for Phase 9; basic version for now)

7. **Update ViewModels**:
   - LogViewModel: Call FoodRepository.logFood(), connect to HomeScreen
   - HomeViewModel: Collect from VitaminRepository on init, expose as StateFlow
   - Ensure single source of truth (repository is the state owner)

**Key files/modules:**
- `core-domain/repository/FoodRepository.kt` (interface)
- `core-domain/repository/VitaminRepository.kt` (interface)
- `core-data/repository/FakeFoodRepositoryImpl.kt`
- `core-data/repository/FakeVitaminRepositoryImpl.kt`
- `core-domain/usecase/CalcVitaminStatusUseCase.kt` (v1: basic macros → vitamin proxy)
- `feature-home/HomeViewModel.kt` (update to use repositories)
- `feature-log/LogViewModel.kt` (inject FoodRepository)
- Hilt bindings in `core-data/di/RepositoryModule.kt`

**Estimated effort:** Medium (2–3 days)

**Prerequisites:** Phase 4

**Success criteria:**
- Logging a meal in Log tab updates Home vitamin cards in real-time
- Switching between tabs and back shows persisted (in-memory) state
- HomeViewModel and LogViewModel communicate via repository + usecase
- No Room DB reads/writes yet (all transient StateFlow)
- Lint-clean, testable repository interfaces

---

## Phase 6: Room DB Persistence ❌ NOT STARTED
### Replace Fake Data with SQLCipher-Encrypted Local Storage

**Status**: 0% — **BLOCKED by Phase 5**  
**Priority**: ⚠️ **CRITICAL** — required for data persistence  
**Estimated Effort**: High (4–5 days)

**What to build:**
- SorusDatabase.kt with SQLCipher AES-256 encryption, all entities:
  - UserProfileEntity (name, DOB, gender, height, weight, calorie goal, goals for P/C/F, preferences)
  - FoodEntryEntity (food name, macros, source, meal type, logged timestamp, confirmed flag)
  - VitaminLogEntity (daily nutrient intake, effective intake after modifiers, RDA, percent, deficiency level)
  - NutritionReferenceEntity (nutrient ID, name, RDA by gender, unit)
  - WellbeingLogEntity, HabitEntity (stubs for now, will populate in later phases)
- DAOs for each entity (FoodEntryDao, VitaminLogDao, NutritionReferenceDao, UserProfileDao)
- Replace FakeFoodRepositoryImpl and FakeVitaminRepositoryImpl with RoomFoodRepositoryImpl, RoomVitaminRepositoryImpl
- Populate NutritionReferenceEntity on first app launch (19-nutrient seed data)
- On log submission: insert FoodEntryEntity + compute VitaminLogEntity row → insert both atomically
- getTodayFoodLog() and getTodayVitaminStatus() now query from DB
- Add DB view migration support in build.gradle (exportSchema=true)

**Key files/modules:**
- `core-data/database/SorusDatabase.kt`
- `core-data/entity/UserProfileEntity.kt`, `FoodEntryEntity.kt`, `VitaminLogEntity.kt`, `NutritionReferenceEntity.kt`, `WellbeingLogEntity.kt`
- `core-data/dao/FoodEntryDao.kt`, `VitaminLogDao.kt`, `NutritionReferenceDao.kt`, `UserProfileDao.kt`
- `core-data/repository/RoomFoodRepositoryImpl.kt`, `RoomVitaminRepositoryImpl.kt`
- `core-data/database/seed/NutritionReferenceSeed.kt` (19-nutrient data with RDA values)
- `core-data/di/DatabaseModule.kt` (Hilt binding for SorusDatabase, DAO injection)
- Onboarding flow updated to write UserProfileEntity on first launch

**Estimated effort:** High (4–5 days)

**Prerequisites:** Phase 5

**Success criteria:**
- App launches and creates encrypted Room DB
- Logging a meal persists to DB
- App restart retrieves yesterdaylog entries from DB
- Home + Dairy screens show persistent data across restarts
- No lint warnings, Room compiler happy
- Nutrition reference table seeded with all 19 nutrients on first launch

---

## Phase 7: Voice Logging ❌ NOT STARTED
### Speech-to-Text Food Entry Capture

**Status**: 0% — **BLOCKED by Phase 6**  
**Priority**: Medium  
**Estimated Effort**: Medium (3–4 days)

**What to build:**
- Add mic FAB button to Log screen
- Long-press or tap mic → launches Android SpeechRecognizer
- Listens for phrases like "I ate half an orange" or "two eggs"
- ParseVoiceInputUseCase (regex + local USDA nutrient lookup) extracts food name + quantity
- Returns ParsedFoodItem with estimated macros (via in-app USDA micronutrient lookup database)
- ConfirmationBottomSheet pre-fills result (editable fields for user review)
- User confirms → same flow as manual logging (inserts to DB)
- Ambiguous inputs trigger NeedsManualReview state with multi-option picker
- SpeechRecognizer availability check before rendering mic button (API gate for unavailable devices)

**Key files/modules:**
- `core-domain/usecase/ParseVoiceInputUseCase.kt`
- `feature-log/VoiceLogService.kt` (SpeechRecognizer wrapper)
- `feature-log/components/ConfirmationBottomSheet.kt` (pre-filled form for user review)
- `core-data/usda/UsdaNutrientLookup.kt` (in-app DB or static map of common foods → macros)
- Update `feature-log/LogScreen.kt` to add mic FAB + voice flow
- `feature-log/LogViewModel.kt` (add voice parsing state flow)

**Estimated effort:** Medium (3–4 days)

**Prerequisites:** Phase 6

**Success criteria:**
- Mic button visible and tappable (or long-press)
- Recordrecognized speech (device SpeechRecognizer)
- ParseVoiceInputUseCase extracts food + quantity regex
- ConfirmationBottomSheet appears with pre-filled editable fields
- User confirms → food logged to DB with source="voice"
- Ambiguous input ("apple") shows multi-option picker instead of guessing

---

## Phase 8: Camera + Zero-Cost AI Food Analysis ❌ NOT STARTED
### Image Capture & Intent-Based AI Routing (Gemini/ChatGPT)

**Status**: 0% — **BLOCKED by Phase 7**  
**Priority**: High (core differentiation feature)  
**Estimated Effort**: High (5–6 days)

**What to build:**
- Add camera FAB button to Log screen
- Tap → CameraX preview (full-screen or modal)
- Capture photo → compress to max 1920×1080, 85% JPEG quality
- BuildPromptUseCase generates v1 prompt (meal type inference + nutrition request)
- AICoreAvailabilityChecker detects if Gemini Nano available (AICore API)
  - YES: Send via AICore to on-device Gemini Nano (offline) → ParseDeepLinkUseCase (no user action)
  - NO: Check if ChatGPT or Gemini installed (PackageManager.queryIntentActivities)
    - YES: Send photo via Intent.ACTION_SEND with prompt text → app responds with sorus:// deep link
    - NO: Show "Install ChatGPT or Gemini" banner + fallback to manual/voice
- Deep link handler in MainActivity.onNewIntent() receives sorus://log URI
- ParseDeepLinkUseCase parses URI (meal type + food items, macros) from AI response
- ParseSharedTextUseCase (regex fallback) if plain text received instead of formatted link
- ConfirmationBottomSheet displays parsed items (editable + review flags for high-cal items >2000 kcal)
- User confirms → LogFoodUseCase saves to DB with source="ai_deeplink", confirmed=true, aiRawResponse text stored (30-day purge job in later phase)

**Key files/modules:**
- `core-domain/usecase/BuildPromptUseCase.kt` (meal type + v1 prompt template)
- `core-domain/usecase/ParseDeepLinkUseCase.kt` (URI parsing, validation, bounds checking)
- `core-domain/usecase/ParseSharedTextUseCase.kt` (regex fallback for plain text)
- `feature-log/ai/AICoreAvailabilityChecker.kt` (AICore detection)
- `feature-log/ai/AiAppInstallDetector.kt` (PackageManager queries for ChatGPT/Gemini)
- `feature-log/CameraViewModel.kt` (state machine: idle → capturing → processing → awaiting_response → confirmed)
- `feature-log/components/CameraPreview.kt` (CameraX + capture button)
- `feature-log/components/ConfirmationBottomSheet.kt` (update with AI-parsed items + review flags)
- `app/MainActivity.kt` (onNewIntent deep link handler + DataStore queue for inter-process save)
- `core-data/entity/FoodEntryEntity.kt` (add aiRawResponse, source fields)
- `core-domain/usecase/LogFoodUseCase.kt` (write to DB with confirmation + source)
- `AndroidManifest.xml` (intent-filter for sorus:// deep link, android:launchMode="singleTask")

**Estimated effort:** High (5–6 days)

**Prerequisites:** Phase 7

**Success criteria:**
- Camera opens on tap, captures photo, compresses to safe size
- Builds correct prompt with meal type + nutrition request
- On Gemini-Nano-capable device: photo analyzed offline, no user action, deep link auto-parsed
- On external AI device (ChatGPT/Gemini): Intent routes to app, user taps response to return, deep link parsed and confirmed
- Fallback to manual/voice if no AI available
- ConfirmationBottomSheet editable + review flags for high-cal items
- Confirmed meal persisted to DB with source="ai_deeplink"
- Deep link URI scheme validated (v=1, required fields present, bounds checked)

---

## Phase 9: Absorption Modifiers & Predictive Alerts ❌ NOT STARTED
### Smart Nutrient Intake Calculation & Nightly Nudges

**Status**: 0% — **BLOCKED by Phase 6 + Phase 8**  
**Priority**: High (defines app intelligence)  
**Estimated Effort**: High (4–5 days)

**What to build:**
- Enhance CalcVitaminStatusUseCase to apply absorption modifier rules:
  - Vitamin A + fat-containing meal: +20% effective absorption
  - Vitamin D + magnesium same day: +15% to Vitamin D effective
  - Iron + Vitamin C same meal: +25% Iron effective
  - Calcium + Vitamin D together: +10% Calcium
  - Zinc + high-phytate meal (beans/grains): -30% Zinc effective
- VitaminLogEntity stores both rawIntake and effectiveIntake
- ApplyBurnAdjustmentsUseCase (from Health Connect later phase) multiplies RDA:
  - Active calories burned >600 kcal: RDA ×1.20 (nutrient demand up 20%)
  - Active calories >300: RDA ×1.10
  - Sleep <6 hours: B-vitamin RDA ×1.15 (recovery cost)
- PredictiveNudgeUseCase runs nightly via WorkManager:
  - Analyzes 3-day nutrient trend per vitamin
  - Generates next-day prediction: "Vitamin C on track (62%)" or "Iron trending low, lentils recommended"
  - Surfaces highest-risk nutrient as center card on Home screen
  - Posts NotificationCompat to KAIROS_VITAMIN_ALERTS channel
- WellbeingLogEntity correlation (phase later) feeds back into nudge messaging ("You felt great yesterday with this nutrient profile")

**Key files/modules:**
- `core-domain/usecase/CalcVitaminStatusUseCase.kt` (update with absorption modifiers)
- `core-domain/usecase/ApplyBurnAdjustmentsUseCase.kt` (placeholder: accepts List<VitaminStatus>, will integrate HC data later)
- `core-domain/usecase/PredictiveNudgeUseCase.kt` (3-day trend analysis, nudge message generation)
- `core-data/worker/PredictiveNudgeWorker.kt` (nightly scheduled, triggers UseCase + posts notification)
- `feature-home/HomeViewModel.kt` (display highest-risk nutrient as center card + update on daily schedule)
- `core-data/di/WorkerModule.kt` (Hilt bindings for WorkManager)
- `AndroidManifest.xml` (add PERMISSION_SCHEDULE_EXACT_ALARM for nightly nudge scheduling)

**Estimated effort:** High (4–5 days)

**Prerequisites:** Phase 6, Phase 8 (for realistic food data)

**Success criteria:**
- Logging meal with fat + Vitamin A shows increased effective intake percentage
- Logging Zinc with beans shows decreased effective intake
- Home screen displays center "risk nutrient" card with prediction
- Nightly WorkManager runs (adb logcat | grep PredictiveNudge)
- Notification posted to KAIROS_VITAMIN_ALERTS channel with nudge message
- VitaminLogEntity stores both raw and effective intake (verifiable in DB)
- Modifier calculations are deterministic and testable (Unit tests for each modifier rule)

---

## Phase 10: Health Connect Integration ❌ NOT STARTED
### Read Activity Data & Adjust Nutrient Demands

**Status**: 0% — **BLOCKED by Phase 9**  
**Priority**: Medium  
**Estimated Effort**: High (4–5 days)

**What to build:**
- Request Health Connect permissions (READ_STEPS, READ_SLEEP, READ_HEART_RATE, READ_ACTIVE_CALORIES_BURNED, READ_WEIGHT, optionally WRITE_NUTRITION + WRITE_HYDRATION)
- PermissionFlowViewModel guides user through Android Health Connect permissions UI
- On app foreground (lifecycle RESUMED), SyncViewModel triggers SyncHealthConnectUseCase
- SyncHealthConnectUseCase reads:
  - Today's steps, active calories, resting HR
  - Last night's sleep duration
  - Current weight (for goal calorie adjustment)
- ApplyBurnAdjustmentsUseCase integrates data:
  - >600 kcal burned today → RDA multiplier ×1.20
  - <6 hours sleep last night → B-vitamin RDA ×1.15
  - Weight change >2kg → recalculate calorie goal
- Optional: After user confirms food log, write NutritionRecord to Health Connect (WRITE_NUTRITION) if toggle enabled in Profile
- PermissionRevocationBanner shown if previously granted permissions now revoked
- Health sync failures silently disable themselves (non-blocking, all other app features work)

**Key files/modules:**
- `core-domain/usecase/SyncHealthConnectUseCase.kt`
- `core-data/health/HealthConnectClient.kt` + wrapper for HealthConnectClient API
- `feature-home/SyncViewModel.kt` (triggers sync on resume + error handling)
- `feature-profile/PermissionFlowViewModel.kt` (Health Connect permission request UI)
- `feature-profile/components/PermissionRevocationBanner.kt`
- `core-data/worker/HealthConnectSyncWorker.kt` (optional background periodic sync every 15 min)
- `feature-profile/ProfileScreen.kt` (add Health Connect permission toggle, Health Connect status indicator)
- `core-domain/usecase/ApplyBurnAdjustmentsUseCase.kt` (update to accept Health Connect data)
- `AndroidManifest.xml` (add Health Connect read/write permissions)

**Estimated effort:** High (4–5 days)

**Prerequisites:** Phase 9

**Success criteria:**
- App requests Health Connect permissions on Profile screen
- User grants permissions → Sorus reads steps, calories, sleep, weight
- RDA multipliers applied correctly (verifiable in VitaminLogEntity adjustedRda field)
- Foreground sync works: watch Home screen vitamin percentages update when Health Connect data changes
- Optional nutrition write works: toggle enabled → logged meal syncs to Health Connect NutritionRecord
- Permission revocation handled gracefully: banner shown, Health Connect disabled, app otherwise functional
- No crashes if HealthConnectClient unavailable (older Android versions or ROM without Health Connect)

---

## Phase 11: Trend Graphs & Habit Tracker ❌ NOT STARTED
### 7-Day Nutrient Charts + Habit Streaks, Badges & Heatmap

**Status**: 0% — **BLOCKED by Phase 6 + Phase 9**  
**Priority**: Medium (analytics/gamification)  
**Estimated Effort**: High (5–6 days)

**What to build:**

**Sub-phase 11A: Trend Graphs**
- Overview screen displays 19 nutrient sparkline charts (Vico library)
- Each chart shows 7-day daily percentage history (day 1–7 on X, % of RDA on Y)
- Chart line color matches current nutrient's DeficiencyLevel (teal=optimal, green=adequate, amber=low, orange=deficient, red=critical)
- Stats summary below charts: weekly average, best/worst day, protein % vs. goal
- Pull data from VitaminLogEntity daily aggregates (Room query: SELECT date, percent GROUP BY date ORDER BY date DESC LIMIT 7)

**Sub-phase 11B: Habit Tracker**
- New Habit CRUD feature (add, edit, delete habits)
- Each Habit: name, icon, color, recurrence (DAILY | WEEKDAYS | WEEKENDS | CUSTOM), optional reminder time, streak counters (current, best)
- HabitEntity + HabitCompletionEntity in Room
- Tap "+" → add new habit modal (name, icon picker, recurrence, reminder time)
- Habit list shows each habit with streak counter (e.g., "Exercise - 12 day streak")
- Long-press habit → check mark animation + confetti burst (3 particles)
- HabitCompletionEntity row inserted on completion
- HabitStreakWorker runs nightly (scheduled at midnight):
  - For each habit with recurrence matching today: check if completion row exists
  - YES: streak_current++, update habit
  - NO: streak_current = 0, update habit
  - If streak_current > streak_best: streak_best = streak_current
- Award badges:
  - 🔥 First Flame: 3-day streak
  - 💎 Diamond: 30-day streak
  - 🌟 Centurion: 100-day streak
  - 📅 Consistent: 7/7 days all habits
- Habit heatmap: 52×7 grid of 12dp squares (weeks on Y, days on X)
  - Color: #00E0FF at varying alpha (0.15 + completion_ratio × 0.85)
  - Empty days: #2A2A2A
  - Rendered as LazyVerticalGrid in Compose

**Key files/modules:**
- `core-ui/components/NutrientTrendChart.kt` (Vico chart wrapper)
- `feature-overview/OverviewScreen.kt` (aggregate charts + stats summary)
- `feature-overview/OverviewViewModel.kt` (load 7-day nutrient data from DB, format for charts)
- `core-domain/model/HabitStatus.kt`, `HabitDto.kt`
- `core-data/entity/HabitEntity.kt`, `HabitCompletionEntity.kt`, `UserBadgeEntity.kt`
- `core-data/dao/HabitDao.kt`, `HabitCompletionDao.kt`, `UserBadgeDao.kt`
- `feature-habits/HabitScreen.kt` (CRUD list, detail, completion tap)
- `feature-habits/HabitViewModel.kt` (CRUD logic, completion, badge check)
- `feature-habits/components/HabitAddModal.kt` (new habit form)
- `feature-habits/components/HabitHeatmap.kt` (52×7 grid renderer)
- `core-data/worker/HabitStreakWorker.kt` (nightly streak calculation + badge awarding)
- `core-domain/usecase/CalcHabitStreakUseCase.kt`, `AwardBadgeUseCase.kt`
- `core-ui/components/ConfettiAnimation.kt` (bursting particles on habit complete)

**Estimated effort:** High (5–6 days)

**Prerequisites:** Phase 9 (for nutrient data), Phase 6 (for DB)

**Success criteria:**
- Overview screen displays 7 nutrient charts with correct historical data
- Chart lines color-coded per deficiency level
- Stats summary accurate (weekly avg, best/worst day, protein tracking)
- Habit list shows all habits with current streak count
- Long-press habit → confetti + check mark animation, streak increments next day after midnight
- Habit heatmap renders 52×7 grid with correct color intensity per week
- Badges awarded and displayed in Profile screen when earned
- HabitStreakWorker runs nightly (verifiable via logcat)
- Streak resets correctly when habit missed (no completion row for scheduled day)

---

## Phase 12: Advanced Smart Features ❌ NOT STARTED
### Food Swaps, Feedback Loop, Doctor Export & Local Encrypted Backup

**Status**: 0% — **BLOCKED by all prior phases**  
**Priority**: Low (polish)  
**Estimated Effort**: High (5–6 days)

**What to build:**

**Sub-phase 12A: Food Swaps & Feedback Loop**
- Tap vitamin bar on Home/Vitamins screen when LOW or DEFICIENT (<60%) → FoodSwapsBottomSheet slides up
- Sheet displays:
  - Reason: "No leafy greens today — Vitamin K at 28%"
  - 3 food suggestions (pulled from local USDA DB filtered by that nutrient + calorie-matched)
  - "Quick-add" button for each → adds to today's log without re-confirmation
- Nightly card on Home screen (appears at 9 PM): "How's your energy today?" with 😴 Low, 🙂 Okay, ⚡ Great buttons
- Response stored in WellbeingLogEntity
- PredictiveNudgeUseCase correlates wellbeing scores with 7-day vitamin profiles → adjusts nudge messaging

**Sub-phase 12B: Doctor Export — 30-Day PDF**
- Profile screen → "Export Report" button
- GenerateExportPDFUseCase (iText7) generates PDF:
  - Header: User name, age, date range
  - Calorie + macro summary table (7-day rolling averages)
  - Vitamin status table: nutrient, avg %, RDA, trend arrow (↑↓→)
  - Wellbeing log (daily energy scores with date)
  - Notable alerts (days with critical deficiencies)
  - Footer disclaimer
- PDF saved to app-private external storage (getExternalFilesDir)
- Share via FileProvider + Intent.ACTION_SEND → user picks email/cloud storage
- PDF covers 30 days prior to export date

**Sub-phase 12C: Local Encrypted Backup & Restore**
- Profile screen → "Backup" button
- ExportBackupUseCase:
  - Serialize entire Room DB as JSON (Kotlin Serialization)
  - Encrypt with AES-256-GCM (key from Android Keystore)
  - User picks destination via Intent.ACTION_CREATE_DOCUMENT
  - Save as .sorusbackup file
- Profile screen → "Restore from Backup" button
- ImportBackupUseCase:
  - User picks .sorusbackup file via Intent.ACTION_OPEN_DOCUMENT
  - Decrypt + deserialize JSON
  - Validate schema (check entity counts, non-null fields)
  - Show confirmation: "Restore will replace all data. Continue?"
  - Drop all DB tables + insert restored entities
  - Restart app or refresh all ViewModels to sync state

**Key files/modules:**
- `feature-vitamins/components/FoodSwapsBottomSheet.kt` (suggestion grid, quick-add buttons)
- `core-domain/usecase/GetFoodSwapsUseCase.kt` (query USDA DB by nutrient, filter by calories)
- `core-data/usda/UsdaFoodSuggestions.kt` (in-app food suggestions DB)
- `feature-home/components/WellbeingFeedbackCard.kt` (nightly "How do you feel?" card)
- `core-data/entity/WellbeingLogEntity.kt` (update entity if needed)
- `core-domain/usecase/GenerateExportPDFUseCase.kt` (iText7 report generation)
- `feature-profile/ProfileViewModel.kt` (trigger export, handle file intent)
- `core-domain/usecase/ExportBackupUseCase.kt`, `ImportBackupUseCase.kt`
- `core-data/backup/BackupSerializer.kt` (JSON serialization of all entities)
- `core-data/backup/BackupEncryption.kt` (AES-256-GCM using Android Keystore)
- `feature-profile/ProfileScreen.kt` (add Export, Backup, Restore buttons)
- `core-data/worker/PurgeOldResponsesWorker.kt` (30-day aiRawResponse cleanup, scheduled weekly)

**Estimated effort:** High (5–6 days)

**Prerequisites:** Phase 6 (DB), Phase 9 (PredictiveNudge), Phase 11 (wellbeing correlation)

**Success criteria:**
- Tapping LOW/DEFICIENT vitamin bar shows FoodSwapsBottomSheet with 3 suggestions + quick-add
- Nightly wellbeing card appears on Home screen at 9 PM (or on demand in settings)
- Wellbeing response stored in DB and correlated with nudge messaging (testable in nudge message)
- "Export Report" button generates PDF in <5 seconds, saved to app-private storage
- PDF contains all sections (header, macros, vitamins, wellbeing, alerts, footer); PDF opens correctly in external viewer
- FileProvider shares PDF via email/cloud
- "Backup" button exports encrypted .sorusbackup file to user-selected location
- "Restore" button imports .sorusbackup, validates schema, confirms with user, replaces DB, app restarts cleanly
- PurgeOldResponsesWorker removes aiRawResponse older than 30 days (weekly job, verifiable in DB)

---

## Implementation Notes

### Additive Workflow
- Each phase extends existing Composables, ViewModels, and DB schema — no rewriting or replacing earlier phases
- Phases 1–6 form solid foundation (UI + persistence)
- Phases 7–8 unlock AI logging (voice + camera)
- Phases 9–10 add intelligence (absorption, burn, Health Connect)
- Phases 11–12 polish and integrate (analytics, habits, export, backup)

### Key Design Constraints
- **No INTERNET permission in V3**: All AI via user's installed apps (Intent routing)
- **Offline-first**: Room DB is source of truth; Health Connect sync is optional enhancement
- **Privacy**: No PII stored (UUID only); encrypted backup keeps user in control
- **Confirmation gating**: All AI-sourced data requires explicit user tap before saving

### Testing at Each Phase
- Unit tests for UseCase logic (parsing, calculations, modifiers)
- Integration tests for DB round-trips (write → read → assert)
- UI tests for Compose screens (button presence, state flow updates)
- Device tests on API 33–35 for edge cases (permission flows, Health Connect variation)

### Key Milestones
- **Phase 6 complete**: Alpha-ready (manual logging + persistence)
- **Phase 8 complete**: MVP ready (AI logging + voice + camera)
- **Phase 10 complete**: Beta-ready (smart nutrition + Health Connect)
- **Phase 12 complete**: Production-ready (export, backup, advanced features)

---

## Estimated Timeline

| Phases | Duration | Checkpoint |
|:---|:---|:---|
| 1–3 | 1–2 weeks | UI/UX shell + animated cards |
| 4–6 | 2–3 weeks | Manual + voice logging + DB |
| 7–8 | 2 weeks | Full AI food logging |
| 9–10 | 2–3 weeks | Smart nutrients + Health Connect |
| 11–12 | 2–3 weeks | Analytics + export + habits |
| **Total** | **9–13 weeks** | Full production app ready for Play Store |

---

*Prepared by: Android Engineering Lead*  
*Based on Architecture.md (Feb 2026)*  
*Reference: VitalTrack_AI_InterApp_Connectivity.md for deep-link specs*

# AURIS — Android Health Dashboard

> Dark-theme AI-assisted nutrient tracking app. Apple Health aesthetic meets Material 3.
> Smooth spring/decelerate animations throughout. Colour palette: teal, blue, green, amber, charcoal — no red/green drama.

---

## Current Status — Phases 1–12 Under Development 🚧

All core infrastructure, data persistence, Health Connect integration, AI food analysis pipeline, and dashboard UI are in development state.
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/10846093-968b-4807-a6bc-048c79553b16" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/08bf822e-05fc-4068-8525-3a0cf2001fba" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/64b17004-0013-459d-ae93-fdb9ab365e01" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/e818fd2b-0dcc-438c-b0b6-1c770e0841d1" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/bbba98d6-135f-4ccc-a280-b9049d5beabf" />


## Project Structure

```
app/src/main/kotlin/com/auris/
│
├── AurisApp.kt                          ← @HiltAndroidApp Application
├── MainActivity.kt                      ← Compose host, deep-link handler
│
├── navigation/
│   ├── Screen.kt                        ← Sealed route hierarchy
│   └── AurisNavHost.kt                  ← NavHost + AurisBottomNav
│
├── ui/
│   ├── theme/
│   │   ├── Colors.kt (AurisColors)      ← Design tokens
│   │   ├── Typography.kt
│   │   └── AurisTheme.kt
│   └── components/
│       ├── GlassCard.kt
│       ├── AurisBottomNav.kt
│       ├── VitaminBarRow.kt
│       ├── LiquidTubeCard.kt
│       ├── NutrientTrendChart.kt
│       ├── PredictiveNudgeCard.kt
│       ├── WellbeingFeedbackCard.kt
│       ├── FoodSwapsBottomSheet.kt
│       ├── ConfirmationBottomSheet.kt
│       └── CameraPreview.kt
│
├── domain/
│   ├── model/
│   │   ├── NutrientId.kt                ← 19 tracked nutrients + RDA
│   │   ├── VitaminStatus.kt             ← percentFraction, deficiencyLevel
│   │   ├── DeficiencyLevel.kt           ← 5-tier classification
│   │   ├── ParsedFoodItem.kt + MealType
│   │   ├── Habit.kt + HabitRecurrence.kt
│   │   └── NutrientTrendSeries.kt
│   ├── repository/
│   │   ├── FoodRepository.kt
│   │   ├── VitaminRepository.kt
│   │   ├── HabitRepository.kt
│   │   └── HealthConnectRepository.kt
│   └── usecase/
│       ├── CalcVitaminStatusUseCase.kt
│       ├── ApplyBurnAdjustmentsUseCase.kt
│       ├── PredictiveNudgeUseCase.kt
│       ├── BuildPromptUseCase.kt
│       ├── ParseVoiceInputUseCase.kt
│       ├── ParseDeepLinkUseCase.kt
│       ├── ParseSharedTextUseCase.kt
│       ├── LogFoodUseCase.kt
│       ├── SyncHealthConnectUseCase.kt
│       ├── ExportBackupUseCase.kt
│       ├── ImportBackupUseCase.kt
│       └── GenerateExportPDFUseCase.kt
│
├── data/
│   ├── database/
│   │   ├── AurisDatabase.kt             ← Room v3, SQLCipher encrypted
│   │   ├── Converters.kt
│   │   └── seed/NutritionReferenceSeed.kt
│   ├── dao/
│   │   ├── FoodEntryDao.kt
│   │   ├── VitaminLogDao.kt
│   │   ├── HabitDao.kt + HabitCompletionDao.kt
│   │   ├── WellbeingLogDao.kt
│   │   ├── NutritionReferenceDao.kt
│   │   └── UserBadgeDao.kt
│   ├── entity/
│   │   ├── FoodEntryEntity.kt
│   │   ├── VitaminLogEntity.kt
│   │   ├── HabitEntity.kt + HabitCompletionEntity.kt
│   │   ├── WellbeingLogEntity.kt
│   │   ├── DailyLogEntity.kt
│   │   ├── UserProfileEntity.kt
│   │   ├── UserBadgeEntity.kt
│   │   └── NutritionReferenceEntity.kt
│   ├── repository/
│   │   ├── RoomFoodRepositoryImpl.kt    ← Live Room-backed food log
│   │   ├── RoomVitaminRepositoryImpl.kt ← Live Room-backed vitamins
│   │   ├── FakeFoodRepositoryImpl.kt    ← In-memory (preview/test)
│   │   ├── FakeVitaminRepositoryImpl.kt ← Pre-seeded dummy nutrients
│   │   ├── HabitRepositoryImpl.kt
│   │   └── health/HealthConnectRepositoryImpl.kt
│   ├── worker/
│   │   ├── HealthConnectSyncWorker.kt
│   │   ├── PredictiveNudgeWorker.kt
│   │   └── HabitStreakWorker.kt
│   ├── usda/UsdaNutrientLookup.kt
│   └── di/
│       ├── DatabaseModule.kt
│       ├── RepositoryModule.kt
│       └── WorkerModule.kt
│
└── feature/
    ├── home/
    │   ├── HomeScreen.kt                ← Dashboard: steps, sleep, HR, water,
    │   │                                    macros, readiness, recovery, vitamins
    │   └── HomeViewModel.kt
    ├── log/
    │   ├── LogScreen.kt
    │   ├── LogViewModel.kt
    │   └── components/ManualFoodForm.kt
    ├── vitamins/
    │   ├── VitaminsScreen.kt            ← 19-nutrient list/grid with real data
    │   └── VitaminViewModel.kt
    ├── overview/
    │   ├── OverviewScreen.kt            ← Trend charts
    │   └── OverviewViewModel.kt
    ├── habits/
    │   ├── HabitScreen.kt
    │   └── HabitViewModel.kt
    ├── diary/DiaryScreen.kt
    ├── profile/
    │   ├── ProfileScreen.kt
    │   └── ProfileViewModel.kt
    ├── sync/SyncViewModel.kt
    ├── permissions/PermissionFlowViewModel.kt
    └── voice/VoiceLogService.kt
```

---

## Dashboard — Current Dummy Values (until HC/food data syncs)

| Card | Value shown |
|---|---|
| Steps | 6,500 / 10,000 (65% ring) |
| Sleep | 7h 30m |
| Heart Rate | Half-fill ECG waveform + blinking red live dot |
| Water | 70% full — 8dp thick animated liquid shimmer bar |
| Readiness | 57 — 220° moon-arc (yellow) |
| Recovery | 77 — 220° moon-arc (green) |
| Protein / Carbs / Fat | 100g / 75g / 22g |
| Vitamins (top 4) | Pre-seeded from 19 nutrients (27–91% fill) |

Real Health Connect data and logged food override these automatically.

---

## Health Connect Setup

The app requests the following HC permissions at runtime:

- `READ_STEPS`, `READ_TOTAL_CALORIES_BURNED`, `READ_ACTIVE_CALORIES_BURNED`
- `READ_SLEEP`, `READ_HEART_RATE`, `READ_WEIGHT`

Requires Health Connect app installed (Android 9+). On Android 14+ it is built in.

Declare in `AndroidManifest.xml`:
```xml
<queries>
    <package android:name="com.google.android.apps.healthdata" />
</queries>
```

---

## Build Setup

```toml
# gradle/libs.versions.toml
agp              = "8.9.0"
kotlin           = "2.0.21"
compose-bom      = "2025.02.00"
hilt             = "2.51.1"
room             = "2.6.1"
healthConnect    = "1.1.0"
workManager      = "2.11.1"
```

```kotlin
// app/build.gradle.kts
compileSdk = 36
targetSdk  = 36
minSdk     = 26
```

**Sync:** File → Sync Project with Gradle Files in Android Studio.
Open `AURIS-ANDROID/` as the project root.

---

## Git Notes

- `.gitignore` covers `build/`, `.gradle/`, `*.hprof`, `local.properties`, `*.jks`
- Never use `git add .` — always stage specific files to avoid committing build outputs
- Before pushing: `git status` → `git diff --cached --stat` → `git push`

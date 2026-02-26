# AURIS — Android Health Dashboard

> **Personalized nutrient tracking with zero-cost AI food analysis.**  
> Log food via photos or voice, auto-track 19 micronutrients, sync with Health Connect. All data encrypted locally—no cloud required.

**Platform:** Android 8.0+ (API 26+)  
**Language:** Kotlin · **UI:** Jetpack Compose · **Architecture:** Clean Architecture + MVVM  
**USP:** Zero-cost AI via ChatGPT/Gemini Intent routing + offline-first + privacy-first

---

## Current Status — Phases 1–12 Under Development 🚧

All core infrastructure, data persistence, Health Connect integration, AI food analysis pipeline, and dashboard UI are in development state.
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/10846093-968b-4807-a6bc-048c79553b16" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/08bf822e-05fc-4068-8525-3a0cf2001fba" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/64b17004-0013-459d-ae93-fdb9ab365e01" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/e818fd2b-0dcc-438c-b0b6-1c770e0841d1" />
<img width="128" height="285" alt="image" src="https://github.com/user-attachments/assets/bbba98d6-135f-4ccc-a280-b9049d5beabf" />

---

## Architecture Overview

### Core Design Pattern

AURIS follows **Clean Architecture + MVVM** with **Repository** abstraction and **Hilt** dependency injection.

```
┌─────────────────────────────────────────────────────────────┐
│                    UI LAYER (Jetpack Compose)               │
│  HomeScreen · VitaminsScreen · CameraScreen · DiaryScreen   │
└──────────────────────┬──────────────────────────────────────┘
                       │ StateFlow / collectAsState
┌──────────────────────┴──────────────────────────────────────┐
│              PRESENTATION LAYER (ViewModels)                │
│  HomeViewModel · VitaminViewModel · LogViewModel · etc.     │
└──────────────────────┬──────────────────────────────────────┘
                       │ invoke()
┌──────────────────────┴──────────────────────────────────────┐
│              DOMAIN LAYER (Use Cases)                       │
│  LogFoodUseCase · ParseDeepLinkUseCase · CalcVitaminStatus  │
│  PredictiveNudgeUseCase · SyncHealthConnectUseCase · etc.   │
└──────────────────────┬──────────────────────────────────────┘
                       │ calls
┌──────────────────────┴──────────────────────────────────────┐
│           DATA LAYER (Repositories)                         │
│  FoodRepository · VitaminRepository · HabitRepository       │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────┴──────────────────────────────────────┐
│        LOCAL DATABASE & EXTERNAL SERVICES                   │
│  Room DB (SQLCipher) · Health Connect · Intent System       │
└─────────────────────────────────────────────────────────────┘
```

### Data Flow

1. **User Action** (tap, voice, camera) → UI event
2. **ViewModel** receives event → invokes **UseCase**
3. **UseCase** orchestrates business logic → calls **Repository**
4. **Repository** reads/writes to **Room DB** or **Health Connect**
5. **Emits Flow<T>** → ViewModel collects → updates **StateFlow**
6. **Compose** observes StateFlow → **recomposes UI**

### Key Features at a Glance

| Feature | Tech | Flow |
|:---|:---|:---|
| **AI Food Analysis** | Intent → ChatGPT/Gemini | Scan → AI responds → Deep link → LogFoodUseCase → Room |
| **Voice Logging** | SpeechRecognizer | Speak → ParseVoiceInputUseCase → USDA lookup → Confirm → Save |
| **Nutrient Tracking** | Room + LiveData | Food logged → CalcVitaminStatusUseCase → 19 nutrients tracked |
| **Health Sync** | Health Connect API | WorkManager triggers sync → HealthConnectRepository → Room |
| **Habit Streaks** | Room + WorkManager | Habit logged → HabitStreakWorker checks → updates badge |

---

## Project Structure

<details>
<summary><b>📁 Detailed Folder Layout (click to expand)</b></summary>

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

</details>

---

## AI Food Analysis — Zero-Cost Flow

AURIS uniquely leverages existing user-owned AI apps (ChatGPT/Gemini) instead of expensive Vision APIs.

```
📸 Capture Food → Intent.ACTION_SEND → ChatGPT/Gemini → Analyzes image
                                             ↓
                                    Returns deep link: sorus://log?...
                                             ↓
                                    User taps → ParseDeepLinkUseCase
                                             ↓
                                    ConfirmationBottomSheet (review)
                                             ↓
                                    LogFoodUseCase → Room DB → Nutrients tracked
```

**Fallback Chain:**
1. Gemini Nano (on-device) — 0 actions  
2. ChatGPT/Gemini Intent → deep-link → 1 tap  
3. Voice log → ParseVoiceInputUseCase → USDA lookup  
4. Manual form → offline USDA nutrition DB  

---

## Key Workflows

### Adding Food

**Scan → Confirm → Save**
- Open camera, capture food
- AI identifies nutrients (cal, protein, carbs, fat)
- Review in confirmation sheet
- Save to Room DB
- Nutrients auto-calculated (CalcVitaminStatusUseCase)

### Tracking Nutrients

19 tracked micronutrients (Vit A, B-complex, C, D, E, K, minerals, etc.)
- RDA-based targets (gender/age aware)
- Deficiency levels: Adequate → Warning → Deficient → Critical
- Real-time sync from food log + Health Connect
- Trend charts over 7/30/90 days

### Syncing Health Data

WorkManager triggers `HealthConnectSyncWorker` periodically
- Steps, sleep, heart rate, calories from HC
- Automatically populates dashboard rings
- Non-blocking background sync

---

## Tech Stack Summary

| Layer | Stack |
|:---|:---|
| **Language** | Kotlin 2.0+ |
| **UI** | Jetpack Compose + Material Design 3 |
| **Architecture** | Clean Arch + MVVM + Repository |
| **DI** | Hilt/Dagger 2 |
| **Database** | Room v3 + SQLCipher (AES-256) |
| **Networking** | Android Intent System (zero-API) |
| **Background** | WorkManager (sync, nudges) |
| **Health** | Health Connect 1.1+ |
| **Charts** | Vico (trend visualization) |

---

## Full Architecture Documentation

For detailed architecture, data flows, database schema, use cases, and design patterns, see [**Documentation/Architecture.md**](Documentation/Architecture.md)

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

**Fallback Chain:**
1. Gemini Nano (on-device) — 0 actions  
2. ChatGPT/Gemini Intent → deep-link → 1 tap  
3. Voice log → ParseVoiceInputUseCase → USDA lookup  
4. Manual form → offline USDA nutrition DB  

---

## Key Workflows

### Adding Food

**Scan → Confirm → Save**
- Open camera, capture food
- AI identifies nutrients (cal, protein, carbs, fat)
- Review in confirmation sheet
- Save to Room DB
- Nutrients auto-calculated (CalcVitaminStatusUseCase)

### Tracking Nutrients

19 tracked micronutrients (Vit A, B-complex, C, D, E, K, minerals, etc.)
- RDA-based targets (gender/age aware)
- Deficiency levels: Adequate → Warning → Deficient → Critical
- Real-time sync from food log + Health Connect
- Trend charts over 7/30/90 days

### Syncing Health Data

WorkManager triggers `HealthConnectSyncWorker` periodically
- Steps, sleep, heart rate, calories from HC
- Automatically populates dashboard rings
- Non-blocking background sync

---

## Tech Stack Summary

| Layer | Stack |
|:---|:---|
| **Language** | Kotlin 2.0+ |
| **UI** | Jetpack Compose + Material Design 3 |
| **Architecture** | Clean Arch + MVVM + Repository |
| **DI** | Hilt/Dagger 2 |
| **Database** | Room v3 + SQLCipher (AES-256) |
| **Networking** | Android Intent System (zero-API) |
| **Background** | WorkManager (sync, nudges) |
| **Health** | Health Connect 1.1+ |
| **Charts** | Vico (trend visualization) |

---

## Full Architecture Documentation

For detailed architecture, data flows, database schema, use cases, and design patterns, see [**Documentation/Architecture.md**](Documentation/Architecture.md)

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

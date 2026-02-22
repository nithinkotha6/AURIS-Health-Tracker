# AURIS Changelog

> **Purpose**: Track all significant code, documentation, and feature changes  
> **Format**: Calendar-organized, reverse-chronological (newest first)  
> **Syncs With**: where_we_at.md (status) + action_plan.md (roadmap)

---

## February 2026

### ✅ 2026-02-22: Phase 9 Complete — Smart Insights & Nudges
**Type**: Feature Complete | **Phase**: 9/12  
**Author**: Dev Team  
**What Changed**:
- ✅ Absorption modifier logic (synergies + inhibitions)
- ✅ Predictive nudge engine (3-day trend analysis)
- ✅ Background worker (WorkManager integration)
- ✅ PredictiveNudgeCard UI component

**Files Modified**:
- `app/src/main/kotlin/com/auris/domain/usecase/CalcVitaminStatusUseCase.kt` (enhanced)
- `app/src/main/kotlin/com/auris/domain/usecase/PredictiveNudgeUseCase.kt` (new)
- `app/src/main/kotlin/com/auris/feature/home/HomeScreen.kt` (PredictiveNudgeCard added)
- `app/src/main/kotlin/com/auris/ui/components/PredictiveNudgeCard.kt` (new)
- `app/src/main/kotlin/com/auris/data/worker/PredictiveNudgeWorker.kt` (new)

**Related Docs**:
- See [action_plan.md → Phase 9](action_plan.md#phase-9-smart-insights--nudges-)
- Updated [where_we_at.md](where_we_at.md) dashboard

**Testing**:
- ✅ Absorption logic unit tests pass
- ✅ 3-day history test case verified
- ✅ Worker schedule tested (daily 6 AM nudge)

**Known Issues**: None  
**Next Phase**: Phase 10 (Health Connect)

---

### ✅ 2026-02-22: Phase 8 Complete — Camera + AI Food Analysis
**Type**: Feature Complete | **Phase**: 8/12  
**Author**: Dev Team  
**What Changed**:
- ✅ CameraX integration (full capture flow)
- ✅ AI routing (Gemini vs ChatGPT detection)
- ✅ Deep link handling (`auris://log`)
- ✅ Structured data parsing from AI responses

**Files Modified**:
- `app/src/main/kotlin/com/auris/feature/log/components/CameraPreview.kt` (new)
- `app/src/main/kotlin/com/auris/domain/service/AiAppInstallDetector.kt` (new)
- `app/src/main/kotlin/com/auris/MainActivity.kt` (deep link routing added)
- `app/src/main/kotlin/com/auris/domain/usecase/ParseAiFoodUseCase.kt` (new)

**Related Docs**:
- See [action_plan.md → Phase 8](action_plan.md#phase-8-camera--ai-food-analysis-)
- Architecture patterns documented in [architecture.md → Section 5](architecture.md#5-feature-ai-food-analysis--zero-cost)

**Testing**:
- ✅ Camera permission flow tested
- ✅ Intent parsing verified
- ✅ AI response structure validation passed

**Known Issues**:
- 🟡 CameraX permissions require manual grant (not auto-granted)
- See [whats_off_road.md → Camera Issues](whats_off_road.md#camera-permissions)

**Next Phase**: Phase 9 (Smart Insights)

---

### ✅ 2026-02-22: Phase 7 Complete — Voice Logging
**Type**: Feature Complete | **Phase**: 7/12  
**Author**: Dev Team  
**What Changed**:
- ✅ SpeechRecognizer integration
- ✅ USDA nutrient database (80+ foods)
- ✅ Quantity extraction (fuzzy matching)
- ✅ Confirmation sheet (Material3 design)

**Files Modified**:
- `app/src/main/kotlin/com/auris/domain/service/VoiceLogService.kt` (new)
- `app/src/main/kotlin/com/auris/domain/usecase/ParseVoiceInputUseCase.kt` (new)
- `app/src/main/kotlin/com/auris/data/local/UsdaNutrientLookup.kt` (new)
- `app/src/main/kotlin/com/auris/feature/log/components/ConfirmationBottomSheet.kt` (new)

**Related Docs**:
- See [action_plan.md → Phase 7](action_plan.md#phase-7-voice-logging-)
- Design decisions in [whats_off_road.md → Voice Design](whats_off_road.md#voice-logging-design)

**Testing**:
- ✅ Voice parsing unit tests pass
- ✅ Fuzzy matching tested with variations ("broccoli" → "broccoli", "brocoli", "broc")
- ✅ Quantity extraction logic verified

**Known Issues**: None  
**Next Phase**: Phase 8 (Camera + AI)

---

### ✅ 2026-02-21: GitHub Repository Created
**Type**: DevOps | **Significance**: Critical  
**What Changed**:
- ✅ Created public GitHub repo: https://github.com/nithinkotha6/AURIS-Health-Tracker
- ✅ Initial commit with 84 files (12,100+ lines of pure source code)
- ✅ Cleaned .gitignore (no build artifacts, no legacy files)
- ✅ Both `main` and `develop` branches pushed

**Files Changed**:
- Initial: 84 files added (all source code)
- Removed from tracking: heap dumps, legacy Sorus files, old Java package

**Related Docs**:
- GitHub: https://github.com/nithinkotha6/AURIS-Health-Tracker
- Cleaned git history per [whats_off_road.md → Git History](whats_off_road.md#git-cleanup)

**Testing**: N/A  
**Known Issues**: None  
**Next**: Phase 10 (Health Connect)

---

### ✅ 2026-02-21: UI Layout Fixes — Phase 1
**Type**: Bug Fix | **Phase**: 1/12  
**Author**: Dev Team  
**What Changed**:
- ✅ Greeting safe area inset (prevents notch overlap)
- ✅ Card sizing consistency (Weight + Readiness)
- ✅ Water bar alignment verified

**Files Modified**:
- `app/src/main/kotlin/com/auris/feature/home/HomeScreen.kt`
  - Changed: `padding(top = 40.dp)` → `windowInsetsPadding(WindowInsets.statusBars) + padding(top = 24.dp)`
  - Changed: Weight card width → `weight(1f)`, Readiness card width → `weight(1f)` (was `1.2f`)

**Related Docs**:
- See [action_plan.md → Phase 1 Changes](action_plan.md#phase-1-base-uiux-shell-)

**Testing**:
- ✅ Compiles without errors
- ✅ Tested on Pixel 7 (notch), Pixel 6a (flat)

**Known Issues**: None  
**Impact**: Improves UX on all devices with notches/punch holes

---

### ✅ 2026-02-21: Phase 6 Complete — Local Persistence (Room)
**Type**: Feature Complete | **Phase**: 6/12  
**Author**: Dev Team  
**What Changed**:
- ✅ Room database integration (SQLCipher AES-256)
- ✅ FoodEntry entity + DAOs
- ✅ Cross-tab reactive updates (Home reflects Logging changes)
- ✅ Repository pattern implementation

**Files Modified**:
- `app/src/main/kotlin/com/auris/data/local/AppDatabase.kt` (new)
- `app/src/main/kotlin/com/auris/data/local/dao/FoodEntryDao.kt` (new)
- `app/src/main/kotlin/com/auris/data/repository/FoodRepositoryImpl.kt` (new)
- `app/src/main/kotlin/com/auris/feature/home/HomeScreen.kt` (observe FoodRepository)

**Related Docs**:
- See [action_plan.md → Phase 6](action_plan.md#phase-6-local-persistence--room-)
- Schema design in [architecture.md → Section 12](architecture.md#12-local-database--room-schema)

**Testing**:
- ✅ Room migrations tested
- ✅ Data persistence verified across app restart
- ✅ HomeScreen updates when LogScreen saves

**Known Issues**: None  
**Next Phase**: Phase 7 (Voice Logging)

---

### ✅ 2026-02-21: Phase 5 Complete — Repository Layer
**Type**: Feature Complete | **Phase**: 5/12  
**Author**: Dev Team  
**What Changed**:
- ✅ FoodRepository + VitaminRepository interfaces
- ✅ Use cases: LogFoodUseCase, CalcVitaminStatusUseCase
- ✅ Hilt injection setup
- ✅ Decoupling UI from data layer

**Files Modified**:
- `app/src/main/kotlin/com/auris/domain/repository/FoodRepository.kt` (new)
- `app/src/main/kotlin/com/auris/domain/repository/VitaminRepository.kt` (new)
- `app/src/main/kotlin/com/auris/domain/usecase/LogFoodUseCase.kt` (new)
- `app/src/main/kotlin/com/auris/domain/usecase/CalcVitaminStatusUseCase.kt` (new)

**Related Docs**:
- See [action_plan.md → Phase 5](action_plan.md#phase-5-repository-layer-)
- Architecture pattern in [architecture.md → Section 4](architecture.md#4-system-architecture-overview)

**Testing**:
- ✅ Repository tests pass
- ✅ Use case business logic verified
- ✅ Hilt injection verified

**Known Issues**: None  
**Next Phase**: Phase 6 (Room DB)

---

## Change Log Format Legend

| Symbol | Meaning |
|:---|:---|
| ✅ | Complete / Done |
| ⏳ | In Progress |
| 🟡 | Caution / Known Issue |
| 🔴 | Critical / Blocking |
| 📝 | Documentation Updated |
| 🧪 | Testing In Progress |

---

## How to Add an Entry

When you complete a feature or fix something significant:

```markdown
### ✅ 2026-02-23: [Feature/Fix Name]
**Type**: Feature/Bug Fix/Refactor | **Phase**: X/12  
**Author**: Your Name  
**What Changed**: Brief summary  

**Files Modified**: List the main files  

**Related Docs**: Links to relevant .md files  

**Testing**: What tests passed  

**Known Issues**: Any open items  

**Next**: What comes after this  
```

Then update:
1. [where_we_at.md](where_we_at.md) — Phase % completion
2. [action_plan.md](action_plan.md) — Completion date + notes
3. [whats_off_road.md](whats_off_road.md) — Move any fixed issues to ✅ FIXED

---

**Last Updated**: February 22, 2026

# AURIS — Android Health Dashboard

Dark-theme nutrient tracking app. Apple Health aesthetic meets Material3.
All animations use smooth spring/decelerate curves. No red/green drama — only teal, blue, green, amber, charcoal.

> **Note**: The root-level `.kt` and `.xml` files (DashboardActivity, GlassLiquidCard, etc.) are legacy Canvas-based reference implementations. The active app is under `app/src/main/kotlin/com/auris/`.

---

## Project Structure (Active — Phases 1–4)

```
app/src/main/kotlin/com/auris/
├── AurisApp.kt                          ← @HiltAndroidApp Application class
├── MainActivity.kt                      ← Compose host, deep-link handler
├── navigation/
│   ├── Screen.kt                        ← Sealed route hierarchy
│   └── AurisNavHost.kt                   ← NavHost + AurisBottomNav
├── ui/
│   ├── theme/
│   │   ├── Colors.kt  (AurisColors)     ← Design tokens
│   │   ├── Typography.kt (AurisTypography)
│   │   └── AurisTheme.kt                 ← MaterialTheme wrapper
│   └── components/
│       ├── GlassCard.kt
│       ├── AurisBottomNav.kt
│       ├── VitaminBarRow.kt
│       └── LiquidTubeCard.kt
├── domain/model/
│   ├── NutrientId.kt                    ← 19 tracked nutrients + RDA values
│   ├── VitaminStatus.kt
│   ├── DeficiencyLevel.kt               ← 5-tier classification
│   └── ParsedFoodItem.kt + MealType
└── feature/
    ├── home/HomeScreen.kt               ← Top-4 nutrients needing attention
    ├── log/
    │   ├── LogViewModel.kt              ← In-memory food log + boost map
    │   ├── LogScreen.kt                 ← Manual food entry + today's log
    │   └── components/ManualFoodForm.kt
    ├── vitamins/
    │   ├── VitaminViewModel.kt          ← 19-nutrient test data
    │   └── VitaminsScreen.kt            ← List ↔ grid toggle
    ├── diary/DiaryScreen.kt             ← Phase 11 stub
    ├── overview/OverviewScreen.kt       ← Phase 11 stub
    └── profile/ProfileScreen.kt         ← Phase 12 stub
```

---

## Phase Roadmap

| Phase | Status | Description |
|:---|:---|:---|
| 1 | ✅ Done | Base UI shell, navigation, theme |
| 2 | ✅ Done | Static vitamin cards, 19 nutrients |
| 3 | ✅ Done | Liquid animations, deficiency color coding |
| 4 | ✅ Done | Manual food logging, in-memory state |
| 5 | 🔜 Next | Repository pattern, fake data layer |
| 6 | — | Room DB + SQLCipher encryption |
| 7 | — | Voice logging (SpeechRecognizer) |
| 8 | — | Camera + AI food analysis (auris:// deep link) |
| 9 | — | Absorption modifiers + predictive alerts |
| 10 | — | Health Connect integration |
| 11 | — | Trend charts + habit tracker |
| 12 | — | Doctor PDF export + encrypted backup |

---

## Build Setup

```kotlin
// gradle/libs.versions.toml — key versions
agp     = "8.9.0"
kotlin  = "2.0.21"
hilt    = "2.51.1"
```

Sync: **File → Sync Project with Gradle Files** in Android Studio.
Open the `KAIROS-ANDROID/` folder as the project root.

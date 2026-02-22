# SALUS — Figma Design System & MCP Instructions

> **Frame**: 375 × 812 px · **Framework**: Flutter/Dart · **Theme**: Dark only · **Updated**: Feb 2026

---

## 1. Color Tokens

| Token | Hex | Usage |
|:---|:---|:---|
| `Background/App` | `#1E1E1E` | Screen background |
| `Background/Surface` | `#1A1A1A` | Cards, sheets |
| `Background/Elevated` | `#282825` | Nav bar, modals |
| `Primary/Teal` | `#00E055` | Active states, FAB, glows |
| `Primary/Teal/Dark` | `#0080CC` | Gradient bottom stop |
| `Accent/Gold` | `#FFB700` | Logo ring, streak |
| `Accent/Orange` | `#FF8C00` | CTA buttons, warnings |
| `Accent/Salmon` | `#FF6B6B` | Error / over-limit |
| `Text/White` | `#FFFFFF` | Primary text |
| `Text/Caption` | `#AAAAAA` | Secondary / timestamps |
| `State/Inactive` | `#3A3A3A` | Empty bars, disabled |
| `Shadow/Card` | `#000000` 35% | Card shadows |

### Gradient Tokens

| Token | Stops | Direction |
|:---|:---|:---|
| `gradient/liquid/teal` | `#00E055` → `#0080CC` | 180° top→bottom |
| `gradient/gold/radial` | `#FFD700` center → `#FF8C00` edge | Radial |
| `gradient/surface/dark` | `#282825` → `#1A1A1A` | 180° |
| `gradient/glow/teal` | `#00E055` 30% → transparent | Radial 60px |
| `gradient/vitamin/status` | `#FF8C00` → `#FFC107` → `#8BC34A` → `#00E055` | 90° left→right |

---

## 2. Typography (Inter, Google Fonts)

| Token | Weight | Size | Usage |
|:---|:---|:---|:---|
| `text/displayLarge` | Bold | 32px | Splash app name |
| `text/heading1` | Bold | 24px | Screen titles |
| `text/heading2` | SemiBold | 20px | Card titles |
| `text/body` | Regular | 16px | Body, list items |
| `text/bodyMedium` | Medium | 15px | Nutrient values |
| `text/caption` | Light | 12px | Timestamps |
| `text/captionBold` | Bold | 12px | TODAY, badges |
| `text/valueDisplay` | Bold | 28px | Calorie ring center |
| `text/micro` | Regular | 10px | Nav labels |

---

## 3. Spacing & Grid

**Base**: 8px grid. All padding/gaps must be multiples of 8 (exceptions: 2px micro, 4px tight).

| Token | Value | Usage |
|:---|:---|:---|
| `space/8` | 8px | Standard unit |
| `space/16` | 16px | Card padding, H margins |
| `space/24` | 24px | Section gap |
| `space/32` | 32px | Major separator |
| `space/48` | 48px | FAB bottom margin |

- **Horizontal margin**: 16px all screens · **Card radius**: 16px · **Sheet radius**: 24px top only

---

## 4. App Frame

| Property | Value |
|:---|:---|
| Size | 375 × 812 px |
| Background | `#1E1E1E` |
| Safe area top | 44px |
| Safe area bottom | 34px |
| Status bar | 20px |
| Canvas bg | `#2A2A2A` (so dark frames are visible) |
| Layer name | `Frame/Mobile/SALUS` |

- All local styles in group `SALUS / Tokens` · All layouts use Auto Layout

---

## 5. Splash Screen (`Screen/Splash`)

### Logo Circle
| Property | Value |
|:---|:---|
| Size | 200 × 200 px ellipse, centered |
| Fill | `gradient/gold/radial` (`#FFD700` center → `#FF8C00` edge) |
| Shadow | X=0 Y=4 Blur=10 Spread=0 · `#000000` 35% |
| Content | `Asset/Birdie` SVG · 96 × 96 px · `#FFD700` · flat silhouette |
| Layer | `Logo/Circle` |

### Title "SALUS"
| Property | Value |
|:---|:---|
| Font | Inter Bold 24px · `#00E055` · -0.3px tracking · centered |
| Margin | 24px below Logo/Circle |
| Layer | `Text/AppTitle` |

### Birdie SVG Export
- `birdie.svg`, `birdie@2x.png`, `birdie@3x.png` → `assets/images/`
- Viewbox 96×96 · monochrome flat (`#FFD700`)

---

## 6. Vitamin Status Bars — Screen/Vitamins

**One full-width bar per vitamin, all stacked vertically in a scrollable list.**

### Parent Card (`Card/VitaminList`)
| Property | Value |
|:---|:---|
| Width | 343px (full minus 16px margins) |
| Fill | `#1A1A1A` · border 1px `#2A2A2A` · radius 16px · padding 16px |
| Inner gap | 8px between bar rows |

### Section Header
- Left: `VITAMINS` — Inter Bold 12px `#AAAAAA` letter-spacing 1.2px
- Right: `Today's Intake` — Inter Regular 12px `#AAAAAA`
- Layer: `Text/SectionHeader/Vitamins`

### Each Bar Row (`Bar/VitaminStatus/[Name]`) — Auto Layout Vertical, gap 4px
```
Info Row (space-between)
  Left:  [Icon 16px]  [Vitamin Name — Inter Medium 13px #FFFFFF]
  Right: [Metric Value — Inter Bold 13px #FFFFFF]
Bar Track (full width · 8px tall · #2A2A2A · radius 4px)
  Fill  (width = percentComplete% · 8px · gradient/vitamin/status · left radius 4px)
```

### Gradient Fill Logic
The gradient spans the **full track width** always. The fill rect clips it at `percentComplete`%:
- **22%** → user sees only orange (`#FF8C00`) — indicates deficiency (Vitamin D)
- **80%** → user sees into green (`#00E055`) — indicates optimal

### Icon Color by Intake Level
| Range | Icon Color | Meaning |
|:---|:---|:---|
| 0–30% | `#FF8C00` | Deficient |
| 31–59% | `#FFC107` | Low |
| 60–79% | `#8BC34A` | Adequate |
| 80–100% | `#00E055` | Optimal |

### Deficiency Badge (`Badge/Deficiency`) — visible only when < 30%
- 40×16px rect · `#FF8C00` 20% fill · 1px `#FF8C00` border · `LOW` Inter Bold 9px `#FF8C00`

### Default Vitamin List (screen order)

| # | Vitamin | Icon ID | Default % | Unit |
|:---|:---|:---|:---|:---|
| 1 | Vitamin D | `icon/sun` | **22%** | IU |
| 2 | Vitamin A | `icon/eye` | 83% | mcg |
| 3 | Vitamin B12 | `icon/dna` | 80% | mcg |
| 4 | Vitamin C | `icon/leaf` | 80% | mg |
| 5 | Vitamin E | `icon/shield` | 85% | mg |
| 6 | Biotin | `icon/dna-2` | 76% | mcg |
| 7 | Iron | `icon/drop` | 78% | mg |
| 8 | Calcium | `icon/bone` | 80% | mg |
| 9 | Magnesium | `icon/bolt` | 72% | mg |
| 10 | Zinc | `icon/atom` | 65% | mg |
| 11 | Folate | `icon/leaf-2` | 88% | mcg |
| 12 | Vitamin K | `icon/bandage` | 90% | mcg |

---

## 7. Liquid Progress Bar (`LiquidBar/Master`)

Used for macros, water, calorie ring. 3 variants:

| Variant | Size | Track | Fill |
|:---|:---|:---|:---|
| `vertical-tube` | 10 × 80 px | `#2A2A2A` · radius 5px | `gradient/liquid/teal` · height animated |
| `horizontal-bar` | full-width × 8 px | `#2A2A2A` · radius 4px | `gradient/liquid/teal` · width animated |
| `circular-ring` | 160px diameter · 12px stroke | `#2A2A2A` | Arc `#00E055`→`#0080CC`; center: Bold 28px kcal |

---

## 8. Bottom Navigation Bar (`NavBar/Master`)

**Frame**: 375 × 60 px · Fill `#282825` · Top border 1px `#2A2A2A` · 5 tabs × 75 px wide

All icons: 24 × 24 px SVG · Active: `#00E055` · Inactive: `#AAAAAA` · Labels: Inter Regular 10px

| Tab | Icon | Container | Active State | Animation |
|:---|:---|:---|:---|:---|
| Home | `icon/home` | Circle 40px | Teal fill 15% + 8px glow | — |
| Log | `icon/pencil` | Square 50×50px `#1A1A1A` teal border 1px radius 10px | Border 100% opacity | Shake (§10.3) |
| Diary | `icon/diary` | Rect 60×40px teal border 1px radius 6px | Border fade in | — |
| Overview | `icon/barchart` | Circle 45px teal 10% fill | — | Pulse (§10.4) |
| Profile | `icon/person` | Circle 40px teal fill (active) | White icon on teal | Zoom (§10.5) |

Active indicator: 4px teal dot above icon (Home tab only).

---

## 9. FAB Speed-Dial (`FAB/SpeedDial`)

**Main FAB**: Circle 56px · fill `#00E055` · `icon/plus` 24px white · shadow: 0 4 12 `#00E055` 40%
Position: bottom-right 16px from edge, 16px above NavBar.

**Speed-dial mini-FABs** (40px circle, 56px vertical stagger above FAB):

| Icon | Label | Color |
|:---|:---|:---|
| `icon/camera` | Scan Food | `#00E055` |
| `icon/pencil-alt` | Quick Log | `#00E055` |
| `icon/water-drop` | Water | `#4FC3F7` |

Labels: Inter Medium 12px white · 8px to the left of each mini-FAB.

---

## 10. Home Dashboard Screen (`Screen/Home`)

**Layout**: Vertical scroll · Auto Layout, gap 24px · 16px H padding · 44px top safe area

**Section order** (top → bottom):
1. Status bar 20px
2. Greeting: Inter Bold 24px `#FFFFFF` / date Inter Light 12px `#AAAAAA`
3. Calorie Ring Card: full-width, 190px, `#1A1A1A`, `Ring/CalorieProgress` 160px + macro bars sidebar
4. Smart Cards Row (horizontal scroll): Macro donut · Water bar · Steps — each 140×120px `#1A1A1A` radius 16px
5. Vitamin Summary (top 4 bars → taps to Vitamins screen)
6. Habits Row: scrollable chips 80px
7. NavBar 60px (pinned)
8. FAB (overlaid)

### Animation Specs

#### 10.1 Vitamin Bar Fill (on load / after log)
- Duration 700ms · `cubic-bezier(0.25, 0.46, 0.45, 0.94)`
- Fill width: 0% → `percentComplete`% ; icon color: `#AAAAAA` → state color
- Stagger: 60ms delay per row

#### 10.2 Tab Glow (Home active)
- 300ms ease-out · shadow 0 → 8px blur `#00E055` 40%

#### 10.3 Log Tab Shake
- 300ms · Keyframes: 0°→ -8°→ 8°→ -4°→ 0° at T=0/75/150/225/300ms

#### 10.4 Overview Tab Pulse
- 400ms ease-in-out · scale 1.0→1.3→1.0 · opacity 100%→60%→100%

#### 10.5 Profile Tab Zoom
- Press: 150ms ease-out scale 1.0→1.15 · Release: 200ms ease-in scale 1.15→1.0

#### 10.6 AI Sheet Slide-Up
- 350ms `cubic-bezier(0.22, 1.0, 0.36, 1.0)` · Y: +300→0 + opacity 0→1 · backdrop `#000000` 50%

#### 10.7 Habit Chip Complete
- 250ms · left-wipe fill `#00E055` 20% + checkmark fade · 3-particle confetti 500ms

---

## 11. AI Confirmation Sheet (`Sheet/AIConfirmation`)

**Frame**: 375px wide · dynamic height (min 300, max 600) · fill `#1A1A1A` · top radius 24px · handle: 32×4px `#3A3A3A` centered at top 8px

**Layout** (top → bottom):
- Handle bar
- Header: 🍽️ icon + "Meal Detected" Bold 20px + meal-type badge (teal bg, 10px capsule)
- Divider 1px `#2A2A2A`
- Food item list (scrollable): name Medium 15px · portion Light 12px `#AAAAAA` · kcal Bold 14px `#00E055` · edit icon right
- Divider
- Total row: "TOTAL" Bold 12px `#AAAAAA` · total kcal Bold 20px · P/C/F badges teal
- **[Log Meal]**: full-width 48px · `#00E055` fill · Bold 16px white · radius 12px
- **[Discard]**: text button Regular 14px `#AAAAAA`
- Safe area spacer 34px

---

## 12. Icon System

**Format**: SVG, 24×24 viewBox · **Style**: 2px stroke, rounded caps/joins · **Color**: monochrome; apply via component property, not baked-in

### Required Icons

| ID | Material Name | Use |
|:---|:---|:---|
| `icon/home` | `ic_home_rounded` | Nav Tab 1 |
| `icon/pencil` | `ic_edit_rounded` | Nav Tab 2 |
| `icon/diary` | `ic_book_rounded` | Nav Tab 3 |
| `icon/barchart` | `ic_bar_chart` | Nav Tab 4 |
| `icon/person` | `ic_person_rounded` | Nav Tab 5 |
| `icon/plus` | `ic_add` | FAB |
| `icon/camera` | `ic_photo_camera` | FAB speed-dial |
| `icon/pencil-alt` | `ic_draw` | FAB speed-dial |
| `icon/water-drop` | `ic_water_drop` | FAB speed-dial |
| `icon/sun` | `ic_wb_sunny` | Vitamin D |
| `icon/eye` | `ic_visibility` | Vitamin A |
| `icon/leaf` | `ic_eco` | Vitamin C |
| `icon/dna` | `ic_genetics` | Vitamin B12 |
| `icon/shield` | `ic_shield` | Vitamin E |
| `icon/drop` | `ic_water` | Iron |
| `icon/bone` | `ic_bone` | Calcium |
| `icon/bolt` | `ic_bolt` | Magnesium |
| `icon/atom` | `ic_science` | Zinc |
| `icon/leaf-2` | `ic_spa` | Folate |
| `icon/bandage` | `ic_healing` | Vitamin K |
| `icon/check` | `ic_check_circle` | Habit complete |
| `icon/flame` | `ic_local_fire` | Streak |

**States**: `state=default` `#AAAAAA` 70% · `state=active` `#00E055` 100% · `state=disabled` `#3A3A3A`

**Naming**: Figma: `Icons/[category]/[icon_id]` · Export: `ic_[id].svg` → `assets/icons/`

---

## 13. Figma File Structure

### Pages
```
SALUS Design System
├── 🎨 Tokens      — All color/type/spacing local styles
├── 🔲 Components  — All master components
├── 📱 Screens     — Screen frames (instances only, no one-offs)
├── 🔤 Icons       — Icon component library
├── 🧪 Prototypes  — Wired flows
└── 📄 Specs       — Developer handoff / this doc
```

### Naming Conventions
- **Components**: `[Category]/[Name]/[Variant=Value]`  
  e.g. `Bar/VitaminStatus/state=default` · `Tab/Home/state=active` · `Sheet/AIConfirmation/state=success`
- **Layers**: `[Type]/[Description]/[Modifier]`  
  Types: Frame, Auto, Rect, Ellipse, Icon, Asset, Text, Divider  
  e.g. `Frame/Screen/Home` · `Rect/VitaminBar/Fill` · `Icon/Home/active`
- ❌ Never use default Figma names (`Rectangle 12`, `Frame 47`)
- ❌ Never use opacity to simulate color — use actual color tokens
- ❌ Never use fonts or sizes outside the token list

---

## 14. Screen Layout Rules

### Z-Order (bottom → top)
`Z1 Background` → `Z2 Scroll content` → `Z3 NavBar` → `Z4 FAB` → `Z5 FAB speed-dial` → `Z6 Bottom sheets` → `Z7 Dialogs` → `Z8 Toast` → `Z9 Status bar`

### Safe Areas
- Top 44px, Bottom 34px, Left/Right 16px — never violate these
- NavBar pinned bottom (Flutter `Stack` over scroll view)
- FAB always visible; does not hide on scroll

---

## 15. MCP Integration Rules

```
TOKEN READING
  1. Read from 'SALUS / Tokens' local styles first.
  2. Never invent values — check this file if token is missing.
  3. Always use token names in code, not raw hex.

COMPONENT CREATION
  1. All components must use Auto Layout.
  2. Constraints: Left+Right expand, Top+Bottom fixed (full-width components).
  3. All interactive states must have a Variant — no hidden layers.
  4. Use component properties (boolean/text/instance swap), not layer visibility.

SCREEN GENERATION
  1. Start: Frame 375×812px fill #1E1E1E.
  2. Safe area: 44px top padding, 34px bottom padding.
  3. NavBar: pinned Bottom constraint.
  4. Content: instances only — no one-off designs.

ICON USAGE
  1. Import as instances from Icons page — never copy/detach.
  2. Change color via 'icon fill' override property only.
  3. Active = #00E055 · Inactive = #AAAAAA — no exceptions.

EXPORTS
  birdie.svg / @2x / @3x         → assets/images/
  ic_[name].svg (all icons)      → assets/icons/
  Color tokens                   → lib/core/theme/app_colors.dart
  Text styles                    → lib/core/theme/app_text_styles.dart
```

---

## 16. Handoff Checklist

**Tokens**: 20+ colors · 5 gradients · 9 text styles · spacing guide frame

**Components**: `Bar/VitaminStatus` (12 vitamins) · `LiquidBar/Master` (3 variants) · `NavBar/Master` · `Tab/[x5]` active+inactive · `FAB/SpeedDial` · `Sheet/AIConfirmation` success+empty · `Ring/CalorieProgress`

**Screens**: Splash · Home · Vitamins · Diary · Habits · Profile · Camera · AI Confirmation overlay

**Icons**: All 23 listed above — SVG + PNG @2x @3x

**Prototype flows**: Home→FAB→ScanFood · NavBar tab switch · AI sheet auto-trigger · Habit chip completion

---
*Cross-reference: `Architecture Overview.md` (feature specs) · `VitalTrack_AI_InterApp_Connectivity.md` (AI flow UX)*

# AURIS Documentation Guide

> **Purpose**: Central hub for understanding AURIS project structure, status, and development workflow  
> **Last Updated**: February 22, 2026  
> **For**: Developers, Code Reviewers, Project Managers

---

## 📁 Documentation Files Overview

This folder contains the single source of truth for AURIS development. Each file has a specific purpose and update frequency.

### **1. WHERE_WE_AT.md** — Current Status Snapshot ⭐
**Purpose**: Real-time project dashboard  
**Update Frequency**: Weekly (or after major milestones)  
**Audience**: Everyone  
**Contains**:
- ✅ Phase completion percentages (95-100%)
- 📊 Feature database (what's built, what's tested)
- 🐛 Known issues and blockers
- 📍 Next immediate action items
- 🔄 Recent changes (last 2 weeks)

**When to Update**:
- After completing a phase/feature
- When major bugs are discovered
- When priorities shift
- Weekly team sync

**Quick Reference**: "What is the app capable of right now?"

---

### **2. ACTION_PLAN.md** — Detailed Roadmap & Task Breakdown 📋
**Purpose**: Long-form implementation guide for developers  
**Update Frequency**: Per sprint (every 2 weeks)  
**Audience**: Developers implementing features  
**Contains**:
- 📝 All 12 phases with detailed breakdown
- ✍️ When each phase was completed (date stamped)
- 🎯 Acceptance criteria for each phase
- 🔗 Links to related code/commits
- 💡 Implementation notes and gotchas
- 📚 Code examples and snippets

**When to Update**:
- Before starting a new phase (flesh out details)
- After completing implementation (add notes)
- When technical approach changes
- When you discover a better way to do something

**Quick Reference**: "How do I build [feature]? What's already done?"

---

### **3. ARCHITECTURE.md** — Technical Design Document 🏗️
**Purpose**: System design reference and code organization  
**Update Frequency**: Quarterly (or when adding new modules)  
**Audience**: Senior developers, architects, code reviewers  
**Contains**:
- 🏛️ Overall system architecture (layers, packages)
- 🔌 API contracts (what each module exposes)
- 💾 Database schema (Room entities, relationships)
- 🔐 Security model and privacy decisions
- 🧩 Dependency graph
- 📋 Design patterns used
- ⚠️ Constraints and tradeoffs

**When to Update**:
- Adding a new feature layer/module
- Changing core design decisions
- Security/privacy review
- After architectural refactoring

**Quick Reference**: "How is the code organized? What calls what?"

---

### **4. WHATS_OFF_ROAD.md** — Issues, Blockers & Decisions ⚠️
**Purpose**: Track problems, decisions, and workarounds  
**Update Frequency**: Real-time (as you discover issues)  
**Audience**: All developers  
**Contains**:
- 🔴 Critical blockers (can't proceed without fixing)
- 🟡 Known bugs (affects quality, not blocking)
- ⭕ Edge cases (weird behavior, unclear if intentional)
- 📌 Design decisions (why we chose A over B)
- 🛠️ Technical debt (code smell, refactoring needed)
- 🤔 Open questions (needs discussion)

**When to Update**:
- Immediately when you discover a bug
- When a decision is made
- When a blocker is resolved (move to ✅ FIXED)

**Quick Reference**: "What's broken? What's risky? What are we avoiding?"

---

### **5. REVIEW_[VERSION].md** — Code Review Snapshots 🔍
**Purpose**: Document significant reviews and fixes  
**Update Frequency**: After each major code review  
**Audience**: Team leads, reviewers  
**Contains**:
- 📝 What was reviewed (file list + changes)
- ✅ What passed review
- ❌ What needs fixing
- 💬 Key discussions/debates
- 🔗 PR/commit links
- 📅 Review date and reviewer

**When to Update**:
- After each sprint review
- After receiving feedback
- When refactoring large sections

**Quick Reference**: "What did we learn in the last review? What needs to change?"

---

## 🔄 Documentation Sync Workflow

### **Daily Development Cycle**
```
1. START CODING
   └─ Check WHERE_WE_AT.md for current status
   └─ Check ACTION_PLAN.md for this phase's tasks
   
2. DURING DEV
   └─ Discover a bug? Add to WHATS_OFF_ROAD.md (🔴/🟡)
   └─ Make design decision? Add to WHATS_OFF_ROAD.md + ARCHITECTURE.md
   
3. AFTER FEATURE COMPLETION
   └─ Update ACTION_PLAN.md with completion date + notes
   └─ Update WHERE_WE_AT.md (Phase X% complete)
   └─ Move resolved issues from WHATS_OFF_ROAD.md to ✅ FIXED
   
4. BEFORE COMMITTING
   └─ Review relevant .md sections
   └─ Ensure commit message references docs (e.g., "Phase 5 complete—see action_plan.md")
```

### **Weekly Team Sync**
- Review WHERE_WE_AT.md dashboard
- Flag items in WHATS_OFF_ROAD.md for discussion
- Update priorities in ACTION_PLAN.md
- Assign next phase tasks

### **Sprint Planning**
- Use ACTION_PLAN.md to determine scope
- Break phase into specific tasks
- Reference ARCHITECTURE.md for code structure
- Note any WHATS_OFF_ROAD.md items affecting deadline

### **Code Review**
- Verify ACTION_PLAN.md implementation notes match code
- Check ARCHITECTURE.md still accurate
- Document findings in REVIEW_[VERSION].md
- Move WHATS_OFF_ROAD issues to ✅ FIXED if resolved

---

## 📊 File Update Matrix

| File | Update Frequency | Who Updates | Trigger |
|:---|:---:|:---:|:---|
| where_we_at.md | Weekly | Dev Lead | Phase complete, major bug, blocker resolved |
| action_plan.md | Per sprint | Developer | Before starting phase, after completion |
| architecture.md | Quarterly | Senior Dev | New module, refactoring, security review |
| whats_off_road.md | Real-time | Any Dev | Bug discovered, design decision, question |
| review_[version].md | After review | Reviewer | Code review completion, sprint retro |

---

## 💡 Best Practices

### ✅ DO
- **Date every update**: `> **Last Updated**: Feb 22, 2026`
- **Link to code**: `See [MainActivity.kt](../app/src/main/kotlin/com/auris/MainActivity.kt) line 42`
- **Include examples**: Show code snippets for complex features
- **Track blockers**: Mark critical issues with 🔴 emoji
- **Use tables**: Organize status, phases, features in tables
- **Reference commits**: "Implemented in `commit abc1234`"

### ❌ DON'T
- ❌ Let docs get stale (older than 1 week)
- ❌ Write the same thing in multiple files
- ❌ Mix strategic planning with daily tasks
- ❌ Document things that code comments should explain
- ❌ Forget to update when code changes
- ❌ Use vague language ("maybe", "probably")

---

## 🔗 Cross-File References

When writing documentation, link between files for consistency:

```markdown
# In where_we_at.md
Phase 5 is now complete (Feb 22). See detailed implementation notes in [action_plan.md#phase-5](action_plan.md#phase-5).

# In action_plan.md
**Phase 5: Repository Layer** — See current blockers in [whats_off_road.md#phase-5](whats_off_road.md#phase-5).

# In architecture.md
The Repository pattern is designed per the decisions in [whats_off_road.md#architecture-decisions](whats_off_road.md#architecture-decisions).
```

---

## 📱 Using Docs with Claude in VSCode

### **Workflow Tips**

1. **Pre-prompt Claude with context**:
   ```
   "I'm building Phase 7 of AURIS. Here's the status (where_we_at.md) 
   and the implementation plan (action_plan.md Phase 7). Here's my code 
   question..."
   ```

2. **Reference docs in questions**:
   ```
   "Per action_plan.md Phase 5, I need to implement FoodRepository. 
   Here's my code so far... should I also add VitaminRepository?"
   ```

3. **Ask Claude to check consistency**:
   ```
   "Is my VoiceLogService implementation consistent with the 
   architecture in architecture.md section 6?"
   ```

4. **Update docs collaboratively**:
   - Claude suggests changes → You verify
   - You ask Claude to draft documentation → You refine
   - Use Claude for formatting, organization, clarity

---

## 🎯 File Templates

### **When adding a new phase to action_plan.md**:
```markdown
## Phase X: [Feature Name]
### [Brief Description]

**Status**: In Progress (started Feb 22, 2026)  
**Estimated Completion**: Feb 25, 2026  
**Owner**: Your Name  
**Links**: [PR #XXX](link), [Feature Branch](link)

### What This Phase Achieves
- ✅ Acceptance criterion 1
- ✅ Acceptance criterion 2
- ⏳ Something in progress

### Implementation Notes
- Key decision 1 (see whats_off_road.md#decision-1)
- How to test
- Known limitations

### Code Changes
- `FeatureViewModel.kt` — main logic
- `FeatureScreen.kt` — UI composable
- `Repository.kt` — data layer
```

### **When documenting an issue in whats_off_road.md**:
```markdown
### 🔴 [Critical] Voice recognition hangs on empty input
**Discovered**: Feb 22, 2026  
**Affected Phase**: Phase 7 (Voice Logging)  
**Root Cause**: Missing null check in ParseVoiceInputUseCase  
**Impact**: App becomes unresponsive if user doesn't speak  
**Workaround**: None (blocking Phase 7 completion)  
**Fix**: [PR #XXX](link) — add input validation  
**Status**: ✅ FIXED (Feb 22)
```

---

## 📞 Questions?

- **"What can the app do right now?"** → where_we_at.md
- **"How do I build feature X?"** → action_plan.md + architecture.md
- **"Why did we choose Y over Z?"** → whats_off_road.md (Decisions section)
- **"What's broken?"** → whats_off_road.md (Issues section)
- **"How is the code organized?"** → architecture.md
- **"What did we learn in review?"** → review_[version].md

---

**Happy documenting!** 📚

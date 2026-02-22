# Documentation Quick Start Guide

## 🎯 "I Need to..." Guide

### "I want to know what the app can do right now"
👉 **WHERE_WE_AT.md**
- Current phase % complete
- Feature checklist (what's working)
- Latest changes (this week)
- Next 3 tasks

---

### "I need to implement Feature X"
👉 **ACTION_PLAN.md** (Phase section for X)
- Step-by-step implementation guide
- Code examples
- Test procedures
- Common pitfalls
- Related files to touch

Then check:
- **ARCHITECTURE.md** — How to structure the code
- **WHATS_OFF_ROAD.md** — Known gotchas for this feature

---

### "I found a bug"
👉 **WHATS_OFF_ROAD.md** (Issues section)
1. Add new issue with:
   - 🔴 (critical) or 🟡 (minor)
   - Description + impact
   - Steps to reproduce
2. Update **WHERE_WE_AT.md** if blocking
3. Create a fix branch
4. When fixed, mark ✅ FIXED in WHATS_OFF_ROAD

---

### "I made a major code change"
👉 Update in order:
1. **ACTION_PLAN.md** — Add completion date + notes
2. **WHERE_WE_AT.md** — Update phase % (if appropriate)
3. **CHANGELOG.md** — Add entry with files changed
4. **ARCHITECTURE.md** — Update if structure changed
5. **WHATS_OFF_ROAD.md** — Mark any fixed issues with ✅

---

### "I'm in a team meeting"
👉 **WHERE_WE_AT.md** — Dashboard section
- Show these tables:
  - Phase completion status
  - Feature checklist
  - Current blockers
  - Next sprint items

---

### "Code review is coming"
👉 Before review:
1. Check **ARCHITECTURE.md** — Is my code structure correct?
2. Check **ACTION_PLAN.md** (Phase section) — Am I following the spec?
3. Check **WHATS_OFF_ROAD.md** — Any issues I should address?

After review:
1. Update **REVIEW_[VERSION].md** with findings
2. Move feedback items to **WHATS_OFF_ROAD.md** if needed

---

## 📊 Documentation Flowchart

```
┌─────────────────────────────────────────────────────────┐
│ YOU START WORKING ON SOMETHING                          │
└────────────────┬────────────────────────────────────────┘
                 │
                 ▼
         ┌───────────────┐
         │ Is it a bug?  │
         └───┬───────┬───┘
            YES      NO
             │        │
             │        ▼
             │    ┌──────────────────┐
             │    │ What type of work?
             │    └─┬────┬────┬──────┘
             │     │    │    │
             │   new   fix refactor
             │   feature
             │     │    │    │
             │     ▼    ▼    ▼
             │   [ACTION_PLAN] [ARCHITECTURE]
             │         │          │
             │         └────┬─────┘
             │              │
             ▼              ▼
        ┌─────────────────────────────────┐
        │ Check WHATS_OFF_ROAD.md for     │
        │ known issues + gotchas          │
        └─────────────┬───────────────────┘
                      │
                      ▼
           ┌──────────────────────┐
           │ DEVELOP & TEST CODE  │
           └─────────┬────────────┘
                     │
                     ▼
        ┌────────────────────────────────┐
        │ COMMIT & DOCUMENT               │
        │ 1. Update ACTION_PLAN.md        │
        │ 2. Update WHERE_WE_AT.md        │
        │ 3. Add to CHANGELOG.md          │
        │ 4. Update other .md if needed   │
        └─────────────┬────────────────────┘
                      │
                      ▼
          ┌───────────────────────┐
          │ READY FOR REVIEW      │
          └───────────────────────┘
```

---

## 📁 File Purposes at a Glance

| File | Update When | Check When | Contains |
|:---|:---:|:---:|:---|
| **WHERE_WE_AT.md** | Completing milestones | Starting work | Status %, feature checklist, blockers, this week's changes |
| **ACTION_PLAN.md** | Implementing a phase | Need implementation details | Step-by-step guide, code examples, test procedures |
| **ARCHITECTURE.md** | Changing code structure | Designing a new feature | System design, API contracts, security, design patterns |
| **WHATS_OFF_ROAD.md** | Finding bugs, making decisions | Stuck or confused | Issues, blockers, design decisions, open questions |
| **CHANGELOG.md** | After completing something | Reviewing history | What changed, when, what files, why |
| **REVIEW_[VERSION].md** | After sprint reviews | Learning from feedback | What was reviewed, what passed, what needs fixing |
| **README.md (this file)** | New docs structure | Need guidance | How to use all the other files |

---

## 🔄 Weekly Update Checklist

Every Friday, before sprint wrap-up:

- [ ] **WHERE_WE_AT.md**: Update phase %, latest changes
- [ ] **ACTION_PLAN.md**: Add completion dates for finished phases
- [ ] **WHATS_OFF_ROAD.md**: Move resolved items to ✅ FIXED section
- [ ] **CHANGELOG.md**: Add key changes from the week
- [ ] **ARCHITECTURE.md**: Update if any major refactoring happened

---

## 💬 Example Workflows

### Workflow 1: Implementing a New Feature
```
1. Read: ACTION_PLAN.md → [Your Phase]
2. Read: ARCHITECTURE.md → relevant sections
3. Check: WHATS_OFF_ROAD.md for gotchas
4. Code!
5. When done:
   - Update ACTION_PLAN.md (add date + notes)
   - Update WHERE_WE_AT.md (phase % + feature list)
   - Add CHANGELOG.md entry
   - Commit with message: "Phase X: [Feature] — see action_plan.md"
```

### Workflow 2: Finding & Fixing a Bug
```
1. Write: WHATS_OFF_ROAD.md (🔴 issue description)
2. Branch: git checkout -b fix/issue-name
3. code fix
4. Update: WHATS_OFF_ROAD.md (mark ✅ FIXED)
5. Update: ACTION_PLAN.md (note the fix)
6. Update: WHERE_WE_AT.md (if blocking)
7. Commit: "Fix: [issue name] — see whats_off_road.md"
```

### Workflow 3: Code Review
```
Before Review:
1. Check: ACTION_PLAN.md (Phase section)
   - Are you implementing it correctly?
2. Check: ARCHITECTURE.md
   - Is your code structure right?
3. Check: WHATS_OFF_ROAD.md
   - Anything you should fix before review?

During Review:
1. Note feedback items
2. Create: REVIEW_[VERSION].md if major findings

After Review:
1. Update: WHATS_OFF_ROAD.md with feedback items
2. Update: ACTION_PLAN.md with notes
3. Fix code, update docs again
```

---

## 🎓 Tips for Using with Claude

### Tip 1: Paste Relevant Section
```
Claude: "I'm implementing Phase 5. Here's what the plan says:
[paste ACTION_PLAN.md Phase 5 section]

Here's my code so far:
[paste your code]

Should I also add..."
```

### Tip 2: Ask Claude to Verify Specs
```
"Per ARCHITECTURE.md section 4, my FoodRepository should...
I've done A, B, C. Am I missing anything?"
```

### Tip 3: Have Claude Help Update Docs
```
"I just implemented [feature]. Can you help me write an 
entry for CHANGELOG.md in the standard format?
Here's what changed: [list files/changes]"
```

### Tip 4: Check Architecture Consistency
```
"Is my new VoiceLogService consistent with the dependency 
injection pattern outlined in ARCHITECTURE.md?"
```

---

## ❓ Common Questions

**Q: Where do I write random notes?**  
A: If it's a decision/gotcha → WHATS_OFF_ROAD.md. If it's a significant change → CHANGELOG.md.

**Q: How detailed should ACTION_PLAN.md be?**  
A: Enough that someone else (or you in 6 months) can pick it up and build it.

**Q: When should I update ARCHITECTURE.md?**  
A: When the system design changes, not for every small code change.

**Q: Can I delete old CHANGELOG entries?**  
A: NO. Keep the full history. It's your project timeline.

**Q: How detailed should WHERE_WE_AT.md be?**  
A: Brief and scannable. People should get the status in 2 minutes.

---

## 🚀 You're Ready!

Now you have:
- ✅ **WHERE_WE_AT.md** — Status dashboard
- ✅ **ACTION_PLAN.md** — Implementation guide
- ✅ **ARCHITECTURE.md** — Design reference
- ✅ **WHATS_OFF_ROAD.md** — Issues & decisions
- ✅ **CHANGELOG.md** — Change history
- ✅ **README.md** — How to use all of these

**Start here**: Open WHERE_WE_AT.md and see current phase. Then open ACTION_PLAN.md for your phase.

Happy building! 🎯

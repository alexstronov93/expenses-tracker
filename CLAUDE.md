# Expense Tracker — project guide for Claude / agents

> Read this first. It's the fast-onboarding brief: what the project is, how it's
> built, the rules to follow, and where things live. Keep it up to date when the
> project changes.

## What this is

A **native Android app to track personal expenses**. A user adds expenses
(title, amount, category, note, date), sees a running total, and can edit or
delete them. Data persists locally on the device.

- **Repo:** `alexstronov93/expenses-tracker` (public)
- **Package / applicationId:** `com.stronov.expensetracker`
- **Platform:** Android only (phone). No backend, no accounts — all local.

## Current status

- ✅ Project scaffold complete and **building green on CI**.
- ✅ Implemented: add / edit / delete expense, running total, categories,
  Room persistence, Material 3 UI with light/dark + dynamic color.
- 🔜 Next: apply a **Figma design** (the owner will provide screens + tokens;
  will likely mean switching from dynamic color to a fixed palette in
  `ui/theme/Color.kt`). Then feature work: category filter, spending
  charts/summaries, CSV export, budgets.

## Tech stack

| Concern      | Choice                                             |
|--------------|----------------------------------------------------|
| Language     | Kotlin 2.0.21                                       |
| UI           | Jetpack Compose + Material 3                        |
| Architecture | MVVM — `ViewModel` + `StateFlow`                    |
| Persistence  | Room 2.6.1 (KSP)                                     |
| Navigation   | Navigation Compose                                  |
| Async        | Coroutines / Flow                                   |
| Build        | Gradle (Kotlin DSL) + version catalog, wrapper 8.11.1 |
| Toolchain    | JDK 17, AGP 8.7.3, `minSdk 26`, `compile/targetSdk 35` |

## Workflow rules (follow strictly)

- **Never commit directly to `master`.** Every change goes on a dedicated branch
  and merges via a pull request.
- **One branch per feature/fix**, prefixed: `feature/`, `fix/`, `chore/`,
  `docs/`, `refactor/` (see `CONTRIBUTING.md`).
- Open a PR into `master` using the template; the **PR Check** workflow
  (compile + unit tests) must be green before merge.
- Releases are automated — merging to `master` refreshes the `latest` APK.
  Never hand-create releases.

## Build, test, run

```bash
./gradlew testDebugUnitTest   # unit tests (JVM)
./gradlew assembleDebug       # compile / produce debug APK
./gradlew lintDebug           # Android Lint
```

> **In Claude Code web/remote sessions there is usually no local Android SDK.**
> Don't try to install it or run an emulator. Instead, let CI compile and build:
> push a branch / open a PR and read the **PR Check** result. The build output
> APK is downloadable from the run's artifacts, and merges publish it to the
> `latest` release.

## CI / release pipelines

- **`.github/workflows/pr-check.yml`** — on every PR: `assembleDebug` +
  `testDebugUnitTest` + lint; uploads a PR build APK and lint report. The merge gate.
- **`.github/workflows/release.yml`** — on push to `master`: rebuilds and
  refreshes the rolling **`latest`** pre-release with `ExpenseTracker-debug.apk`.
- **Install link (always current):**
  `https://github.com/alexstronov93/expenses-tracker/releases/download/latest/ExpenseTracker-debug.apk`

## Architecture & file map

```
app/src/main/java/com/stronov/expensetracker/
├── ExpenseApplication.kt     # App container / simple service locator (DB + repo)
├── MainActivity.kt           # Compose entry point; sets the theme + ExpenseApp()
├── data/                     # Room layer
│   ├── Expense.kt            # @Entity; amount stored as integer CENTS
│   ├── Category.kt           # enum of expense categories
│   ├── Converters.kt         # Room TypeConverters (Category <-> String)
│   ├── ExpenseDao.kt         # queries; returns Flow for observation
│   ├── ExpenseDatabase.kt    # RoomDatabase singleton
│   └── ExpenseRepository.kt  # wraps the DAO (single source of truth)
├── ui/
│   ├── ExpenseApp.kt         # NavHost + routes (list, add, edit)
│   ├── ExpenseViewModel.kt   # exposes StateFlow; add/update/delete actions
│   ├── ExpenseListScreen.kt  # list + running total + FAB
│   ├── AddEditExpenseScreen.kt # form with validation, category dropdown, delete
│   └── theme/                # Color.kt, Type.kt, Theme.kt  <-- design tokens live here
└── util/
    └── Money.kt              # cents <-> display string; parsing/formatting
```

Data flow: **Compose screen → collects `StateFlow` from `ExpenseViewModel` →
`ExpenseRepository` → `ExpenseDao` (Room)**. Writes go back the same way.

## Conventions

- **Money is always integer cents** — never floats. Convert via `util/Money.kt`.
- **Theming is centralized** in `ui/theme/`. Apply design changes there, not
  ad-hoc per screen.
- Add a **unit test** for non-trivial logic (see `MoneyTest`).
- Keep `master` releasable at all times.

## Useful links

- Repo: https://github.com/alexstronov93/expenses-tracker
- Actions: https://github.com/alexstronov93/expenses-tracker/actions
- Releases: https://github.com/alexstronov93/expenses-tracker/releases
- Contributing guide: `CONTRIBUTING.md`

# Project guide for Claude

Android app to track personal expenses. Kotlin + Jetpack Compose.

## Workflow rules (follow these strictly)

- **Never commit directly to `master`.** Every change goes on a dedicated branch
  and is merged via a pull request.
- **One branch per feature/fix**, named with a prefix: `feature/`, `fix/`,
  `chore/`, `docs/`, or `refactor/` (see `CONTRIBUTING.md`).
- Open a PR into `master` using the template, and make sure the **PR Check**
  workflow passes (compile + unit tests) before considering the change done.
- Releases are automated: pushing/merging to `master` refreshes the `latest`
  pre-release APK. Do not hand-create releases.

## Build & test

```bash
./gradlew testDebugUnitTest   # unit tests (JVM)
./gradlew assembleDebug       # compile the app / produce debug APK
./gradlew lintDebug           # Android Lint
```

Requires JDK 17 and Android SDK (compileSdk/targetSdk 35, minSdk 26).

## Architecture

- **UI:** Jetpack Compose + Material 3, screens in `ui/`. Navigation via
  Navigation Compose in `ui/ExpenseApp.kt`.
- **State:** `ui/ExpenseViewModel.kt` exposes `StateFlow`; screens collect it.
- **Data:** Room in `data/` (`Expense` entity, `ExpenseDao`, `ExpenseDatabase`,
  `ExpenseRepository`). Dependencies are wired in `ExpenseApplication.kt` (a
  simple service locator).
- **Money:** always stored as integer **cents**; convert with `util/Money.kt`.

## Conventions

- Package: `com.stronov.expensetracker`.
- Add a unit test when introducing non-trivial logic (see `MoneyTest`).
- Keep `master` releasable at all times.

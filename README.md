# Expense Tracker (Android)

A simple Android app to track personal expenses, built with **Kotlin** and
**Jetpack Compose**. This is the initial project scaffold: a working app you can
open in Android Studio, build, and run, with a clean architecture to grow from.

## Features (current scaffold)

- Add, edit, and delete expenses (title, amount, category, note, date)
- Running total of everything spent
- Categorized expenses with a Material 3 UI (light/dark + dynamic color)
- Local persistence with Room (survives app restarts)

## Tech stack

| Concern            | Choice                                    |
|--------------------|-------------------------------------------|
| Language           | Kotlin                                    |
| UI                 | Jetpack Compose + Material 3              |
| Architecture       | MVVM (ViewModel + StateFlow)             |
| Persistence        | Room                                      |
| Navigation         | Navigation Compose                        |
| Async              | Kotlin Coroutines / Flow                  |
| Build              | Gradle (Kotlin DSL) + version catalog     |

## Requirements

- Android Studio (Ladybug or newer recommended)
- JDK 17
- Android SDK with API 35 installed
- `minSdk` 26 (Android 8.0), `targetSdk`/`compileSdk` 35

## Getting started

1. Open the project folder in Android Studio (**File → Open**).
2. Let Gradle sync and download dependencies.
3. Pick an emulator or a connected device and press **Run**.

From the command line (once the Android SDK is configured via
`local.properties` or `ANDROID_HOME`):

```bash
./gradlew assembleDebug      # build a debug APK
./gradlew test               # run JVM unit tests
./gradlew installDebug       # install on a running device/emulator
```

> `local.properties` (with `sdk.dir=...`) is intentionally git-ignored. Android
> Studio generates it for you on first open.

## Project structure

```
app/src/main/java/com/stronov/expensetracker/
├── ExpenseApplication.kt        # App container / simple service locator
├── MainActivity.kt              # Compose entry point
├── data/                        # Room entities, DAO, database, repository
│   ├── Expense.kt
│   ├── Category.kt
│   ├── Converters.kt
│   ├── ExpenseDao.kt
│   ├── ExpenseDatabase.kt
│   └── ExpenseRepository.kt
├── ui/                          # Compose screens + ViewModel + navigation
│   ├── ExpenseApp.kt            # NavHost / routes
│   ├── ExpenseViewModel.kt
│   ├── ExpenseListScreen.kt
│   ├── AddEditExpenseScreen.kt
│   └── theme/                   # Color, Type, Theme
└── util/
    └── Money.kt                 # cents <-> display string helpers
```

Money is stored as an integer number of **cents** to avoid floating-point
rounding problems.

## Roadmap ideas

- Filter/summarize by category and by month
- Charts for spending trends
- Budgets and alerts
- Export to CSV
- Cloud sync / multi-device

## License

MIT — see [LICENSE](LICENSE).

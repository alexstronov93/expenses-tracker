# Contributing

This project uses a simple, branch-per-change workflow. `master` is always
kept in a working, releasable state.

## Golden rules

1. **Never commit directly to `master`.** All changes go through a pull request.
2. **One branch per feature/fix.** Keep PRs focused and reviewable.
3. **CI must be green before merging.** The app must compile and tests must pass.

## Branch naming

Use a short prefix describing the kind of change:

| Prefix     | Use for                          | Example                          |
|------------|----------------------------------|----------------------------------|
| `feature/` | new functionality                | `feature/category-filter`        |
| `fix/`     | bug fixes                        | `fix/negative-amount-crash`      |
| `chore/`   | build, CI, deps, tooling         | `chore/bump-agp`                 |
| `docs/`    | documentation only               | `docs/readme-screenshots`        |
| `refactor/`| internal changes, no behavior    | `refactor/extract-repository`    |

## Workflow

```bash
# 1. Start from an up-to-date master
git checkout master
git pull origin master

# 2. Create a branch for your change
git checkout -b feature/my-thing

# 3. Make changes, then verify locally
./gradlew test          # unit tests
./gradlew assembleDebug # app compiles

# 4. Commit and push
git commit -am "Add my thing"
git push -u origin feature/my-thing

# 5. Open a pull request into master (fill in the PR template)
```

## Continuous integration

Two GitHub Actions workflows run automatically:

- **PR Check** (`.github/workflows/pr-check.yml`) — runs on every pull request.
  Compiles the app (`assembleDebug`), runs unit tests, and reports Android Lint
  findings. This is the gate that must pass before merge.
- **Release APK** (`.github/workflows/release.yml`) — runs when `master` changes.
  Rebuilds and refreshes the rolling **`latest`** pre-release with an installable
  `ExpenseTracker-debug.apk`, so the download link is always current.

## Keeping master protected (repo admin)

To *enforce* the rules above, enable branch protection once in
**Settings → Branches → Add branch ruleset / protection rule** for `master`:

- Require a pull request before merging
- Require status checks to pass → select **PR Check / Compile, test & lint**
- (Optional) Require branches to be up to date before merging

## Project conventions

- **Language/UI:** Kotlin + Jetpack Compose + Material 3.
- **Architecture:** MVVM — Compose screens observe `StateFlow` from a `ViewModel`,
  which talks to a repository, which wraps the Room DAO.
- **Money is stored as integer cents** (never floats). Use `util/Money.kt` to
  parse/format.
- **Package:** `com.stronov.expensetracker`.
- Add a unit test when you add non-trivial logic.

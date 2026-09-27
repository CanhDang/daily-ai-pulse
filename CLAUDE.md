# Project Overview

You are a senior Android developer.
We are building an MVP native Android application called DailyAIPulse.

- Feature 1 (done): the first screen displays a list of articles fetched from an API.
- Feature 2 (planned): the second screen displays a list of the article sources, also fetched from an API.
- Feature 3 (planned): AI summarizes the articles for us.

The product will get more features in the future. The architecture must accommodate them.
The architecture must stay simple.

# Fundamental principles

- Write clean, simple and readable code
- Implement features in the simplest possible way
- Keep files small and focused
- Use clear, consistent naming
- Write simple, clean and modular code
- Use clear and easy-to-understand language. Write in short sentences.

# Error Fixing

- Do not jump to conclusions. Consider multiple possible causes before deciding.
- Explain the problem in plain English
- Make minimal necessary changes, changing as few lines as possible to fix the error
- In case of strange error you are not able to understand inform the user that he might need to consult other tools.

# Building Process

- To build the project run the command ./gradlew assembleDebug
- The shell has no Java installed. Use the JDK bundled with Android Studio:
  `export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"`
- Run unit tests: `./gradlew testDebugUnitTest`
- Install on a device or emulator: `./gradlew installDebug`
- adb lives at `~/Library/Android/sdk/platform-tools/adb`

# Comments

- Do not randomly delete comments. If you realise that a comment is outdated inform the user to verify it before deleting.
- Write comments only when we need to express the why we do something. When we include complex business logic. The what should be inferred by the clean code.

# Tech Stack

- Language: Kotlin 2.2.10 (AGP 9 built-in Kotlin, no `kotlin-android` plugin), Java 11 target
- Build: AGP 9.2.1, Gradle 9.4.1, version catalog in `gradle/libs.versions.toml`
- SDK: compileSdk 36.1, targetSdk 36, minSdk 26
- UI: Jetpack Compose (BOM 2026.02.01), Material 3
- DI: Hilt 2.60 with KSP 2.3.9, `androidx.hilt` 1.3.0 (`hiltViewModel()`)
- Networking: Retrofit 3.0.0, OkHttp 5.4.0 (+ logging interceptor), Moshi 1.15.2 with KSP codegen
- Images: Coil 3.4.0 (`coil-compose` + `coil-network-okhttp`)
- Async: Kotlin Coroutines 1.11.0, `StateFlow`, `collectAsStateWithLifecycle()` (lifecycle-runtime-compose 2.9.4)
- Logging: Timber 5.0.1
- Tests: JUnit 4, kotlinx-coroutines-test
- Included but not used yet: Navigation Compose 2.9.8

## Pinned versions (do not upgrade blindly)

The project compiles against SDK 36.1 with Kotlin 2.2.10. Newer libraries break the build:

- `lifecycle-runtime-compose` 2.10+ requires compileSdk 37. Keep 2.9.4.
- Coil 3.6.x requires compileSdk 37. Coil 3.5.0 pulls kotlin-stdlib 2.4.0, which Kotlin 2.2.10 cannot read. Keep 3.4.0.
- Before adding or upgrading a dependency, run `./gradlew assembleDebug` and check for `checkDebugAarMetadata` or Kotlin metadata errors.

# Architecture

Package by feature, then by layer. Each feature has three layers: `data`, `presentation`, `ui`.
Data flows in one direction: API -> Repository -> ViewModel -> Screen.

```
dd.canh.dailyaipulse
├── DailyAIPulseApp.kt        @HiltAndroidApp, plants Timber DebugTree in debug builds
├── MainActivity.kt           @AndroidEntryPoint, hosts the Compose content
├── di/
│   ├── NetworkModule.kt      OkHttpClient, Moshi, Retrofit, feature APIs
│   └── AppModule.kt          app-wide objects (java.time.Clock)
├── articles/                 Feature 1
│   ├── data/
│   │   ├── ArticleData.kt        API model (+ SourceData), Moshi @JsonClass
│   │   ├── ArticlesResponse.kt   API response wrapper
│   │   ├── ArticleApi.kt         Retrofit interface
│   │   └── ArticleRepository.kt  @Singleton, returns List<ArticleData>
│   ├── presentation/
│   │   ├── Article.kt            UI model
│   │   ├── ArticleMapper.kt      ArticleData -> Article (relative dates)
│   │   ├── ArticleUIState.kt     Loading / Success / Error
│   │   └── ArticleViewModel.kt   @HiltViewModel, exposes StateFlow<ArticleUIState>
│   └── ui/
│       ├── ArticleScreen.kt      stateful ArticleScreen + stateless ArticleScreenContent
│       └── ArticleItem.kt        one list row + @Preview
└── ui/theme/                 Material 3 theme (DailyAIPulseTheme)
```

## Layer rules

- `data`: API interfaces, API models (`XData`, `XResponse`) and repositories. No Android UI code.
- `presentation`: UI models, mappers, UI state and ViewModels. All presentation logic lives here (formatting, error messages).
- `ui`: Composables only. They render a UI state. No business logic.
- The UI never sees `XData` models. The ViewModel maps them to UI models first.

## Naming conventions (follow them for new features)

| Kind | Name | Example |
|------|------|---------|
| API model | `XData` | `ArticleData` |
| API response | `XsResponse` | `ArticlesResponse` |
| Retrofit interface | `XApi` | `ArticleApi` |
| Repository | `XRepository` | `ArticleRepository` |
| UI model | `X` | `Article` |
| Mapper | `XData.toX()` in `XMapper.kt` | `ArticleData.toArticle(clock)` |
| UI state | `XUIState` sealed interface | `ArticleUIState` |
| ViewModel | `XViewModel` | `ArticleViewModel` |
| Screen | `XScreen` + `XScreenContent` | `ArticleScreen` |
| List row | `XItem` | `ArticleItem` |

## Key patterns

- JSON field renames use `@param:Json(name = "...")` on constructor parameters. `@Json` alone gives Kotlin warnings.
- Nullable API fields stay nullable in the models (`description`, `imageUrl`, `source.id`).
- UI state is a sealed interface: `Loading`, `Success(data)`, `Error(message)`.
- The ViewModel starts as `Loading`, loads in `init`, and exposes a public `loadX()` for retry.
- Every state change goes through one `emitState()` function that logs the state with Timber.
- Error handling: catch `CancellationException` first and re-throw it. Then catch `Exception`, log it with `Timber.e(e, ...)` and emit a user-friendly message: "Something went wrong, please try again later". Never show technical errors on screen.
- Dates: `ArticleMapper` maps ISO dates to "Today", "Yesterday" or "X days ago", counting calendar days in the device time zone. Unparseable dates fall back to the raw value.
- Anything time-based takes an injected `java.time.Clock`, so tests can fix "now".
- Screens are split in two: the stateful one gets the ViewModel with `hiltViewModel()`, the stateless one takes only the UI state. Previews and tests use the stateless one.
- Images: Coil `AsyncImage`. Skip the image when the URL is null or blank. A gray `ColorPainter` shows when loading fails.

# API and Configuration

- API: NewsAPI (`https://newsapi.org/`). Articles come from `GET v2/top-headlines?country=us`.
- The API key is `NEWS_API_KEY` in `~/.gradle/gradle.properties`. It is never committed.
- Gradle exposes it as `BuildConfig.NEWS_API_KEY`. This needs `buildFeatures { buildConfig = true }` (AGP 9 disables it by default).
- `NetworkModule` adds the key as the `X-Api-Key` header on every request.
- HTTP logging is `BODY` in debug and `NONE` in release. The key header is redacted from logs.
- Timber only logs in debug builds.
- The manifest declares the `INTERNET` permission and `android:name=".DailyAIPulseApp"`.

# Adding a New Feature

Use the `articles` feature as the template. For example, Feature 2 (sources):

1. Create `sources/data/`: `SourceData`, `SourcesResponse`, `SourceApi`, `SourceRepository`.
2. Provide the new API in `di/NetworkModule.kt` (`retrofit.create()`).
3. Create `sources/presentation/`: `Source`, `SourceMapper`, `SourceUIState`, `SourceViewModel`.
4. Create `sources/ui/`: `SourceScreen`, `SourceItem`.
5. Add tests in `app/src/test/.../sources/presentation/` with a fake API.
6. Build and run the tests.

Notes:

- `SourceData` already exists in `articles/data/ArticleData.kt` (the `source` object of an article). Pick a name that does not clash, or reuse it on purpose.
- There is no navigation yet. `MainActivity` shows `ArticleScreen` directly. A second screen needs navigation (Navigation Compose is already a dependency).

# Testing

- Unit tests live in `app/src/test/java/dd/canh/dailyaipulse/<feature>/presentation/`.
- Do not use a mocking library. Fake the API interface (see `FakeArticleApi`) and use the real repository.
- ViewModel tests: `Dispatchers.setMain(StandardTestDispatcher())`, `runTest`, `advanceUntilIdle()`.
- Mapper tests: pass `Clock.fixed(...)` with a known time zone.

# Troubleshooting

- `Unresolved reference 'BuildConfig'` in Android Studio while Gradle builds fine: sync Gradle, then Make Project.
- `NoClassDefFoundError: KotlinSymbolProcessing$ExitCode` (or `KSPLoader`) in `kspDebugKotlin`: stale Gradle daemon. Run `./gradlew --stop` and build again.
- `requires ... compile against version 37` in `checkDebugAarMetadata`: the library is too new. Downgrade it (see Pinned versions).
- `Module was compiled with an incompatible version of Kotlin`: a dependency pulled in a newer kotlin-stdlib. Downgrade that dependency.

# Git

- Branch: `main`. Remote: `origin` (https://github.com/CanhDang/ai-android-course).
- `.gitignore` covers build output, `.gradle/`, `.kotlin/`, `local.properties`, `.idea/*` (except `codeStyles/`), keystores and `.claude/settings.local.json`.
- Never commit API keys or signing keys.

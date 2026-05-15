# Quick Reference Guide - Virasat-Namma

## 🚀 Get Started in 2 Minutes

### Step 1: Open in Android Studio
```
1. Launch Android Studio
2. File → Open → Select Virasat-Namma folder
3. Wait for Gradle sync (3-5 min)
```

### Step 2: Run the App
```
1. Click Run button (green play ▶️)
2. Select emulator or device
3. App launches in ~2 minutes
```

### Step 3: Explore Features
- **Home**: Swipe through heritage sites
- **Site Details**: Tap any site card
- **Check-In**: Click "Check-In" button
- **Passport**: View visited sites
- **Scanner**: See QR demo

---

## 📂 Project Structure at a Glance

```
data/           → Database & Repository (M in MVVM)
ui/             → Screens & Components (V in MVVM)
viewmodel/      → Logic & State (VM in MVVM)
navigation/     → App navigation
utils/          → Helper functions
```

---

## 🎯 Key Files to Know

| Need to Change? | Edit This File |
|-----------------|---|
| Colors | `ui/theme/Color.kt` |
| Fonts/Sizes | `ui/theme/Type.kt` |
| Site Data | `data/repository/HeritageRepository.kt` |
| Home Screen | `ui/screens/HomeScreen.kt` |
| Site Details | `ui/screens/SiteDetailsScreen.kt` |
| Button Styles | `ui/components/Buttons.kt` |
| Navigation | `navigation/Navigation.kt` |
| Database | `data/local/Database.kt` |

---

## 💻 Essential Commands

```bash
# Build app
./gradlew build

# Run app on device
./gradlew installDebug

# Clean everything
./gradlew clean

# Check dependencies
./gradlew dependencies
```

---

## 🏗️ MVVM Pattern

```
┌─ View (UI) ─────────────────┐
│  HomeScreen.kt              │
│  Shows UI, observes state   │
└─────────────┬───────────────┘
              ↓
┌─ ViewModel (Logic) ─────────┐
│  HomeViewModel.kt           │
│  Manages state, business    │
│  logic                      │
└─────────────┬───────────────┘
              ↓
┌─ Data (Repository) ────────┐
│  HeritageRepository.kt      │
│  Fetches from DB or API     │
└─────────────────────────────┘
```

---

## 🌈 Color Scheme

| Color | Hex Code | Usage |
|-------|----------|-------|
| **Saffron** | #FF9800 | Primary buttons |
| **Gold** | #FFD700 | Accents |
| **Beige** | #D4A574 | Secondary |
| **Dark Brown** | #5D4037 | Text |

---

## 📱 Screen Navigation

```
Home Screen
├─ Tap Site Card → Site Details Screen
├─ Bottom Nav (Scanner) → QR Scanner Screen
│   └─ Scan Success → Site Details
└─ Bottom Nav (Passport) → Digital Passport Screen
```

---

## 🗂️ Adding New Feature

### 1. Create Data Model
```kotlin
// In data/local/Entities.kt
data class MyFeature(val id: String, val name: String)
```

### 2. Create DAO
```kotlin
// In data/local/Daos.kt
@Dao
interface MyFeatureDao {
    @Query("SELECT * FROM features")
    fun getAll(): Flow<List<MyFeature>>
}
```

### 3. Create ViewModel
```kotlin
// In viewmodel/MyViewModel.kt
class MyViewModel(repo: Repository) : ViewModel() {
    val state = MutableStateFlow<State>(State.Loading)
}
```

### 4. Create Screen
```kotlin
// In ui/screens/MyScreen.kt
@Composable
fun MyScreen(viewModel: MyViewModel) {
    // Compose UI
}
```

### 5. Add Route
```kotlin
// In navigation/Navigation.kt
sealed class NavRoute {
    object MyScreen : NavRoute("my_screen")
}
```

---

## 🔍 Common Tasks

### Change App Name
```xml
<!-- In app/src/main/res/values/strings.xml -->
<string name="app_name">New Name</string>
```

### Change App Colors
```kotlin
// In ui/theme/Color.kt
val Saffron = Color(0xFFFF9800)  // Change this
```

### Add New Screen
```
1. Create MyScreen.kt in ui/screens/
2. Create MyViewModel.kt in viewmodel/
3. Add route in navigation/Navigation.kt
4. Add NavHost composable
```

### Modify Sample Data
```kotlin
// In data/repository/HeritageRepository.kt
private fun generateSampleSites(): List<SiteEntity> {
    // Edit this function
}
```

### Update Dependencies
```gradle
// In app/build.gradle
dependencies {
    implementation 'group:artifact:version'
}
```

---

## 🐛 Debugging Tips

### View App Logs
```
Android Studio → View → Tool Windows → Logcat
```

### Clear App Data
```bash
adb shell pm clear com.virasatnamma
```

### View Database
```
Android Studio → View → Tool Windows → Database Inspector
```

### Check Memory
```
Android Studio → View → Tool Windows → Profiler
```

---

## 📱 Test on Device

### Enable USB Debugging
1. Settings → About Phone
2. Tap "Build Number" 7 times
3. Settings → Developer Options → USB Debugging (ON)

### Connect & Install
```bash
adb devices           # Verify connection
./gradlew installDebug  # Install app
```

---

## 🎨 Component Reuse Examples

### Use Existing Buttons
```kotlin
PrimaryButton(
    text = "Check-In",
    onClick = { /* action */ }
)

AudioPlayButton(
    isPlaying = state.isPlaying,
    onToggle = { /* action */ }
)
```

### Use Existing Cards
```kotlin
HeritageLocationCard(
    locationState = site,
    onCardClick = { /* action */ }
)
```

### Use Theme
```kotlin
Text(
    text = "Title",
    style = MaterialTheme.typography.headlineMedium,
    color = VirasatColors.DarkBrown
)
```

---

## 🔄 State Management Pattern

```kotlin
// Define state
private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// Update state
viewModelScope.launch {
    try {
        val data = fetchData()
        _uiState.value = UiState.Success(data)
    } catch (e: Exception) {
        _uiState.value = UiState.Error(e.message)
    }
}

// Read state in UI
val state = viewModel.uiState.collectAsState().value
when (state) {
    is UiState.Loading → ShowSpinner()
    is UiState.Success → ShowData(state.data)
    is UiState.Error → ShowError(state.message)
}
```

---

## 📦 Key Dependencies

| Library | Use | Import |
|---------|-----|--------|
| Jetpack Compose | UI | `androidx.compose.*` |
| Room | Database | `androidx.room.*` |
| Firebase | Cloud | `com.google.firebase.*` |
| Coil | Images | `coil.compose.*` |
| Navigation | Routes | `androidx.navigation.*` |
| ViewModel | State | `androidx.lifecycle.*` |

---

## 🚨 Error Handling Pattern

```kotlin
viewModelScope.launch {
    try {
        val result = repository.getData()
        _state.value = State.Success(result)
    } catch (e: IOException) {
        _state.value = State.Error("Network error")
    } catch (e: Exception) {
        _state.value = State.Error(e.message ?: "Unknown error")
    }
}
```

---

## 📊 Database Quick Reference

### Query Examples
```kotlin
// Get all
getSiteDao().getAllSites()

// Filter
getSiteDao().getSitesByCategory("Temple")

// Single item
getSiteDao().getSiteById("site_001")

// Search
getSiteDao().searchSites("%query%")

// Count
getSiteDao().getSiteCount()
```

---

## 🔗 Navigation Quick Reference

```kotlin
// Navigate to screen
navController.navigate(NavRoute.Home.route)

// Navigate with parameter
navController.navigate(
    NavRoute.SiteDetails.createRoute("site_001")
)

// Go back
navController.popBackStack()

// Get parameter
val siteId = backStackEntry.arguments?.getString("siteId")
```

---

## 🎯 Performance Tips

1. **Use LazyColumn** for lists (not Column)
2. **Use remember {}** for expensive operations
3. **Use keys in lists** for LazyColumn/LazyRow
4. **Avoid GlobalScope** (use viewModelScope)
5. **Use `.collectAsState()`** for StateFlow
6. **Memoize composables** with remember

---

## 📚 Documentation Files

| File | Content |
|------|---------|
| README.md | Overview & features |
| INSTALLATION_GUIDE.md | Setup instructions |
| FIREBASE_SETUP.md | Cloud configuration |
| ARCHITECTURE_GUIDE.md | Design patterns |
| PROJECT_SUMMARY.md | Complete reference |

---

## 🎓 Learning Path

1. **Understand MVVM** → Read ARCHITECTURE_GUIDE.md
2. **Set up project** → Follow INSTALLATION_GUIDE.md
3. **Explore code** → Open `ui/screens/HomeScreen.kt`
4. **Modify colors** → Edit `ui/theme/Color.kt`
5. **Add new screen** → Create in `ui/screens/`
6. **Run tests** → `./gradlew test`

---

## ✅ Pre-Flight Checklist

Before deploying:
- [ ] Build succeeds: `./gradlew build`
- [ ] No errors in Logcat
- [ ] App launches on emulator
- [ ] All screens navigable
- [ ] Check-in saves to DB
- [ ] No memory leaks (Profiler)
- [ ] Runs on real device

---

## 🤝 Contributing Code

1. Follow MVVM pattern
2. Add KDoc comments
3. Handle errors with try-catch
4. Use viewModelScope (not GlobalScope)
5. Make composables stateless
6. Use LazyColumn for lists
7. Test on emulator/device

---

## 📞 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| Won't build | `./gradlew clean build` |
| Emulator slow | Use hardware acceleration |
| No sites showing | Check database initialization |
| App crashes | Check Logcat for errors |
| Navigation broken | Verify NavHost routes |

---

## 🎉 Quick Wins

- ✅ Change app name (1 file)
- ✅ Change colors (1 file)
- ✅ Add new site (1 function)
- ✅ Modify layout (1 file)
- ✅ Add new button (1 component)

---

## 📞 Need Help?

1. **Build issues** → Run `./gradlew clean`
2. **Runtime errors** → Check Logcat
3. **Architecture questions** → See ARCHITECTURE_GUIDE.md
4. **Setup questions** → See INSTALLATION_GUIDE.md

---

**Happy coding! Keep it simple, keep it MVVM! 🚀**

**Last Updated**: May 2026

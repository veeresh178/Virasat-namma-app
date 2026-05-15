# MVVM Architecture & Development Notes

## Architecture Overview

The Virasat-Namma app follows **MVVM (Model-View-ViewModel)** pattern with clean separation of concerns.

```
┌─────────────────────────────────────────────────┐
│              UI Layer (Jetpack Compose)         │
│  ┌──────────────────────────────────────────┐   │
│  │  HomeScreen, SiteDetailsScreen, etc.     │   │
│  │  (Stateless, Composable functions)       │   │
│  └──────────────────────────────────────────┘   │
│                       ↑                         │
│                  Observes & Updates            │
│                       ↓                         │
├─────────────────────────────────────────────────┤
│        ViewModel Layer (State Management)       │
│  ┌──────────────────────────────────────────┐   │
│  │  HomeViewModel                           │   │
│  │  SiteDetailsViewModel                    │   │
│  │  ScannerViewModel                        │   │
│  │  PassportViewModel                       │   │
│  │  (StateFlow, LiveData)                   │   │
│  └──────────────────────────────────────────┘   │
│                       ↑                         │
│              Business Logic & State             │
│                       ↓                         │
├─────────────────────────────────────────────────┤
│        Data Layer (Repository Pattern)          │
│  ┌──────────────────────────────────────────┐   │
│  │  HeritageRepository                      │   │
│  │  (Single source of truth)                │   │
│  └──────────────────────────────────────────┘   │
│         ↙                              ↖        │
│    Data Access                    Remote API    │
│        ↓                              ↓        │
│  ┌──────────────┐            ┌──────────────┐  │
│  │  Room DB     │            │  Firebase    │  │
│  │  (Local)     │            │  Firestore   │  │
│  │  - SiteDao   │            │  (Cloud)     │  │
│  │  - CheckInDao│            │              │  │
│  │  - Database  │            │              │  │
│  └──────────────┘            └──────────────┘  │
└─────────────────────────────────────────────────┘
```

---

## Data Flow Example

### Example: User Checking-In at a Site

```
User taps "Check-In" button on SiteDetailsScreen
         ↓
SiteDetailsScreen calls viewModel.performCheckIn()
         ↓
SiteDetailsViewModel.performCheckIn() {
  viewModelScope.launch {
    repository.addCheckIn(siteId, siteName)
    _isVisited.value = true
  }
}
         ↓
HeritageRepository.addCheckIn() {
  insert into Room DB ← CheckInEntity
  update Firebase ← (optional async)
}
         ↓
SiteDetailsScreen observes _isVisited StateFlow
         ↓
UI updates: Button shows "✓ Visited"
         ↓
PassportViewModel observes check-in changes
         ↓
PassportScreen shows updated visit count
```

---

## File Structure Explanation

### Data Layer

**`data/local/Entities.kt`**
- Room database entities
- Defines database schema
- @Entity decorated classes
- Example: SiteEntity, CheckInEntity

**`data/local/Daos.kt`**
- Database access objects
- Query methods for each entity
- @Dao, @Query, @Insert, @Delete decorators
- Example: SiteDao, CheckInDao

**`data/local/Database.kt`**
- Room database initialization
- Singleton pattern
- Database migration management

**`data/repository/HeritageRepository.kt`**
- Data access abstraction
- Combines local + remote data
- Business logic for data
- Example: `getAllSites()`, `addCheckIn()`

### UI Layer

**`ui/theme/`**
- `Color.kt`: Color palette definition
- `Type.kt`: Typography (fonts, sizes)
- `Theme.kt`: Theme composition

**`ui/components/`**
- `Cards.kt`: HeritageLocationCard, HeritagePreviewCard
- `Buttons.kt`: PrimaryButton, AudioPlayButton, etc.
- `Banners.kt`: InfoBanner, ErrorBanner
- Reusable UI components

**`ui/screens/`**
- `HomeScreen.kt`: Site discovery feed
- `SiteDetailsScreen.kt`: Full site information
- `ScannerScreen.kt`: QR code scanner
- `PassportScreen.kt`: Digital passport + progress

### ViewModel Layer

**`viewmodel/HomeViewModel.kt`**
- Manages home screen state
- Calculates distances
- Filters sites by category
- StateFlow: uiState, nearestSites

**`viewmodel/SiteDetailsViewModel.kt`**
- Single site details management
- Language toggle state
- Audio playback state
- Check-in logic

**`viewmodel/ScannerViewModel.kt`**
- QR scan result processing
- Navigation to scanned site
- Error handling

**`viewmodel/PassportViewModel.kt`**
- Passport data collection
- Visit history management
- Progress calculation

---

## State Management Pattern

### StateFlow vs LiveData

```kotlin
// StateFlow (Recommended for Compose)
private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// Usage in Compose
val uiState = viewModel.uiState.collectAsState().value

// LiveData (Legacy)
private val _data = MutableLiveData<String>()
val data: LiveData<String> = _data

// Usage (works with Compose too)
val data = viewModel.data.observeAsState()
```

### UI State Management

```kotlin
// Sealed class for UI states
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val sites: List<LocationCardState>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

// Usage in UI
when (uiState) {
    is HomeUiState.Loading -> { /* show spinner */ }
    is HomeUiState.Success -> { /* show sites */ }
    is HomeUiState.Error -> { /* show error */ }
}
```

---

## Room Database Usage

### Entity Definition
```kotlin
@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    // ... other fields
)
```

### DAO Query Examples

```kotlin
// Insert
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertSite(site: SiteEntity)

// Query all
@Query("SELECT * FROM sites ORDER BY name ASC")
fun getAllSites(): Flow<List<SiteEntity>>

// Query with filter
@Query("SELECT * FROM sites WHERE category = :category")
fun getSitesByCategory(category: String): Flow<List<SiteEntity>>

// Delete all
@Query("DELETE FROM sites")
suspend fun clearAllSites()
```

### Database Transactions

```kotlin
// Multiple operations together
@Transaction
@Query("SELECT * FROM sites")
fun getSitesWithCheckIns(): Flow<List<SiteWithCheckIns>>
```

---

## Jetpack Compose Best Practices

### 1. **Composable Functions**

```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    // Remember recomposition
    val sites = viewModel.sites.collectAsState().value
    
    LazyColumn {
        items(sites) { site ->
            HeritageLocationCard(site)
        }
    }
}
```

### 2. **State Management in Compose**

```kotlin
// Don't do this (resets on recomposition)
var count = 0  // ❌ Wrong

// Do this
val (count, setCount) = remember { mutableStateOf(0) }  // ✅ Right
```

### 3. **Reusable Components**

```kotlin
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}
```

### 4. **Lazy Lists**

```kotlin
// For large lists, use LazyColumn (like RecyclerView)
LazyColumn {
    items(sites) { site ->
        SiteCard(site)
    }
}
```

### 5. **Key for Lists**

```kotlin
// Always provide key for recomposition optimization
LazyColumn {
    items(sites, key = { it.id }) { site ->
        SiteCard(site)
    }
}
```

---

## ViewModels - Key Concepts

### 1. **Scope Safety**

```kotlin
class HomeViewModel(private val repository: HeritageRepository) : ViewModel() {
    // viewModelScope lives as long as ViewModel
    // Auto-cancelled when ViewModel is destroyed
    fun loadData() {
        viewModelScope.launch {
            // Safe: won't execute if ViewModel destroyed
            val data = repository.getData()
        }
    }
}
```

### 2. **Avoiding Memory Leaks**

```kotlin
// ✅ Correct - Scope bound to ViewModel lifecycle
viewModelScope.launch { }

// ❌ Wrong - GlobalScope never cancels
GlobalScope.launch { }
```

### 3. **StateFlow vs SharedFlow**

```kotlin
// StateFlow (has initial value, multicast)
val state = MutableStateFlow(initialValue)

// SharedFlow (no initial value, more flexible)
val events = MutableSharedFlow<Event>()
```

---

## Testing Architecture

### Unit Test Example

```kotlin
@Test
fun testDistanceCalculation() {
    val distance = LocationUtils.calculateDistance(
        lat1 = 15.3352,
        lon1 = 76.4745,
        lat2 = 12.3052,
        lon2 = 76.6245
    )
    assertTrue(distance > 0)
}
```

### ViewModel Test Example

```kotlin
@Test
fun testLoadSiteDetails() = runTest {
    val viewModel = SiteDetailsViewModel(mockRepository)
    viewModel.loadSiteDetails("site_001")
    
    val state = viewModel.siteDetails.value
    assertEquals("site_001", state?.id)
}
```

---

## Error Handling Strategy

### 1. **Try-Catch in Coroutines**

```kotlin
viewModelScope.launch {
    try {
        val data = repository.fetchData()
        _uiState.value = UiState.Success(data)
    } catch (e: Exception) {
        _uiState.value = UiState.Error(e.message ?: "Unknown error")
    }
}
```

### 2. **Custom Error Classes**

```kotlin
sealed class DataError {
    object NetworkError : DataError()
    object DatabaseError : DataError()
    data class UnknownError(val message: String) : DataError()
}
```

### 3. **Audio Player Error Handling**

```kotlin
audioManager.play(audioUrl) { error ->
    _uiState.value = UiState.Error("Audio play failed: $error")
}
```

---

## Coroutines Best Practices

### 1. **Use viewModelScope**

```kotlin
// ✅ Automatically cancelled when ViewModel destroyed
viewModelScope.launch {
    val data = repository.getData()
}
```

### 2. **Structured Concurrency**

```kotlin
// ✅ Parent coroutine waits for all children
viewModelScope.launch {
    val site = async { repository.getSite(id) }
    val visits = async { repository.getVisits(id) }
    
    val siteWithVisits = site.await() to visits.await()
}
```

### 3. **Error Propagation**

```kotlin
// ✅ Errors propagate to parent
viewModelScope.launch {
    try {
        launch { riskyOperation() }
    } catch (e: Exception) {
        handleError(e)
    }
}
```

---

## Navigation Architecture

### Route Definition

```kotlin
sealed class NavRoute(val route: String) {
    object Home : NavRoute("home")
    object SiteDetails : NavRoute("site_details/{siteId}") {
        fun createRoute(siteId: String) = "site_details/$siteId"
    }
}
```

### Navigation Usage

```kotlin
// Navigate to site details
navController.navigate(NavRoute.SiteDetails.createRoute("site_001"))

// Pop back
navController.popBackStack()

// Deep linking
navController.navigate("site_details/site_001")
```

---

## Performance Optimization

### 1. **Lazy Composition**

```kotlin
// ✅ Only visible items are composed
LazyColumn {
    items(1000) { index ->
        HeavyComponent(index)  // Only composed when visible
    }
}

// ❌ All items composed (bad for large lists)
Column {
    repeat(1000) { index ->
        HeavyComponent(index)  // All composed at once
    }
}
```

### 2. **Remember Expensive Operations**

```kotlin
@Composable
fun ExpensiveScreen() {
    // ❌ Recalculated every recomposition
    val expensiveList = calculateExpensiveList()
    
    // ✅ Calculated once, remembered
    val expensiveList = remember {
        calculateExpensiveList()
    }
}
```

### 3. **Stable Data Classes**

```kotlin
@Stable  // Tells Compose this class won't change
data class SiteData(
    val id: String,
    val name: String
)
```

---

## Debugging Tips

### 1. **Logcat in Android Studio**
```
View → Tool Windows → Logcat
Search for: "virasatnamma"
Filter levels: Error, Warning, Info
```

### 2. **Database Inspector**
```
Device Manager → Select device
View → Tool Windows → Database Inspector
Browse: app/virasat_namma_db
```

### 3. **Compose Preview**
```kotlin
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(viewModel = mockViewModel)
}
```

### 4. **Recomposition Highlighting**
```
Settings → Developer Services → Show Recompositions
```

---

## Future Enhancements Architecture

### Dependency Injection with Hilt

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HeritageRepository
) : ViewModel() {
    // Automatically provided by Hilt
}

// In Activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Hilt handles injection
}
```

### Repository Pattern with Network Layer

```kotlin
class HeritageRepository(
    private val localDataSource: SiteDao,
    private val remoteDataSource: FirebaseService
) {
    // Fetch from network, cache locally
    suspend fun getSites(): List<HeritageLocation> {
        return try {
            remoteDataSource.fetchSites().also { sites ->
                localDataSource.insertSites(sites)
            }
        } catch (e: Exception) {
            localDataSource.getAllSites()
        }
    }
}
```

---

## Code Quality Checklist

- [ ] All functions have KDoc comments
- [ ] Error handling implemented
- [ ] No null pointer exceptions
- [ ] StateFlow used for UI state
- [ ] ViewModels own their data
- [ ] No memory leaks (no GlobalScope)
- [ ] Composables are stateless
- [ ] LazyColumn used for long lists
- [ ] Images are properly cached (Coil)
- [ ] Database queries optimized

---

**Architecture is the foundation of maintainable, scalable code! 🏗️**

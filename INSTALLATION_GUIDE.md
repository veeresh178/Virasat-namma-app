# Virasat-Namma Installation & Running Guide

## 🎯 Quick Start

### Option 1: Android Studio (Recommended)

#### Prerequisites
- **Java Development Kit (JDK)**: 17 or higher
- **Android Studio**: Hedgehog (2023.1.1) or later
- **Android SDK**: API 34
- **Gradle**: 8.0+

#### Installation Steps

1. **Download Android Studio**
   - Go to https://developer.android.com/studio
   - Download and install Android Studio

2. **Open Project**
   - Launch Android Studio
   - Click "Open an existing Android Studio project"
   - Navigate to `Virasat-Namma` folder
   - Click "Open"

3. **Wait for Gradle Sync**
   - Android Studio will automatically download dependencies
   - This may take 3-5 minutes on first run
   - Check "Messages" tab for progress

4. **Configure Virtual Device (Emulator)**
   - Click "Device Manager" on right sidebar
   - Click "Create device"
   - Select device (e.g., "Pixel 6 Pro")
   - Select API Level 34 or higher
   - Click "Next" → "Finish"

5. **Run the App**
   - Click "Run" button (green play icon) or press `Shift + F10`
   - Select your virtual device
   - Click "OK"
   - App will build and launch (~1-2 minutes)

---

### Option 2: VS Code (Advanced Setup)

#### Prerequisites
- **VS Code**: Latest version
- **JDK 17+**
- **Android SDK** (Command-line tools)
- **Gradle**: 8.0+

#### Installation Steps

1. **Install Required VS Code Extensions**
   ```
   - Extension Pack for Java (Microsoft)
   - Gradle for Java (Microsoft)
   - Android Tools (Elias Sclar)
   - Kotlin Language
   ```

   Or paste these IDs in Extensions:
   ```
   vscjava.vscode-java-pack
   vscjava.vscode-gradle
   aar-android-tools
   fwcd.kotlin
   ```

2. **Install Android SDK (Command-line)**

   **Windows:**
   ```powershell
   # Install using Chocolatey (if available)
   choco install android-sdk
   
   # Or download from: https://developer.android.com/studio#downloads
   # Download "Command line tools only"
   
   # Extract to: C:\Android\cmdline-tools\latest\
   ```

   **macOS:**
   ```bash
   brew install android-sdk
   ```

   **Linux:**
   ```bash
   sudo apt-get install android-sdk
   ```

3. **Set Environment Variables**

   **Windows (PowerShell):**
   ```powershell
   $env:ANDROID_SDK_ROOT = "C:\Android"
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
   ```

   **macOS/Linux:**
   ```bash
   export ANDROID_SDK_ROOT=~/Library/Android/sdk
   export JAVA_HOME=$(/usr/libexec/java_home -v 17)
   ```

4. **Open Project in VS Code**
   ```bash
   code Virasat-Namma/
   ```

5. **Build Project**
   - Press `Ctrl+Shift+P` (Windows) or `Cmd+Shift+P` (Mac)
   - Type "Gradle: Build"
   - Select "Gradle: Build" task

6. **Create Android Virtual Device**
   ```bash
   sdkmanager "system-images;android-34;google_apis;arm64-v8a"
   avdmanager create avd -n VirasatEmulator -k "system-images;android-34;google_apis;arm64-v8a" -d pixel_6_pro
   ```

7. **Start Emulator**
   ```bash
   emulator -avd VirasatEmulator
   ```

8. **Run App**
   ```bash
   ./gradlew installDebug
   ```

---

## 📱 Running on Physical Device

### Prerequisites
- Android phone with **Android 7.0+**
- USB cable
- USB debugging enabled on phone

### Steps

1. **Enable Developer Mode on Phone**
   - Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back, open "Developer Options"
   - Enable "USB Debugging"

2. **Connect Phone via USB**
   - Connect phone to computer
   - Allow USB debugging prompt on phone

3. **Verify Connection**
   ```bash
   adb devices
   ```
   - Should show your device as "device"

4. **Build & Install**

   **Android Studio:**
   - Click Run button, select your device

   **VS Code / Terminal:**
   ```bash
   ./gradlew installDebug
   adb shell am start -n com.virasatnamma/.MainActivity
   ```

5. **App Should Launch**
   - Look for "Virasat-Namma" on home screen
   - Tap to open

---

## 🛠️ Build & Compile Commands

### Debug APK (For Testing)
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (For Deployment)
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

### App Bundle (For Play Store)
```bash
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab
```

### Clean Build
```bash
./gradlew clean build
```

### Run Tests
```bash
./gradlew test
```

### Check Lint Issues
```bash
./gradlew lint
```

---

## ⚙️ Environment Configuration

### Set JAVA_HOME

**Windows Registry Method:**
```
JAVA_HOME = C:\Program Files\Java\jdk-17
```

**Windows Terminal/PowerShell:**
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
echo $env:JAVA_HOME
```

**Verify:**
```bash
java -version
```

### Set ANDROID_SDK_ROOT

**Windows:**
```powershell
$env:ANDROID_SDK_ROOT = "C:\Android"
```

**macOS:**
```bash
export ANDROID_SDK_ROOT=~/Library/Android/sdk
```

**Linux:**
```bash
export ANDROID_SDK_ROOT=~/Android/Sdk
```

**Verify:**
```bash
ls $ANDROID_SDK_ROOT/platforms
```

---

## 🧪 Testing the App

### First Launch Checklist

- [ ] App starts without crashing
- [ ] Home screen displays site cards
- [ ] Can scroll through sites
- [ ] Site filter works (click categories)
- [ ] Tap a site card → opens details screen
- [ ] Language toggle works (EN/KN)
- [ ] Check-in button responds
- [ ] Scanner screen shows camera icon
- [ ] Passport screen displays progress

### Sample Test Scenarios

**Test 1: Browse Sites**
1. Launch app
2. Scroll through site list
3. Filter by "Temple"
4. Verify only temple sites show

**Test 2: Check-In**
1. Tap a site card
2. Click "Check-In" button
3. Button changes to "✓ Visited"
4. Go to Passport screen
5. Verify site appears in visits list

**Test 3: Language Toggle**
1. Open site details
2. Click language toggle button
3. Text changes to Kannada
4. Click again → changes back to English

**Test 4: Audio Guide**
1. Open site details
2. Click "Play Audio Guide" button (demo won't actually play)
3. Button changes to "Pause Audio Guide"
4. Click again → changes back to "Play"

---

## 🐛 Troubleshooting

### "Gradle sync failed"
**Solution:**
```bash
./gradlew clean
./gradlew build
```

### "Java not found" or "JAVA_HOME not set"
**Windows:**
```powershell
# Check Java installation
java -version

# If not installed, download JDK 17 from:
# https://www.oracle.com/java/technologies/downloads/
```

### "Android SDK not found"
```bash
# Download SDK through Android Studio:
# - Android Studio → Settings → Appearance & Behavior → System Settings → Android SDK
# - Download API 34, Build Tools 34.0.0

# Or via command line:
sdkmanager "platforms;android-34"
sdkmanager "build-tools;34.0.0"
```

### "Emulator won't start"
```bash
# Kill existing emulator processes
killall emulator

# Clear emulator cache
emulator -avd VirasatEmulator -wipe-data

# Restart
emulator -avd VirasatEmulator
```

### "App crashes on startup"
1. Check Logcat in Android Studio: View → Tool Windows → Logcat
2. Look for red "E/" error messages
3. Common causes:
   - Database not initialized
   - Missing permissions
   - Corrupted cache

**Fix:**
```bash
# Clear app data
adb shell pm clear com.virasatnamma

# Reinstall
./gradlew installDebug
```

### "No mock data showing"
**Solution:**
- First launch automatically initializes sample data
- If still empty, check Room database in Android Studio
- Device → Data → Data Storage → com.virasatnamma → Virasat-Namma DB

---

## 📊 Development Workflow

### Daily Development

1. **Edit code** in Android Studio / VS Code
2. **Save changes** (Ctrl+S / Cmd+S)
3. **Hot reload** (Android Studio) or rebuild (VS Code)
4. **View Logcat** for debug messages
5. **Test on emulator/device**

### Debugging

**Android Studio Debugger:**
1. Set breakpoints (click line numbers)
2. Click "Debug 'app'" button
3. App will pause at breakpoints
4. Inspect variables, step through code

**Logcat Monitoring:**
```bash
# Real-time logs
adb logcat | grep "virasatnamma"

# Filtered by level
adb logcat *:E  # Errors only
adb logcat *:W  # Warnings only
```

---

## 📦 Dependencies Management

### View Dependencies
```bash
./gradlew dependencies
```

### Check for Updates
```bash
./gradlew dependencyUpdates
```

### Update Dependencies
Edit `app/build.gradle` and change version numbers, then:
```bash
./gradlew build
```

---

## 🚀 Deployment Checklist

Before releasing to Play Store:

- [ ] Test on real device
- [ ] Check all screens for UI issues
- [ ] Verify internet connectivity handling
- [ ] Test with Firebase (if integrated)
- [ ] Check for crash reports in Logcat
- [ ] Update version number in `app/build.gradle`
- [ ] Create release notes
- [ ] Sign release APK/AAB
- [ ] Upload to Google Play Console

---

## 📚 Useful Resources

| Resource | Link |
|----------|------|
| Android Docs | https://developer.android.com/docs |
| Jetpack Compose | https://developer.android.com/jetpack/compose |
| Room Database | https://developer.android.com/training/data-storage/room |
| Firebase | https://firebase.google.com/docs |
| Kotlin Docs | https://kotlinlang.org/docs |
| Material Design 3 | https://m3.material.io |

---

## 💡 Pro Tips

1. **Fast Build**: Use `./gradlew assemble --parallel`
2. **Skip Tests**: `./gradlew build -x test`
3. **View All Tasks**: `./gradlew tasks`
4. **Incremental Build**: Delete only `app/build/`, keep `.gradle/`
5. **Use Gradle Daemon**: Already enabled, speeds up builds
6. **Profile Build**: `./gradlew build --profile`

---

## 📞 Support

If you encounter issues:
1. Check Logcat for error messages
2. Check Android Studio event log
3. Try `./gradlew clean`
4. Restart Android Studio
5. Invalidate caches (File → Invalidate Caches)

---

**Happy coding! 🎉**

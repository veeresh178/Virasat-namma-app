# Firebase Configuration Guide

## Step-by-Step Firebase Setup

### 1. Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Click "Create a project"
3. Project name: `Virasat-Namma`
4. Enable Google Analytics (optional)
5. Click "Create project"

### 2. Register Android App

1. In Firebase console, click "Add app"
2. Select "Android"
3. Fill in details:
   - **Package name**: `com.virasatnamma`
   - **App nickname**: `Virasat-Namma Android`
   - **SHA-1 hash**: (Get from next step)

### 3. Get SHA-1 Fingerprint

Run this command in project root:

**Windows (PowerShell)**:
```powershell
./gradlew signingReport
```

**macOS/Linux**:
```bash
./gradlew signingReport
```

Look for:
```
SHA1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
```

Copy the SHA1 value (without colons) to Firebase console.

### 4. Download google-services.json

1. In Firebase console, after SHA-1 is verified, click "Download google-services.json"
2. Save it to: `Virasat-Namma/app/google-services.json`
3. Android Studio should auto-detect it

### 5. Enable Firebase Services

In Firebase Console > Project Settings:

#### Firestore Database
1. Click "Firestore Database"
2. Click "Create database"
3. Select "Start in production mode"
4. Choose region (e.g., "asia-south1" for India)
5. Click "Enable"

#### Authentication
1. Go to "Authentication"
2. Click "Get started"
3. Enable "Anonymous" (for guest login):
   - Click "Anonymous"
   - Toggle "Enable"
   - Click "Save"

#### Storage (Optional)
1. Go to "Storage"
2. Click "Get started"
3. Select region same as Firestore
4. Click "Create"

### 6. Create Firestore Collections & Rules

#### Create Collections

**Collection 1: `sites`**
- Document: `site_001`
  ```
  {
    "name": "Virupaksha Temple",
    "description_en": "Ancient temple...",
    "description_kn": "ಪ್ರಾಚೀನ...",
    "category": "Temple",
    "latitude": 15.3180,
    "longitude": 76.7597,
    "imageUrl": "https://...",
    "audioUrl": "https://...",
    "yearEstablished": 740
  }
  ```

**Collection 2: `user_checkins`** (auto-created on first check-in)

#### Update Firestore Rules

In Firestore > Rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Public read access to sites
    match /sites/{document=**} {
      allow read: if true;
      allow write: if false; // Prevent client-side writes
    }
    
    // User check-ins
    match /user_checkins/{userId}/{document=**} {
      allow read: if request.auth.uid == userId;
      allow create: if request.auth.uid == userId;
      allow update, delete: if request.auth.uid == userId && 
        resource.data.userId == userId;
    }
  }
}
```

### 7. Enable Firestore API

If not already enabled:
1. Go to "Project Settings" > "APIs & Services"
2. Search for "Cloud Firestore API"
3. Click "Enable"

### 8. Get Firebase Configuration

In Firebase Console > Project Settings:
- Click "Your apps"
- Under Android, copy:
  - **API Key**
  - **Project ID**
  - **Storage Bucket**

Update in code if needed:
```kotlin
// Already handled by google-services.json
// Firebase SDK auto-initializes
```

### 9. Test Firebase Connection

Add debug code to MainActivity:

```kotlin
import com.google.firebase.firestore.FirebaseFirestore

val db = FirebaseFirestore.getInstance()
db.collection("sites").get().addOnSuccessListener { documents ->
    Log.d("Firebase", "Sites loaded: ${documents.size()}")
}.addOnFailureListener { e ->
    Log.e("Firebase", "Error", e)
}
```

## Firebase Service Integration Code

### Firestore Service Class (Optional)

Create `app/src/main/java/com/virasatnamma/data/remote/FirebaseService.kt`:

```kotlin
package com.virasatnamma.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.virasatnamma.data.local.CheckInEntity
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val db = FirebaseFirestore.getInstance()
    
    suspend fun syncCheckIn(userId: String, checkIn: CheckInEntity) {
        try {
            db.collection("user_checkins")
                .document(userId)
                .collection("visits")
                .add(checkIn.toMap())
                .await()
        } catch (e: Exception) {
            throw e
        }
    }
    
    suspend fun getRemoteSites() {
        db.collection("sites")
            .get()
            .await()
    }
}

private fun CheckInEntity.toMap(): Map<String, Any> {
    return mapOf(
        "siteId" to siteId,
        "siteName" to siteName,
        "timestamp" to timestamp
    )
}
```

## Testing Firebase Locally

### Firebase Emulator Suite (Development)

1. Install Firebase CLI:
   ```bash
   npm install -g firebase-tools
   ```

2. Initialize Firebase:
   ```bash
   firebase init emulators
   ```

3. Start emulators:
   ```bash
   firebase emulators:start
   ```

4. Point app to emulator:
   ```kotlin
   val settings = FirebaseFirestoreSettings.Builder()
       .setHost("10.0.2.2:8080") // Android emulator
       .setSslEnabled(false)
       .build()
   FirebaseFirestore.getInstance().firestoreSettings = settings
   ```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `google-services.json not found` | Ensure file is in `app/` folder |
| `SHA1 mismatch error` | Verify SHA-1 in Firebase console matches `signingReport` |
| `Firestore permission denied` | Check Firestore rules, ensure auth is enabled |
| `API not enabled` | Enable Cloud Firestore API in Google Cloud Console |
| `No internet connection` | Check device network, Firebase connectivity |

## Security Checklist

- ✅ Download `google-services.json` safely (don't commit to public repos)
- ✅ Use `.gitignore` to exclude `google-services.json`
- ✅ Enable Firestore rules before production
- ✅ Use Firebase Authentication for user data
- ✅ Never expose API keys in code
- ✅ Set up Firebase monitoring

## Next Steps

1. ✅ Complete Firebase setup
2. ✅ Test connection from app
3. ✅ Deploy Firestore rules
4. ✅ Set up Cloud Functions (for advanced features)
5. ✅ Monitor with Firebase Analytics

---

**Firebase Documentation**: https://firebase.google.com/docs

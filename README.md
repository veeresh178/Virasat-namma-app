# Virasat-Namma - Premium Heritage Tourism App

A premium, immersive Android application for discovering the rich heritage of Karnataka, inspired by traditional South Indian temple architecture and the historical grandeur of the Vijayanagara Empire.

## 🏛️ Design Philosophy: "The Digital Gateway"
Virasat-Namma is a cultural immersion. The UI/UX has been transformed to evoke the feeling of walking through ancient stone corridors and reading sacred scrolls, moving away from a standard utility app to a premium tourism experience.

### 🎨 Visual Identity
- **Premium Palette**: Deep Saffron, Antique Gold, Temple Brown, and Dark Maroon accents on a Soft Cream/Parchment background.
- **Aesthetics**: Stone-carved tablet cards, parchment-style info surfaces, and decorative "Mandala" procedural patterns.
- **Typography**: Elegant Serif headings for a royal historical atmosphere combined with clean, readable body text.

---

## 📱 Premium Features

### 1. **Cinematic Site Discovery** 🗺️
- **Stone Tablet UI**: Heritage sites are displayed on cards with layered depth and subtle floating animations.
- **Procedural Backgrounds**: Dynamic Mandala patterns drawn in real-time to create a sacred atmosphere.
- **Nearby Discovery**: Instant distance calculations with a "Deep Saffron" proximity badge for sites within 5km.

### 2. **Immersive Site Details** 📖
- **Ken Burns Hero Banner**: Large images with smooth zoom effects and parallax scrolling for a cinematic feel.
- **Parchment Information**: History and legends presented on ancient scroll-style surfaces.
- **Sacred Secrets**: Expandable "Stone Fact" cards for hidden historical details.

### 3. **Royal Audio Guide** 🎧
- **Ambient Glow Navigation**: The audio button "pulses" with light while playing, providing a visual cue for the guided tour.
- **Bilingual Narratives**: High-quality Text-to-Speech support in both English and Kannada.

### 4. **Mystic QR Gateway** 📷
- **Portal Scanner**: A mystical, pulsing interface for site check-ins at heritage locations.
- **Instant Identification**: Point at site QR codes to unlock historical details and earn your "Verified Seal."

### 5. **Digital Passport & Achievement Scroll** 🎫
- **Royal Progress**: Track your pilgrimage across Karnataka's heritage sites with a percentage completion indicator.
- **Verified Seals**: Golden seals added to your "Pilgrimage Log" for every monument visited.

---

## 🧩 Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Kotlin |
| **Architecture** | MVVM (Modern, Scalable) |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Animations** | Compose Animation, Lottie, Ken Burns Effects |
| **Image Loading** | Coil |
| **Local Database** | Room Database |
| **Cloud Database** | Firebase Firestore |
| **QR Scanning** | ML Kit Barcode Scanning |
| **Navigation** | Jetpack Navigation (Floating Temple Bar) |

---

## ⚡ Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Kotlin 1.9.0+
- Android SDK 34
- Firebase Project (for cloud features)

### Installation
1. **Clone the Project**
2. **Open in Android Studio** and let Gradle sync complete.
3. **Configure Firebase**: Place your `google-services.json` in the `app/` folder to enable cloud sync and check-ins.
4. **Build and Run**: Shift + F10 to launch the "Digital Gateway" on your device.

## 📂 Project Structure
```
Virasat-Namma/
├── app/src/main/java/com/virasatnamma/
│   ├── data/           # Room & Firebase data layers
│   ├── ui/
│   │   ├── screens/    # Redesigned Premium Screens
│   │   ├── components/ # Stone-carved Cards, Shimmer, & Mandala Decorations
│   │   └── theme/      # Royal Heritage Colors & Serif Typography
│   ├── viewmodel/      # MVVM Business Logic
│   └── navigation/     # Floating Bottom Navigation
└── README.md
```

## 🔐 Security & Permissions
- `INTERNET`: For cloud sync and high-quality image loading.
- `ACCESS_FINE_LOCATION`: For proximity-based heritage discovery.
- `CAMERA`: For the Mystic QR Gateway scanner.

---

**Version**: 2.0.0 (Premium Heritage Edition)  
**Author**: Senior Android Developer & UI/UX Designer

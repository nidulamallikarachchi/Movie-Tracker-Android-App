# 🎬 Movie Tracker

Movie Tracker is an Android app built with Kotlin that allows users to easily track movies they have watched and want to watch. The app features user profiles, customizable movie preferences, and dynamic watchlists. It integrates with Firebase for data storage and uses smooth animations to enhance the user experience.

---

## 📱 Features

- **Watchlist Management**: Add movies you plan to watch to a personalized watchlist.
- **Watched Movies**: Keep track of movies you've already watched and rate them.
- **User Profiles**: Edit your profile, including uploading a profile picture and setting movie preferences.
- **Smooth Transitions**: Enjoy smooth fade-in and fade-out transitions between screens.
- **Dark Mode**: Experience the app in dark mode, providing a sleek and modern UI.
- **Lottie Animations**: Engage with fun Lottie animations when interacting with the app.

---

## 🚀 Technologies Used

- **Kotlin**: For building the app.
- **Firebase Firestore**: To store user data, movie preferences, and watchlists.
- **Firebase Storage**: To handle user profile pictures.
- **Retrofit**: For handling API calls to fetch movie data.
- **Room Database**: To cache movie data locally.
- **Lottie**: For smooth and engaging animations.
- **Picasso**: To load and display user profile images.
- **Bottom Navigation**: To easily navigate between Home, Watched, Watchlist, and Profile sections.

---

## 💻 Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/movie-tracker.git
   cd movie-tracker
   ```

2. Open the project in Android Studio.

3. Sync Gradle files to download all dependencies.

4. Set up Firebase:
   - Add your `google-services.json` to the `app/` folder.
   - Enable Firestore and Firebase Storage in your Firebase console.

5. Build and run the app on an Android device or emulator.

---

## 📂 Project Structure

```bash
├── adapters/               # RecyclerView adapters
├── fragments/              # App Fragments (Home, Watchlist, Watched, Profile)
├── models/                 # Data models (Movie, UserProfile)
├── network/                # Retrofit API services
├── activities/             # App Activities (MainActivity, EditUserProfileActivity)
├── res/
│   ├── anim/               # Animation files (fade_in.xml, fade_out.xml)
│   ├── layout/             # XML layouts for activities and fragments
│   └── raw/                # Lottie animation files
└── MainActivity.kt         # MainActivity with Bottom Navigation
```

---

## 📖 Key Features Breakdown

### 1. **User Profiles**
Users can update their profiles, including uploading profile pictures to Firebase Storage and selecting movie preferences using a multi-choice dialog.

### 2. **Watched & Watchlist Movies**
Users can add movies to their watched or watchlist. Movies are stored in Firebase Firestore, and Lottie animations are triggered when a movie is deleted from the watched list.

### 3. **Fragment Transitions**
Fragment transitions are animated with a smooth fade-in and fade-out effect, enhancing the user experience when navigating between different sections of the app.

### 4. **Dark Mode**
The app features dark mode to improve user experience in low-light environments, with the UI optimized for readability and elegance in dark themes.

---

## 🔄 Fragment Transition Animations

```xml
<!-- fade_in.xml -->
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="400"
    android:fromAlpha="0.0"
    android:toAlpha="1.0" />

<!-- fade_out.xml -->
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="400"
    android:fromAlpha="1.0"
    android:toAlpha="0.0" />
```

---

## ⚡ Dependencies

```toml
[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
retrofit = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore", version.ref = "firebaseFirestore" }
firebase-storage = { group = "com.google.firebase", name = "firebase-storage", version.ref = "firebaseStorage" }
lottie = { group = "com.airbnb.android", name = "lottie", version.ref = "lottie" }
picasso = { module = "com.squareup.picasso:picasso", version.ref = "picasso" }
```

---

## 👤 Author

- **Nidula Mallikarachchi** - Developer of Movie Tracker.

Feel free to contribute by forking the repository and submitting pull requests.


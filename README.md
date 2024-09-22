# WallpicK - Wallpaper App

WallpicK is a feature-rich wallpaper application that allows users to browse, upload, and manage wallpapers. The app is built using modern Android development practices with Jetpack Compose and Firebase integration for storage, authentication, and Firestore database management.

## Features

- **Browse Wallpapers**
    - View a wide collection of wallpapers filtered by categories, popularity, and most downloaded.
    - Filters include:
        - Latest Wallpapers
        - Popular Wallpapers
        - Most Downloaded Wallpapers
    - Wallpapers are displayed with relevant information like resolution, tags, and colors.
      
- **Google Sign-In Integration**
    - Secure login using Google Sign-In with Firebase Authentication.
    - User-specific personalization and wallpaper management.
      
- **Firebase Storage Integration**
    - Upload wallpapers to Firebase Storage with metadata like categories, resolution, and tags.
    - Automatic file URL generation and storage in Firestore.
      
- **Firestore Database**
    - Manage wallpapers and category data stored in Firestore.
    - Metadata such as views, downloads, and tags are stored and updated for each wallpaper.

- **Categories & Backgrounds**
    - Each wallpaper is associated with a category and displayed with the appropriate background.
    - Categories are dynamically updated based on the uploaded wallpapers.
      
- **Dynamic Filters**
    - Apply dynamic filters based on the wallpaper's metadata (e.g., resolution, tags, views, and downloads).
    - Easily switch between different filters like latest, most popular, or most downloaded wallpapers.
      
- **Firestore Query-Based Filtering**
    - Wallpapers can be queried and filtered based on tags, views, or download count for a customized experience.
      
- **Favorites**
    - Mark wallpapers as favorites for easy access.
      
- **Offline Mode**
    - View wallpapers in offline mode once they are cached locally.
 
## Tech Stack

- **Kotlin** - For Android development.
- **Jetpack Compose** - Modern UI toolkit for building native Android UIs.
- **Firebase** - Backend services including:
    - Firebase Authentication
    - Firebase Firestore
    - Firebase Storage
- **Room Database** - Local database to cache wallpapers for offline access.
- **Coil** - For image loading and caching.
- **Dagger-Hilt** - Dependency Injection.

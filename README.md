# MoviesApp
Created Movies App using jetpack compose used Retrofit for Network calls
# Movies App

A modern Android application that allows users to browse movies using TMDB API. The app showcases trending movies, different movie categories, and detailed movie information including trailers and cast details.

## Project Structure

```
└── app/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/example/moviesapp/
    │   │   │   ├── data/
    │   │   │   │   ├── api/
    │   │   │   │   │   └── MovieApiService.kt
    │   │   │   │   ├── model/
    │   │   │   │   │   ├── Movie.kt
    │   │   │   │   │   ├── MovieResponse.kt
    │   │   │   │   │   └── CastMember.kt
    │   │   │   │   └── repository/
    │   │   │   │       └── MovieRepository.kt
    │   │   │   ├── di/
    │   │   │   │   └── AppModule.kt
    │   │   │   ├── ui/
    │   │   │   │   ├── theme/
    │   │   │   │   │   └── Theme.kt
    │   │   │   │   └── viewModel/
    │   │   │   │       └── MovieViewModel.kt
    │   │   │   └── MainActivity.kt
    │   │   └── res/
    │   └── test/
    └── build.gradle
```

## Features

- Trending Movies Display
- Movie Categories (Now Playing, Popular, Top-Rated, Upcoming)
- Detailed Movie Information
- Cast Information
- YouTube Trailer Integration
- Responsive Design

## Tech Stack & Libraries

### Core
- Kotlin
- Jetpack Compose - Modern UI toolkit
- Android Architecture Components
  - ViewModel
  - LiveData
- Coroutines - For asynchronous programming

### Dependency Injection
- Hilt - For dependency injection

### Networking
- Retrofit - REST API client
- OkHttp3 - HTTP client
- Gson - JSON parsing

### Image Loading
- Coil - Image loading library

### Video Player
- ExoPlayer - Media player
- YouTube Player - For trailer playback

### Architecture
- MVVM (Model-View-ViewModel)
- Repository Pattern
- Clean Architecture principles

## Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- Minimum SDK 24
- Target SDK 34
- Kotlin 1.9.0 or newer

### Configuration
1. Clone the repository
2. Open project in Android Studio
3. Add your TMDB API key in `local.properties`:
   ```
   tmdb_api_key=your_api_key_here
   ```
4. Sync project with Gradle files
5. Run the app

## Architecture

The app follows MVVM architecture pattern:

- **View Layer** (UI)
  - Activities/Composables
  - Observes ViewModel
  - Handles UI logic

- **ViewModel Layer**
  - Holds UI-related data
  - Handles UI logic
  - Communicates with Repository

- **Repository Layer**
  - Single source of truth
  - Handles data operations
  - Manages API calls

- **Data Layer**
  - API Service
  - Response Models
  - Remote Data Source

## API Integration

The app uses TMDB API for fetching movie data:
- Base URL: `https://api.themoviedb.org/3/`
- Endpoints used:
  - Trending Movies
  - Now Playing Movies
  - Popular Movies
  - Top-Rated Movies
  - Upcoming Movies
  - Movie Details
  - Cast Details
  - Movie Trailers

## Known Issues & Limitations

- Limited offline support
- No local data persistence
- Video player requires active internet connection
- Limited error handling for edge cases

## Future Improvements

- Implement Room database for offline support
- Add search functionality
- Implement favorites feature
- Add more comprehensive error handling
- Improve UI/UX with animations
- Add unit tests and UI tests

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details

SS OF THE APP


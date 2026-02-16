# Mini-App Mobile Application (Android)

## Overview
This is the Android mobile application built with Kotlin and Jetpack Compose that connects to the same Spring Boot backend as the web application.

## Features
- **Login Screen** - User authentication with email and password
- **Register Screen** - New user registration with username, email, and password
- **Dashboard/Profile Screen** - Protected screen showing user profile information
- **Logout Functionality** - Secure logout with token management
- **JWT Authentication** - Token-based authentication with the backend
- **Persistent Login** - Auto-login on app restart if token is valid

## Technology Stack
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material3** - Material Design components
- **Retrofit** - REST API client
- **OkHttp** - HTTP client with logging
- **Coroutines & Flow** - Asynchronous programming
- **Navigation Component** - In-app navigation
- **ViewModel** - UI state management
- **SharedPreferences** - Local token storage

## Architecture
```
app/src/main/java/com/it342/miniapp/
├── data/
│   ├── api/
│   │   ├── ApiService.kt          # Retrofit API interface
│   │   └── RetrofitClient.kt      # Retrofit configuration
│   └── model/
│       ├── AuthResponse.kt        # Authentication response DTO
│       ├── LoginRequest.kt        # Login request DTO
│       ├── RegisterRequest.kt     # Registration request DTO
│       └── UserDTO.kt             # User data DTO
├── navigation/
│   ├── AppNavigation.kt           # Navigation graph
│   └── Screen.kt                  # Screen routes
├── ui/
│   ├── screen/
│   │   ├── DashboardScreen.kt     # Dashboard/Profile UI
│   │   ├── LoginScreen.kt         # Login UI
│   │   └── RegisterScreen.kt      # Registration UI
│   ├── theme/                     # App theme configuration
│   └── viewmodel/
│       ├── DashboardViewModel.kt  # Dashboard logic
│       ├── LoginViewModel.kt      # Login logic
│       └── RegisterViewModel.kt   # Registration logic
├── utils/
│   └── TokenManager.kt            # JWT token management
└── MainActivity.kt                # App entry point
```

## Configuration

### Backend URL Configuration
Before running the app, configure the backend URL in [RetrofitClient.kt](../app/src/main/java/com/it342/miniapp/data/api/RetrofitClient.kt):

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080"  // For Android Emulator
// OR
private const val BASE_URL = "http://YOUR_COMPUTER_IP:8080"  // For Physical Device
```

**Important Notes:**
- **Android Emulator**: Use `http://10.0.2.2:8080` (10.0.2.2 maps to localhost on your computer)
- **Physical Device**: Use your computer's IP address (e.g., `http://192.168.1.100:8080`)
  - Find your IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
  - Ensure your device and computer are on the same network
  - Make sure the backend allows connections from your IP

### Backend Setup
Ensure the Spring Boot backend is running on port 8080:
```bash
cd backend
./mvnw spring-boot:run
```

## Running the App

### Prerequisites
- Android Studio (latest version)
- JDK 8 or higher
- Android SDK (API 24 or higher)
- Android device or emulator

### Steps to Run
1. Open the `mobile` folder in Android Studio
2. Wait for Gradle sync to complete
3. Configure the backend URL in `RetrofitClient.kt`
4. Select a device/emulator
5. Click Run (or press Shift+F10)

### Building APK
```bash
cd mobile
./gradlew assembleDebug
```
APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

## API Endpoints Used

The mobile app uses the same backend API as the web application:

- **POST** `/api/auth/register` - Register new user
- **POST** `/api/auth/login` - User login
- **GET** `/api/user/me` - Get current user (protected)
- **POST** `/api/auth/logout` - User logout

## User Flow

1. **First Time User**
   - App opens on Login screen
   - Click "Register" to create account
   - Fill in username, email, password, confirm password
   - On success, automatically navigates to Dashboard

2. **Returning User**
   - If token exists, app opens directly on Dashboard
   - Click "Logout" to return to Login screen

3. **Session Management**
   - Token stored in SharedPreferences
   - Auto-attached to API requests via Interceptor
   - Auto-logout on token expiration

## UI Layouts

The mobile app screens match the web application layouts:

### Login Screen
- Email input field
- Password input field
- Login button
- Link to Register screen

### Register Screen
- Username input field
- Email input field
- Password input field
- Confirm Password input field
- Register button
- Link to Login screen

### Dashboard Screen
- Header with "Dashboard" title and Logout button
- Profile information cards:
  - Username
  - Email
  - Member Since (formatted date)

## Dependencies

Key dependencies (defined in [libs.versions.toml](../gradle/libs.versions.toml)):
- Retrofit 2.9.0
- OkHttp 4.11.0
- Gson 2.10.1
- Navigation Compose 2.7.0
- Compose BOM 2023.08.00
- Material3

## Troubleshooting

### Network Connection Issues
1. **Emulator can't reach backend**
   - Use `http://10.0.2.2:8080` instead of `localhost`
   - Check if backend is running: `curl http://localhost:8080/api/auth/login`

2. **Physical device can't reach backend**
   - Get your computer's IP address
   - Update `BASE_URL` in RetrofitClient.kt
   - Ensure firewall allows port 8080
   - Ensure both devices are on the same network

### Build Issues
1. **Gradle sync failed**
   - File → Invalidate Caches / Restart
   - Ensure internet connection for downloading dependencies

2. **Kotlin version mismatch**
   - Check `kotlin` version in `libs.versions.toml` matches Android Studio

### Runtime Issues
1. **App crashes on launch**
   - Check Logcat for error messages
   - Verify backend URL is correct
   - Ensure INTERNET permission is in AndroidManifest.xml

2. **Login fails**
   - Verify backend is running
   - Check network logs in Logcat (HttpLoggingInterceptor shows requests)
   - Verify credentials are correct

## Testing

### Manual Testing Checklist
- [ ] Register new user
- [ ] Login with created user
- [ ] View Dashboard with user info
- [ ] Logout
- [ ] Login again with same credentials
- [ ] Close app and reopen (should auto-login)
- [ ] Verify date formatting on Dashboard

## Future Enhancements
- Remember email on login screen
- Password strength indicator
- Email verification
- Profile picture upload
- Edit profile functionality
- Biometric authentication
- Offline support

## License
This project is part of the Mini-App suite.

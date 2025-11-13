# Pill Reminder App
## Ứng dụng Nhắc Nhở Uống Thuốc

Dự án được phát triển cho học phần **Lập trình Thiết bị Di động** - Ứng dụng nhắc nhở uống thuốc giúp người dùng quản lý và theo dõi lịch uống thuốc hàng ngày một cách hiệu quả.

## 👥 Thành viên nhóm

**Nhóm 8** - Học phần Lập trình Thiết bị Di động
    Nguyễn Dương Minh Hy
    Thịnh 
    Dũng

---

## 📋 Giới thiệu ngắn

**Pill Reminder App** là ứng dụng Android được xây dựng để giúp người dùng:
- Quản lý thông tin các loại thuốc cần uống với form chi tiết (tên, liều lượng, đơn vị, lời khuyên uống)
- Thiết lập lịch nhắc nhở uống thuốc tự động với nhiều thời điểm trong ngày
- Theo dõi lịch sử uống thuốc với trạng thái (taken/missed/skipped)
- Xem thống kê và báo cáo về việc tuân thủ lịch uống thuốc qua biểu đồ và danh sách
- Nhận thông báo nhắc nhở kịp thời qua WorkManager
- Onboarding flow thân thiện cho người dùng mới

Ứng dụng được thiết kế với giao diện hiện đại, màu sắc nhẹ nhàng (light blue theme), dễ sử dụng và đảm bảo tính ổn định cao trong việc nhắc nhở người dùng.

---

## 🛠 Kiến thức và Công nghệ

### Ngôn ngữ lập trình
- **Kotlin** - Ngôn ngữ chính được sử dụng trong toàn bộ dự án

### Giao diện người dùng (UI)
- **Jetpack Compose** - Framework hiện đại để xây dựng giao diện Android
- **Material 3** - Design system tiên tiến của Google, đảm bảo trải nghiệm người dùng nhất quán và đẹp mắt

### Kiến trúc ứng dụng
- **MVVM (Model-View-ViewModel)** - Mô hình kiến trúc giúp tách biệt logic nghiệp vụ và giao diện, dễ bảo trì và test

### Điều hướng
- **Navigation Compose** - Quản lý điều hướng giữa các màn hình trong ứng dụng

### Cơ sở dữ liệu
- **Room Database** - Thư viện persistence của Android, cung cấp abstraction layer cho SQLite
  - Lưu trữ thông tin thuốc (MedicineEntity)
  - Lưu trữ lịch sử uống thuốc (HistoryEntity)
  - DAO (Data Access Object) để thao tác với database

### Xử lý bất đồng bộ
- **Kotlin Coroutines** - Xử lý các tác vụ bất đồng bộ một cách hiệu quả
- **Flow** - Reactive streams để quản lý dữ liệu theo thời gian thực

### Quản lý Dependency Injection
- **Hilt** - Dependency injection framework dựa trên Dagger, giúp quản lý dependencies dễ dàng và rõ ràng

### Nhắc nhở (Lõi chức năng)
- **WorkManager** - Quản lý các tác vụ nền đáng tin cậy, đảm bảo thông báo nhắc nhở được gửi đúng thời gian
- **ReminderWorker** - Worker xử lý logic gửi thông báo nhắc nhở

### Quyền truy cập (Permissions)
- **POST_NOTIFICATIONS** - Quyền hiển thị thông báo (Android 13+)
- **SCHEDULE_EXACT_ALARM** - Quyền lên lịch báo thức chính xác (Android 12+)
- **USE_EXACT_ALARM** - Quyền sử dụng báo thức chính xác

---

## 📁 Cấu trúc Project Chi tiết

```
Pill-Reminder-App/
├── app/
│   ├── build.gradle.kts                          # Cấu hình build cho module app
│   ├── proguard-rules.pro                        # Quy tắc ProGuard cho release build
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml                # Cấu hình manifest của ứng dụng
│       │   ├── java/nhom8/uth/pillreminderapp/
│       │   │   │
│       │   │   ├── PillReminderApplication.kt     # Application class với Hilt
│       │   │   ├── MainActivity.kt                # Activity chính của ứng dụng
│       │   │   │
│       │   │   ├── data/                          # Package chứa logic data layer
│       │   │   │   ├── AppDatabase.kt             # Room database configuration
│       │   │   │   ├── database/
│       │   │   │   │   ├── dao/                   # Data Access Objects
│       │   │   │   │   │   ├── HistoryDao.kt      # DAO cho lịch sử uống thuốc
│       │   │   │   │   │   └── MedicineDao.kt     # DAO cho thông tin thuốc
│       │   │   │   │   └── entity/                # Database entities
│       │   │   │   │       ├── HistoryEntity.kt   # Entity lịch sử uống thuốc
│       │   │   │   │       └── MedicineEntity.kt  # Entity thông tin thuốc
│       │   │   │   └── repository/                # Repository pattern
│       │   │   │       ├── MedicineRepository.kt  # Interface repository
│       │   │   │       └── MedicineRepositoryImpl.kt # Implementation repository
│       │   │   │
│       │   │   ├── di/                            # Dependency Injection
│       │   │   │   └── AppModule.kt               # Hilt module cung cấp dependencies
│       │   │   │
│       │   │   ├── navigation/                    # Navigation logic
│       │   │   │   ├── AppNavigation.kt           # Cấu hình navigation graph
│       │   │   │   └── Screen.kt                  # Định nghĩa các màn hình (sealed class)
│       │   │   │
│       │   │   ├── ui/                             # UI layer
│       │   │   │   ├── components/                 # Reusable UI components
│       │   │   │   │   ├── ReminderCard.kt         # Card hiển thị thông tin nhắc nhở
│       │   │   │   │   ├── StatPieChart.kt         # Biểu đồ tròn thống kê
│       │   │   │   │   └── WeeklyTracker.kt       # Theo dõi tuần
│       │   │   │   │
│       │   │   │   ├── screens/                    # Các màn hình chính
│       │   │   │   │   ├── splash/                 # Màn hình splash
│       │   │   │   │   │   └── SplashScreen.kt     # UI màn hình splash
│       │   │   │   │   │
│       │   │   │   │   ├── onboarding/             # Onboarding flow
│       │   │   │   │   │   ├── GetStartedScreen.kt # Màn hình giới thiệu
│       │   │   │   │   │   ├── NicknameScreen.kt   # Nhập tên người dùng
│       │   │   │   │   │   ├── ReminderToneScreen.kt # Chọn âm thanh nhắc nhở
│       │   │   │   │   │   ├── NotificationPermissionScreen.kt # Yêu cầu quyền thông báo
│       │   │   │   │   │   ├── AllDoneScreen.kt    # Hoàn thành onboarding
│       │   │   │   │   │   └── OnboardingViewModel.kt # ViewModel quản lý onboarding
│       │   │   │   │   │
│       │   │   │   │   ├── home/                   # Màn hình chính
│       │   │   │   │   │   ├── HomeScreen.kt       # UI màn hình home
│       │   │   │   │   │   └── HomeViewModel.kt    # ViewModel xử lý logic
│       │   │   │   │   │
│       │   │   │   │   ├── add_med/                # Màn hình thêm/sửa thuốc
│       │   │   │   │   │   ├── AddMedScreen.kt    # UI màn hình thêm thuốc
│       │   │   │   │   │   └── AddMedViewModel.kt  # ViewModel xử lý logic
│       │   │   │   │   │
│       │   │   │   │   ├── statistics/             # Màn hình thống kê/tiến độ
│       │   │   │   │   │   ├── StatisticsScreen.kt # UI màn hình thống kê (Charts & List tabs)
│       │   │   │   │   │   └── StatisticsViewModel.kt # ViewModel xử lý logic
│       │   │   │   │   │
│       │   │   │   │   ├── history/                # Màn hình lịch sử (optional)
│       │   │   │   │   │   ├── HistoryScreen.kt   # UI màn hình lịch sử
│       │   │   │   │   │   └── HistoryViewModel.kt # ViewModel xử lý logic
│       │   │   │   │   │
│       │   │   │   │   └── settings/                # Màn hình cài đặt
│       │   │   │   │       └── SettingScreen.kt    # UI màn hình cài đặt
│       │   │   │   │
│       │   │   │   └── theme/                      # Theme configuration
│       │   │   │       ├── Color.kt                # Định nghĩa màu sắc
│       │   │   │       ├── Theme.kt                # Theme chính (Material 3)
│       │   │   │       └── Type.kt                 # Typography
│       │   │   │
│       │   │   ├── util/                           # Utilities và helpers
│       │   │   │   ├── AlarmScheduler.kt           # Lên lịch báo thức nhắc nhở
│       │   │   │   ├── Constants.kt                # Hằng số của ứng dụng
│       │   │   │   ├── NotificationHelper.kt        # Helper cho thông báo
│       │   │   │   └── PreferencesManager.kt        # Quản lý SharedPreferences/DataStore
│       │   │   │
│       │   │   └── workers/                        # Background workers
│       │   │       └── ReminderWorker.kt           # Worker xử lý nhắc nhở (WorkManager)
│       │   │
│       │   └── res/                                # Resources
│       │       ├── drawable/                       # Hình ảnh và drawables
│       │       │   ├── ic_launcher_background.xml
│       │       │   └── ic_launcher_foreground.xml
│       │       │
│       │       ├── mipmap-*/                       # App icons (các kích thước)
│       │       │   ├── mipmap-hdpi/
│       │       │   ├── mipmap-mdpi/
│       │       │   ├── mipmap-xhdpi/
│       │       │   ├── mipmap-xxhdpi/
│       │       │   ├── mipmap-xxxhdpi/
│       │       │   └── mipmap-anydpi/
│       │       │
│       │       ├── values/                          # Giá trị tài nguyên
│       │       │   ├── colors.xml                   # Màu sắc (XML)
│       │       │   ├── strings.xml                  # Chuỗi văn bản
│       │       │   └── themes.xml                   # Theme XML
│       │       │
│       │       └── xml/                             # XML configurations
│       │           ├── backup_rules.xml             # Quy tắc backup
│       │           └── data_extraction_rules.xml   # Quy tắc extract data
│       │
│       ├── androidTest/                             # Android instrumentation tests
│       │   └── java/nhom8/uth/pillreminderapp/
│       │       └── ExampleInstrumentedTest.kt       # Test mẫu
│       │
│       └── test/                                    # Unit tests
│           └── java/nhom8/uth/pillreminderapp/
│               └── ExampleUnitTest.kt                # Unit test mẫu
│
├── build.gradle.kts                                 # Root build configuration
├── settings.gradle.kts                              # Project settings
├── gradle.properties                                 # Gradle properties
├── gradlew                                           # Gradle wrapper (Unix)
├── gradlew.bat                                       # Gradle wrapper (Windows)
├── local.properties                                  # Local properties (SDK path)
│
├── gradle/
│   ├── libs.versions.toml                           # Version catalog (dependencies)
│   └── wrapper/                                      # Gradle wrapper files
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
│
├── README.md                                         # Tài liệu dự án
├── TODO.md                                           # Danh sách công việc
└── DEPENDENCY_INJECTION_EXPLANATION.md              # Giải thích về DI
```

---

## 🏗 Kiến trúc MVVM

### Model Layer
- **Entities**: `MedicineEntity`, `HistoryEntity` - Định nghĩa cấu trúc dữ liệu
- **Repository**: `MedicineRepository`, `MedicineRepositoryImpl` - Trung gian giữa ViewModel và Data Source
- **Database**: `AppDatabase` - Room database với các DAO

### View Layer
- **Onboarding Screens**: `SplashScreen`, `GetStartedScreen`, `NicknameScreen`, `ReminderToneScreen`, `NotificationPermissionScreen`, `AllDoneScreen` - Hướng dẫn người dùng lần đầu
- **Compose Screens**: `HomeScreen`, `AddMedScreen`, `StatisticsScreen`, `SettingScreen` - UI components
- **Compose Components**: `ReminderCard`, `StatPieChart`, `WeeklyTracker` - Reusable components

### ViewModel Layer
- **OnboardingViewModel**: Quản lý state và logic cho onboarding flow
- **HomeViewModel**: Quản lý logic cho màn hình chính (Overdue & Today's Schedule)
- **AddMedViewModel**: Xử lý logic thêm/sửa thuốc với form validation
- **StatisticsViewModel**: Xử lý logic tính toán và hiển thị thống kê (Charts & List views)

### Data Flow
1. **View** → **ViewModel**: User actions (click, input)
2. **ViewModel** → **Repository**: Request data operations
3. **Repository** → **Database/Worker**: Execute queries, schedule work
4. **Database/Flow** → **Repository**: Emit data changes
5. **Repository** → **ViewModel**: Expose StateFlow/Flow
6. **ViewModel** → **View**: Update UI state

---

## 🔧 Cài đặt và Chạy Project

### Yêu cầu hệ thống
- Android Studio Hedgehog | 2023.1.1 hoặc mới hơn
- JDK 11 hoặc cao hơn
- Android SDK 26 (Android 8.0) trở lên
- Gradle 8.x

### Các bước cài đặt

1. **Clone repository**
```bash
git clone <repository-url>
cd Pill-Reminder-App
```

2. **Mở project trong Android Studio**
   - File → Open → Chọn thư mục `Pill-Reminder-App`

3. **Sync Gradle**
   - Android Studio sẽ tự động sync, hoặc click "Sync Now"

4. **Chạy ứng dụng**
   - Kết nối thiết bị Android hoặc khởi động emulator
   - Click Run (▶️) hoặc nhấn `Shift + F10`

---

## 📱 Tính năng chính

### ✅ Đã hoàn thành
- [x] Thiết kế UI/UX trên Figma
- [x] Setup project và cấu hình dependencies
- [x] Cấu trúc project MVVM với Jetpack Compose
- [x] Tích hợp Room Database
- [x] Hilt Dependency Injection
- [x] Material 3 Theme

### 🚧 Đang phát triển / Cần hoàn thiện

#### Onboarding Flow
- [ ] Màn hình Splash với logo và loading
- [ ] Màn hình Get Started giới thiệu ứng dụng
- [ ] Màn hình nhập Nickname
- [ ] Màn hình chọn Reminder Tone
- [ ] Màn hình yêu cầu Notification Permission
- [ ] Màn hình All Done hoàn thành onboarding

#### Core Features
- [ ] Màn hình chính (HomeScreen) với Overdue và Today's Schedule
- [ ] Màn hình thêm/sửa thuốc (AddMedScreen) với form đầy đủ
- [ ] Màn hình thống kê (StatisticsScreen) với Charts và List views
- [ ] Màn hình cài đặt (SettingScreen)
- [ ] Navigation Compose với bottom navigation
- [ ] Logic lên lịch nhắc nhở với WorkManager
- [ ] ReminderCard component hiển thị thông tin thuốc

---

## 📚 Dependencies chính

```kotlin
// Compose
androidx.compose.ui
androidx.compose.material3
androidx.activity.compose

// Architecture
androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0
androidx.navigation:navigation-compose:2.7.7

// Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1
androidx.room:room-compiler:2.6.1 (KSP)

// Background Work
androidx.work:work-runtime-ktx:2.9.0

// Dependency Injection
com.google.dagger:hilt-android:2.51.1
com.google.dagger:hilt-compiler:2.51.1 (KSP)

// DataStore (cho preferences - optional)
androidx.datastore:datastore-preferences:1.0.0
```

## 🎨 Thiết kế UI

Ứng dụng sử dụng thiết kế Material 3 với:
- **Màu chủ đạo**: Light blue (#E3F2FD và các biến thể)
- **Typography**: Material 3 typography system
- **Components**: 
  - Bottom Navigation Bar với 3 tabs: Home, Progress, Setting
  - Reminder Cards với status indicators
  - Donut Chart cho thống kê
  - Form inputs với validation
- **Onboarding Flow**: 6 màn hình hướng dẫn người dùng mới



---

## 📄 License

Dự án này được phát triển cho mục đích học tập.

---

## 📝 Ghi chú

- Đảm bảo cấp quyền thông báo khi ứng dụng yêu cầu
- WorkManager sẽ tự động quản lý việc nhắc nhở ngay cả khi ứng dụng bị đóng
- Dữ liệu được lưu trữ cục bộ trên thiết bị bằng Room Database

---

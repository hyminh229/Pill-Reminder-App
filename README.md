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
│       │   │   │   ├── AlarmScheduler.kt           # Lên lịch báo thức nhắc nhở với WorkManager
│       │   │   │   ├── Constants.kt                # Hằng số của ứng dụng (notification, work tags, etc.)
│       │   │   │   ├── NotificationHelper.kt       # Helper tạo và hiển thị notifications
│       │   │   │   ├── SoundHelper.kt              # Helper quản lý danh sách và phát âm thanh thông báo
│       │   │   │   └── PreferencesManager.kt       # Quản lý SharedPreferences (settings, nickname, etc.)
│       │   │   │
│       │   │   ├── receivers/                      # BroadcastReceivers
│       │   │   │   └── ReminderActionReceiver.kt   # Xử lý action từ notification (Confirm, Skip, Remind Later)
│       │   │   │
│       │   │   └── workers/                        # Background workers
│       │   │       └── ReminderWorker.kt           # Worker xử lý nhắc nhở (WorkManager)
│       │   │
│       │   └── res/                                # Resources
│       │       ├── drawable/                       # Hình ảnh và drawables
│       │       │   ├── ic_launcher_background.xml
│       │       │   └── ic_launcher_foreground.xml
│       │       │   └── [nhiều icon drawables khác]
│       │       │
│       │       ├── mipmap-*/                       # App icons (các kích thước)
│       │       │   ├── mipmap-hdpi/
│       │       │   ├── mipmap-mdpi/
│       │       │   ├── mipmap-xhdpi/
│       │       │   ├── mipmap-xxhdpi/
│       │       │   ├── mipmap-xxxhdpi/
│       │       │   └── mipmap-anydpi/
│       │       │
│       │       ├── raw/                            # Âm thanh thông báo (13 files .wav)
│       │       │   ├── arabian_mystery_harp_notification.wav
│       │       │   ├── bell_notification.wav
│       │       │   ├── clear_announce_tones.wav
│       │       │   ├── correct_answer_reward.wav
│       │       │   ├── correct_answer_tone.wav
│       │       │   ├── doorbell_single_press.wav
│       │       │   ├── happy_bells_notification.wav
│       │       │   ├── positive_notification.wav
│       │       │   ├── sci_fi_click.wav
│       │       │   ├── software_interface_back.wav
│       │       │   ├── software_interface_remove.wav
│       │       │   ├── software_interface_start.wav
│       │       │   └── wrong_answer_fail_notification.wav
│       │       │
│       │       ├── values/                          # Giá trị tài nguyên
│       │       │   ├── colors.xml                   # Màu sắc (XML)
│       │       │   ├── strings.xml                  # Chuỗi văn bản
│       │       │   └── themes.xml                   # Theme XML
│       │       │
│       │       └── xml/                             # XML configurations
│       │           ├── backup_rules.xml             # Quy tắc backup
│       │           ├── data_extraction_rules.xml   # Quy tắc extract data
│       │           └── file_paths.xml               # FileProvider paths
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

### ✅ Đã hoàn thành (98%)

#### Architecture & Setup
- [x] Thiết kế UI/UX trên Figma
- [x] Setup project và cấu hình dependencies
- [x] Cấu trúc project MVVM với Jetpack Compose
- [x] Tích hợp Room Database (MedicineEntity, HistoryEntity)
- [x] Hilt Dependency Injection
- [x] Material 3 Theme
- [x] Navigation Compose với bottom navigation

#### Onboarding Flow (Hoàn chỉnh)
- [x] Màn hình Splash với logo và loading
- [x] Màn hình Get Started giới thiệu ứng dụng
- [x] Màn hình nhập Nickname
- [x] Màn hình chọn Reminder Tone
- [x] Màn hình yêu cầu Notification Permission
- [x] Màn hình All Done hoàn thành onboarding
- [x] OnboardingViewModel quản lý flow

#### Core Features (Hoàn chỉnh)
- [x] Màn hình chính (HomeScreen) với Overdue và Today's Schedule
- [x] Màn hình thêm/sửa thuốc (AddMedScreen) với form đầy đủ
- [x] Màn hình thống kê (StatisticsScreen) với Charts và List views
- [x] Màn hình cài đặt (SettingScreen) với Sound Picker
- [x] ReminderCard component hiển thị thông tin thuốc
- [x] StatPieChart component cho biểu đồ tròn

#### Notification & Reminder System (Hoàn chỉnh)
- [x] Logic lên lịch nhắc nhở với WorkManager (AlarmScheduler)
- [x] ReminderWorker xử lý thông báo nhắc nhở
- [x] NotificationHelper quản lý notification channel và hiển thị thông báo
- [x] Thông báo với 3 action buttons: Confirm, Skip, Remind Later
- [x] ReminderActionReceiver xử lý hành động từ thông báo
- [x] Hỗ trợ 13 âm thanh tùy chỉnh + âm thanh mặc định
- [x] Chức năng đổi âm thanh thông báo với preview
- [x] SoundHelper quản lý danh sách và phát âm thanh

#### Data Management (Hoàn chỉnh)
- [x] Room Database với 2 entities: MedicineEntity, HistoryEntity
- [x] MedicineRepository và MedicineRepositoryImpl
- [x] DAOs: MedicineDao, HistoryDao với các query phức tạp
- [x] PreferencesManager quản lý SharedPreferences
- [x] Type converters cho Date và List<String>

#### Permissions (Hoàn chỉnh)
- [x] Request POST_NOTIFICATIONS permission (Android 13+)
- [x] Request SCHEDULE_EXACT_ALARM permission (Android 12+)
- [x] Permission handling UI trong NotificationPermissionScreen

### 🚧 Đang phát triển / Chức năng phụ

#### Settings (Một số tính năng đang phát triển)
- [x] Reminder Sound - Đổi âm thanh thông báo ✅
- [ ] Reminder Mode - "As device settings" (hiển thị thông báo "đang phát triển")
- [ ] Remove ADS (hiển thị thông báo "đang phát triển")
- [ ] Light or Dark Theme (hiển thị thông báo "đang phát triển")

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

## 🔔 Hướng Dẫn Chi Tiết: Chức Năng Thông Báo và Đổi Âm Thanh Thông Báo

### Tổng Quan về Hệ Thống Thông Báo

Ứng dụng Pill Reminder App sử dụng hệ thống thông báo (notification) để nhắc nhở người dùng uống thuốc đúng giờ. Hệ thống này bao gồm các thành phần chính:

- **NotificationHelper** (`util/NotificationHelper.kt`): Quản lý việc tạo và hiển thị thông báo
- **SoundHelper** (`util/SoundHelper.kt`): Quản lý danh sách và phát âm thanh thông báo
- **ReminderWorker** (`workers/ReminderWorker.kt`): Worker xử lý lịch trình thông báo
- **PreferencesManager** (`util/PreferencesManager.kt`): Lưu trữ cài đặt âm thanh người dùng
- **AlarmScheduler** (`util/AlarmScheduler.kt`): Lên lịch nhắc nhở với WorkManager

---

### 🔔 Cơ Chế Hoạt Động của Thông Báo

#### 1. **Tạo Notification Channel**

Trên Android 8.0 (API 26) trở lên, ứng dụng phải tạo Notification Channel trước khi gửi thông báo. Channel được tạo tự động khi khởi động ứng dụng.

**File**: `util/NotificationHelper.kt`

- **Channel ID**: `"pill_reminder_channel"`
- **Channel Name**: `"Pill Reminders"`
- **Importance**: `IMPORTANCE_HIGH` (Mức độ ưu tiên cao)
- **Các tính năng**:
  - ✅ Bật rung (Vibration)
  - ✅ Bật đèn LED
  - ✅ Hiển thị badge
  - ⚠️ Tắt sound trong channel (âm thanh sẽ được phát trực tiếp)

**Lý do tắt sound trong channel**: Trên Android 8.0+, Notification Channel không hỗ trợ custom sound từ resource một cách linh hoạt. Do đó, ứng dụng sẽ phát âm thanh trực tiếp bằng `RingtoneManager` sau khi hiển thị thông báo.

#### 2. **Hiển Thị Thông Báo Nhắc Nhở**

Khi đến giờ nhắc nhở, `ReminderWorker` được kích hoạt bởi WorkManager và gọi `NotificationHelper.showReminderNotification()`.

**Thông tin hiển thị trên thông báo**:
- **Tiêu đề**: "Medication at {thời gian}" (ví dụ: "Medication at 08:00 AM")
- **Nội dung**: "{Tên thuốc} {Số lượng} {Đơn vị}" (ví dụ: "Paracetamol 2 pills")
- **Style**: BigTextStyle để hiển thị đầy đủ nội dung khi mở rộng

**Các nút hành động (Action Buttons)**:
1. **Confirm** (✓): Xác nhận đã uống thuốc → Đánh dấu status = "taken", xóa thông báo
2. **Skip** (✗): Bỏ qua lần uống thuốc này → Đánh dấu status = "skipped", xóa thông báo
3. **30 mins later** (⏰): Nhắc lại sau 30 phút → Lên lịch thông báo mới, xóa thông báo hiện tại

**Xử lý hành động**: Khi người dùng nhấn vào các nút, `ReminderActionReceiver` (BroadcastReceiver) sẽ xử lý và cập nhật database.

#### 3. **Phát Âm Thanh Thông Báo**

**Trên Android < 8.0**: Âm thanh được đặt trực tiếp trong `NotificationCompat.Builder`:
```kotlin
notificationBuilder.setSound(soundUri)
```

**Trên Android >= 8.0**: Âm thanh được phát trực tiếp bằng `RingtoneManager`:
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    playNotificationSound()
}
```

---

### 🔊 Hệ Thống Âm Thanh Thông Báo

#### 1. **Danh Sách Âm Thanh Có Sẵn**

Ứng dụng cung cấp **13 âm thanh tùy chỉnh** được lưu trong thư mục `res/raw/`:

1. **Default** - Âm thanh mặc định của hệ thống Android
2. **Arabian Mystery** - `arabian_mystery_harp_notification.wav`
3. **Bell** - `bell_notification.wav`
4. **Clear Announce** - `clear_announce_tones.wav`
5. **Correct Answer** - `correct_answer_reward.wav`
6. **Correct Tone** - `correct_answer_tone.wav`
7. **Doorbell** - `doorbell_single_press.wav`
8. **Happy Bells** - `happy_bells_notification.wav`
9. **Positive** - `positive_notification.wav`
10. **Sci-Fi Click** - `sci_fi_click.wav`
11. **Interface Back** - `software_interface_back.wav`
12. **Interface Remove** - `software_interface_remove.wav`
13. **Interface Start** - `software_interface_start.wav`
14. **Wrong Answer** - `wrong_answer_fail_notification.wav`

**File**: `util/SoundHelper.kt` - Function `getNotificationSounds()`

SoundHelper tự động quét các file `.wav` trong thư mục `res/raw/` và tạo danh sách với tên hiển thị thân thiện. Danh sách được sắp xếp theo tên, với "Default" luôn ở đầu.

#### 2. **Lưu Trữ Cài Đặt Âm Thanh**

Cài đặt âm thanh được lưu trong SharedPreferences với 3 giá trị:

**File**: `util/PreferencesManager.kt`

- `reminderTone`: Tên hiển thị của âm thanh (ví dụ: "Bell", "Default")
- `reminderToneUri`: URI của âm thanh dưới dạng String (để phát lại)
- `reminderToneRawName`: Tên file raw resource (để truy vấn nhanh)

**Lý do lưu cả 3 giá trị**:
- `reminderTone`: Hiển thị tên trong Settings
- `reminderToneUri`: Dùng để phát âm thanh
- `reminderToneRawName`: Để convert sang URI nếu cần

#### 3. **Cách Âm Thanh Được Phát**

**File**: `util/NotificationHelper.kt` - Function `playNotificationSound()`

```kotlin
private fun playNotificationSound() {
    try {
        val soundUri = getSoundUri()  // Lấy URI từ preferences
        val ringtone = RingtoneManager.getRingtone(context, soundUri)
        ringtone?.play()
    } catch (e: Exception) {
        android.util.Log.e("NotificationHelper", "Error playing sound", e)
    }
}
```

Function `getSoundUri()` sẽ:
1. Lấy URI từ `PreferencesManager.reminderToneUri`
2. Convert String thành URI bằng `SoundHelper.stringToUri()`
3. Nếu không có hoặc lỗi, sử dụng âm thanh mặc định của hệ thống

---

### 🎵 Hướng Dẫn Đổi Âm Thanh Thông Báo

#### **Bước 1: Truy Cập Màn Hình Settings**

1. Mở ứng dụng Pill Reminder App
2. Chuyển đến tab **Settings** (biểu tượng bánh răng ⚙️ ở thanh điều hướng dưới cùng)

#### **Bước 2: Mở Sound Picker**

1. Trong phần **"Reminder Settings"**, tìm mục **"Reminder Sound"**
2. Nhấn vào mục này để mở dialog chọn âm thanh

#### **Bước 3: Chọn Âm Thanh**

**File**: `ui/screens/settings/SettingScreen.kt` - Component `SoundPickerDialog`

1. Dialog sẽ hiển thị danh sách tất cả các âm thanh có sẵn (scrollable)
2. Mỗi âm thanh có:
   - **Tên hiển thị** (ví dụ: "Bell", "Arabian Mystery")
   - **Nút Preview** (biểu tượng play ▶️) để nghe thử
   - **Dấu tick** (✓) nếu là âm thanh đang được chọn

3. **Để nghe thử**: Nhấn vào nút **Play** (▶️) bên cạnh tên âm thanh
   - Âm thanh sẽ phát ngay lập tức
   - Có thể nghe thử nhiều âm thanh khác nhau
   - Âm thanh hiện tại sẽ tự động dừng khi chọn âm thanh khác

4. **Để chọn**: Nhấn vào tên âm thanh (hoặc vùng xung quanh tên)
   - Âm thanh được chọn sẽ có tick (✓) và màu đậm hơn
   - Dialog sẽ đóng lại

#### **Bước 4: Lưu Cài Đặt**

**File**: `ui/screens/settings/SettingViewModel.kt` - Function `updateReminderTone()`

Khi bạn chọn một âm thanh:

1. **Lưu vào PreferencesManager**:
   - Tên âm thanh → `reminderTone`
   - URI → `reminderToneUri`
   - Raw resource name → `reminderToneRawName`

2. **Cập nhật UI**: Tên âm thanh mới sẽ hiển thị trong Settings

3. **Cập nhật notification channel**: `NotificationHelper.updateNotificationChannel()` được gọi để đảm bảo thông báo sử dụng âm thanh mới

**Lưu ý quan trọng**: 
- Âm thanh mới sẽ được áp dụng ngay lập tức cho các thông báo tiếp theo
- Không cần khởi động lại ứng dụng
- Cài đặt được lưu vĩnh viễn, ngay cả khi đóng ứng dụng

---

### 🔄 Luồng Hoạt Động Chi Tiết

#### **Luồng Hiển Thị Thông Báo**

```
1. AlarmScheduler lên lịch reminder với WorkManager
   ↓
2. Đến giờ nhắc nhở → WorkManager kích hoạt ReminderWorker
   ↓
3. ReminderWorker gọi NotificationHelper.showReminderNotification()
   ↓
4. NotificationHelper:
   a. Cập nhật notification channel
   b. Lấy sound URI từ PreferencesManager
   c. Tạo notification với:
      - Tiêu đề: "Medication at {time}"
      - Nội dung: "{medicineName} {quantity} {unit}"
      - 3 action buttons: Confirm, Skip, 30 mins later
   d. Hiển thị notification
   e. Phát âm thanh (Android 8.0+)
   ↓
5. Người dùng thấy thông báo và nghe âm thanh
```

#### **Luồng Đổi Âm Thanh**

```
1. Người dùng mở Settings → "Reminder Sound"
   ↓
2. SettingScreen hiển thị SoundPickerDialog
   ↓
3. SoundHelper.getNotificationSounds() lấy danh sách từ res/raw/
   ↓
4. Dialog hiển thị danh sách với preview buttons
   ↓
5. Người dùng nghe thử (preview) và chọn âm thanh mới
   ↓
6. SettingViewModel.updateReminderTone() được gọi:
   a. Lưu cài đặt vào PreferencesManager
   b. Cập nhật notification channel
   c. Cập nhật UI (hiển thị tên âm thanh mới)
   ↓
7. Dialog đóng lại, cài đặt được lưu
```

#### **Luồng Xử Lý Action từ Thông Báo**

```
1. Người dùng nhấn action button (Confirm/Skip/Remind Later)
   ↓
2. BroadcastReceiver (ReminderActionReceiver) nhận Intent
   ↓
3. Xác định action:
   - ACTION_CONFIRM → markMedicineAsTaken()
   - ACTION_SKIP → markMedicineAsSkipped()
   - ACTION_REMIND_LATER → scheduleReminderIn30Minutes()
   ↓
4. Cập nhật database (HistoryEntity)
   ↓
5. Xóa thông báo hiện tại
```

---

### 📱 Xử Lý Khác Biệt Theo Phiên Bản Android

#### **Android < 8.0 (API < 26)**

- ✅ **Không cần Notification Channel**
- ✅ **Phát âm thanh**: Đặt trực tiếp trong NotificationCompat.Builder
  ```kotlin
  notificationBuilder.setSound(soundUri)
  ```

#### **Android >= 8.0 (API >= 26)**

- ⚠️ **Bắt buộc có Notification Channel**
- ⚠️ **Notification Channel không hỗ trợ custom sound linh hoạt** (chỉ hỗ trợ file trong MediaStore hoặc URI hệ thống)
- ✅ **Giải pháp**: 
  - Tắt sound trong channel: `setSound(null, null)`
  - Phát âm thanh trực tiếp bằng RingtoneManager sau khi hiển thị notification:
  ```kotlin
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      playNotificationSound()
  }
  ```

**Lý do kỹ thuật**: Notification Channel trên Android 8.0+ yêu cầu sound URI phải là URI từ MediaStore hoặc URI hệ thống. Nhưng âm thanh từ `res/raw/` có dạng `android.resource://...` không được hỗ trợ tốt trong channel. Do đó, ứng dụng sử dụng cách phát trực tiếp bằng RingtoneManager để đảm bảo tất cả các âm thanh đều hoạt động.

---

### 🎯 Tóm Tắt

#### **Chức Năng Thông Báo**
- ✅ Tạo notification channel tự động khi khởi động ứng dụng
- ✅ Hiển thị thông báo với tiêu đề và nội dung rõ ràng
- ✅ Cung cấp 3 nút hành động: Confirm, Skip, Remind Later (30 phút)
- ✅ Tự động phát âm thanh khi thông báo xuất hiện
- ✅ Hỗ trợ rung và đèn LED
- ✅ Hoạt động ngay cả khi ứng dụng bị đóng (WorkManager)

#### **Chức Năng Đổi Âm Thanh**
- ✅ 13 âm thanh tùy chỉnh + âm thanh mặc định của hệ thống
- ✅ Preview âm thanh trước khi chọn (nghe thử)
- ✅ Lưu cài đặt ngay lập tức vào SharedPreferences
- ✅ Áp dụng cho tất cả thông báo tiếp theo
- ✅ Hoạt động trên mọi phiên bản Android (8.0+)
- ✅ Giao diện thân thiện với dialog scrollable

#### **Cách Sử Dụng Đơn Giản**
1. Mở **Settings** → **Reminder Sound**
2. Nghe thử bằng nút **Preview** (▶️)
3. Chọn âm thanh yêu thích
4. Âm thanh sẽ được áp dụng ngay!

---

### 📝 Ghi Chú Quan Trọng

1. **Notification Channel không thể thay đổi sau khi tạo** (trên Android 8.0+):
   - Để thay đổi cài đặt, phải xóa và tạo lại channel
   - Function `updateNotificationChannel()` tự động xử lý việc này khi cần

2. **Âm thanh được lưu dưới dạng URI String**:
   - Dễ dàng serialize/deserialize
   - Hỗ trợ cả resource URI (`android.resource://...`) và system URI (`content://settings/system/...`)

3. **Notification ID phải là unique**:
   - Dựa trên `medicineId + reminderTime` → hash code
   - Đảm bảo không bị ghi đè khi có nhiều thông báo cùng lúc

4. **PendingIntent flags**:
   - `FLAG_UPDATE_CURRENT`: Cập nhật intent nếu đã tồn tại
   - `FLAG_IMMUTABLE`: Bắt buộc từ Android 12+ (API 31+)

5. **WorkManager Constraints**:
   - Không yêu cầu network
   - Chạy ngay cả khi battery low
   - Không yêu cầu charging
   - Đảm bảo thông báo luôn được gửi đúng giờ

---
# 📋 Danh sách Công việc - Pill Reminder App

## ✅ Đã hoàn thành
- [x] Thiết kế Figma (Hy, Thịnh)
- [x] Setup project và thêm Dependencies (Hy)
- [x] Database Layer (Entities, DAOs, AppDatabase)
- [x] Repository Layer (MedicineRepository, MedicineRepositoryImpl)
- [x] Dependency Injection với Hilt (AppModule, @HiltAndroidApp, @AndroidEntryPoint)
- [x] Navigation Setup (Screen.kt, AppNavigation.kt)
- [x] Onboarding Flow (6 màn hình + OnboardingViewModel)
- [x] HomeScreen (HomeViewModel, HomeScreen, ReminderCard)
- [x] AddMedScreen (AddMedViewModel, AddMedScreen - form đầy đủ)
- [x] Reminder System (AlarmScheduler, NotificationHelper, ReminderWorker với HiltWork)
- [x] StatisticsScreen (StatisticsViewModel, StatisticsScreen với Charts và List tabs, StatPieChart)
- [x] SettingScreen (SettingViewModel, SettingScreen với Sound picker, SoundHelper, thông báo "đang phát triển")
- [x] WeeklyTracker (Calendar view component hiển thị 7 ngày với taken/missed/skipped status)
- [x] SoundHelper (Lấy danh sách notification sounds từ hệ thống Android)
- [x] Constants.kt và PreferencesManager.kt
- [x] Permissions handling (POST_NOTIFICATIONS, SCHEDULE_EXACT_ALARM)

---

## 🔥 Ưu tiên cao - Cần làm ngay

### 1. Database Layer (Data Models)
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 2-3 giờ

- [x] **MedicineEntity.kt** - Định nghĩa entity cho thông tin thuốc
  - id, name, quantity, unit, intakeAdvice, startDate, endDate, reminderTimes (list), repeat, notes, isActive
- [x] **HistoryEntity.kt** - Định nghĩa entity cho lịch sử uống thuốc
  - id, medicineId, takenDate, takenTime, status (taken/missed/skipped)
- [x] **Converters.kt** - Type converters cho Room (Date, List<String>)
- [x] **MedicineDao.kt** - CRUD operations cho Medicine
  - getAllMedicines(), getActiveMedicines(), insert(), update(), delete()
  - getMedicineById(), getMedicinesByDate(), getOverdueMedicines()
- [x] **HistoryDao.kt** - CRUD operations cho History
  - insertHistory(), getHistoryByMedicineId(), getHistoryByDateRange()
  - getTodayHistory(), getStatisticsByMedicine(), getOverallStatistics()
- [x] **AppDatabase.kt** - Room database configuration
  - Database class với @Database annotation
  - Export schema = true
  - Version management (version 1)
  - Singleton pattern

### 2. Repository Layer
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 1-2 giờ

- [x] **MedicineRepository.kt** - Interface repository
  - Định nghĩa các method cần thiết cho Medicine và History
  - Medicine operations: CRUD, getByDate, getOverdue, activate/deactivate
  - History operations: CRUD, getByDateRange, getToday, statistics
  - Convenience methods: markAsTaken, markAsMissed, markAsSkipped
- [x] **MedicineRepositoryImpl.kt** - Implementation
  - Inject MedicineDao, HistoryDao với @Inject constructor
  - @Singleton annotation
  - Implement tất cả methods từ interface
  - Sử dụng Flow cho reactive data
  - Map raw statistics to domain models

### 3. Dependency Injection (Hilt)
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 1 giờ

- [x] **AppModule.kt** - Hilt module
  - @Module và @InstallIn(SingletonComponent::class)
  - @Provides AppDatabase (singleton)
  - @Provides MedicineDao
  - @Provides HistoryDao
  - @Binds MedicineRepository interface với MedicineRepositoryImpl
  - @Provides WorkManager (singleton)
  - @Provides PreferencesManager (singleton)
  - @ApplicationContext được inject tự động bởi Hilt

### 4. Navigation Setup
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 1 giờ

- [x] **Screen.kt** - Định nghĩa sealed class Screen
  - Onboarding: Splash, GetStarted, Nickname, ReminderTone, NotificationPermission, AllDone
  - Main: Home, AddMed, EditMed (với medicineId parameter), Statistics, Settings
  - NavigationRoutes helper object với constants và helper functions
- [x] **AppNavigation.kt** - Navigation graph
  - NavHost với NavController
  - Onboarding flow navigation (Splash → GetStarted → Nickname → ReminderTone → NotificationPermission → AllDone → Home)
  - Main app navigation (Home, AddMed, EditMed, Statistics, Settings)
  - Navigation callbacks cho các screens
  - Arguments handling cho EditMed screen

---

## 🟡 Ưu tiên trung bình - Core Features

### 5. Onboarding Flow
**Người nhận:** ✅ Hoàn thành (UI)  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 4-5 giờ

- [x] **SplashScreen.kt** - Màn hình splash
  - Logo với stopwatch và pills icon (emoji tạm thời)
  - Title "Pill Remider" và subtitle
  - Loading indicator (CircularProgressIndicator)
  - Auto navigate sau 2.5 giây với LaunchedEffect
  - Gradient background (MediumBlue → LightBlue)
- [x] **GetStartedScreen.kt** - Màn hình giới thiệu
  - Icon stopwatch với pills (emoji)
  - Title "Get medication reminders"
  - Subtitle "And much more..."
  - Button "Get Started" với DarkBlue color
- [x] **NicknameScreen.kt** - Nhập tên người dùng
  - Question "To start with, what should we call you?"
  - TextField với placeholder "Nickname"
  - Clear button (X icon) khi có text
  - Button "Next" (disabled khi empty)
  - Keyboard actions (Done)
- [x] **ReminderToneScreen.kt** - Chọn âm thanh nhắc nhở
  - Title "Pick your reminder tone"
  - Subtitle "For what matters most, choose a sound you won't ignore"
  - Sound selector với dropdown (mặc định "Meow meow")
  - Text "You can always change this later"
  - Button "Next"
- [x] **NotificationPermissionScreen.kt** - Yêu cầu quyền thông báo
  - Title "Never miss a dose!"
  - Subtitle "Let's make sure you get reminders exactly when you need them."
  - Button "Allow notifications"
  - Link "No thanks!"
- [x] **AllDoneScreen.kt** - Hoàn thành onboarding
  - Title "All done!"
  - Party popper emojis (🎉🎉🎉)
  - Button "Let's go"
- [x] **OnboardingViewModel.kt** - Quản lý state onboarding
  - Lưu nickname vào PreferencesManager
  - Lưu reminder tone preference
  - Check first launch để quyết định hiển thị onboarding
  - Hoàn thành onboarding và đánh dấu first launch = false

### 6. Màn hình chính (HomeScreen)
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 4-5 giờ

- [x] **HomeViewModel.kt**
  - State: list of medicines, overdue medicines, today's schedule
  - Functions: loadMedicines(), markAsTaken(), markAsSkipped(), deleteMedicine()
  - Sử dụng StateFlow/Flow từ Repository
  - MedicineReminder data class với ReminderStatus enum
  - Logic tính toán status dựa trên history và time
- [x] **HomeScreen.kt** - UI
  - Top app bar với logo pills và title "Pills Reminder"
  - Section "Overdue" với danh sách thuốc quá hạn
  - Section "Today's Schedule" với danh sách thuốc hôm nay
  - ReminderCard component cho mỗi thuốc (với status: Completed, Before Eating, Skipped)
  - Checkbox để mark as taken
  - FAB button (+) để thêm thuốc mới
  - Bottom navigation bar (Home, Progress, Setting)
  - Empty state khi chưa có thuốc
- [x] **ReminderCard.kt** - Component hiển thị thông tin thuốc
  - Medicine name, reminder time, status badge
  - Checkbox/checkmark với interaction
  - Arrow icon để navigate

### 7. Màn hình thêm/sửa thuốc (AddMedScreen)
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 5-6 giờ

- [x] **AddMedViewModel.kt**
  - State: form fields (name, dosage, quantity, unit, intakeAdvice, startDate, dueDate, reminderTimes, repeat)
  - Validation logic
  - Functions: saveMedicine(), updateMedicine(), deleteMedicine()
  - Handle intake advice (None, Before meal, With meal, After meal)
  - Handle unit selection (pills, ampoule, application, drop, gram, injection, miligram, teaspoon)
  - Load medicine cho edit mode
- [x] **AddMedScreen.kt** - UI
  - Back button và title "Medication details"
  - Pills name field với icon và edit button
  - Intake advice section:
    - Quantity selector (number picker)
    - Unit dropdown (pills, ampoule, etc.)
    - Intake advice dropdown (None, Before meal, With meal, After meal)
  - Date section:
    - Start date selector (Today dropdown)
    - Due date picker với calendar icon
  - Notification section:
    - Reminder time list với bell icon (10:00 AM)
    - Delete button cho mỗi reminder time
    - "Add reminder time" button với TimePicker
    - Repeat button với cycle icon
  - Done button
  - Error handling và validation messages
  - Material 3 DatePicker và TimePicker

### 8. Utility Classes - Nhắc nhở
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 3-4 giờ

- [x] **AlarmScheduler.kt**
  - Function scheduleReminder(medicine, times)
  - Function cancelReminder(medicineId)
  - Function updateReminder(medicine)
  - Sử dụng WorkManager để schedule
  - Parse time string và tính toán delay
  - @Singleton với @Inject constructor
- [x] **NotificationHelper.kt**
  - Function createNotificationChannel()
  - Function showNotification(title, message, medicineId)
  - Custom notification layout
  - @Singleton với @Inject constructor
- [x] **ReminderWorker.kt**
  - Extend CoroutineWorker với @HiltWorker
  - Logic hiển thị notification khi đến giờ
  - Sử dụng AssistedInject cho HiltWork
  - OneTimeWorkRequest với input data

### 9. Màn hình Progress/Statistics
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 4-5 giờ

- [x] **StatisticsViewModel.kt** ✅
  - State: statistics data, chart data, history list
  - Functions: loadStatistics(), loadHistory(), filterByDate()
  - Tính toán tỷ lệ taken/missed cho mỗi loại thuốc
  - Sử dụng Repository để lấy data
  - Expose StateFlow cho UI
  - ChartItem và HistoryItem data classes
  - TimeFilter enum (WEEK, MONTH, YEAR, ALL)
  - Color scheme cho chart segments
- [x] **StatisticsScreen.kt** ✅
  - Tab "Charts":
    - Title "Progress" với tabs "Charts" và "List"
    - Donut chart với các segment màu (teal, yellow, orange, pink, purple, blue)
    - Total value ở giữa chart
    - Data table với columns: Label, Value, %
    - Color-coded dots cho mỗi medicine
    - Empty state với message hướng dẫn
  - Tab "List":
    - Title "Progress" với tabs "Charts" và "List"
    - LazyColumn với history items
    - Mỗi item: Date (DD.MM.YYYY), Medicine Name, Dosage (icon + quantity), Time (HH:MM AM/PM)
    - Horizontal divider giữa các items
    - Empty state
  - Bottom navigation bar (Home, Progress active, Setting)
  - Tab selector với underline cho tab active

---

## 🟢 Ưu tiên thấp - Enhancements

### 10. UI Components
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 3-4 giờ

- [x] **ReminderCard.kt** - Component hiển thị thông tin thuốc ✅
  - Medicine name, reminder time
  - Status badge (Completed, Before Eating, Skipped, Missed, Pending)
  - Checkbox/checkmark với interaction
  - Arrow icon để navigate
  - Material 3 components
- [x] **StatPieChart.kt** - Biểu đồ thống kê ✅
  - Donut chart component với Canvas
  - Hiển thị segments với màu sắc
  - Total value ở giữa chart
  - ChartSegment data class
  - Responsive sizing
- [ ] **WeeklyTracker.kt** - Theo dõi tuần (Bỏ qua - không cần implement)
  - Calendar view hiển thị 7 ngày trong tuần
  - Mark days với taken/missed/skipped status
  - DayStatus data class và DayStatusType enum
  - Helper function createWeekDataFromHistory()
  - Legend hiển thị màu sắc cho từng status
  - Material 3 Card với rounded corners

### 11. Màn hình Settings
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 2-3 giờ

- [x] **SettingViewModel.kt** ✅
  - State: reminderTone, theme, reminderMode, availableSounds
  - Functions: loadSettings(), loadAvailableSounds(), updateReminderTone(), updateTheme()
  - Sử dụng SoundHelper để lấy danh sách sounds từ hệ thống
  - Lưu sound URI vào PreferencesManager
- [x] **SettingScreen.kt** ✅
  - Header với gear icon và "Setting" text
  - Section "Reminder Settings":
    - "Reminder Sound" option với dialog chọn sound từ hệ thống (hoạt động)
    - "Reminder Mode" với value "As device settings" (hiển thị "đang phát triển")
  - Section "General":
    - "Remove ADS" option (hiển thị "đang phát triển")
    - "Light or Dark Theme" với value "Light" (hiển thị "đang phát triển")
  - Bottom navigation bar (Home, Stats, Setting active)
  - SoundPickerDialog với danh sách notification sounds
  - Snackbar hiển thị thông báo "Tính năng này đang được phát triển" cho các chức năng chưa hoạt động
  - Sử dụng PreferencesManager để lưu settings
- [x] **SoundHelper.kt** ✅
  - Lấy danh sách notification sounds từ hệ thống bằng RingtoneManager
  - SoundItem data class (title, uri, isDefault)
  - Functions: getNotificationSounds(), getSoundTitle(), uriToString(), stringToUri()
  - @Singleton với @Inject constructor

### 12. Permissions Handling
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 1-2 giờ

- [x] Request POST_NOTIFICATIONS permission (Android 13+)
  - Đã implement trong NotificationPermissionScreen
  - Sử dụng Accompanist Permissions
- [x] Request SCHEDULE_EXACT_ALARM permission (Android 12+)
  - Đã khai báo trong AndroidManifest.xml
- [x] Permission handling UI
  - NotificationPermissionScreen với UI đẹp
  - Permission request trong AppNavigation
- [x] Graceful degradation khi không có quyền
  - Có option "No thanks!" để skip

### 13. Constants và Utils
**Người nhận:** ✅ Hoàn thành  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 1 giờ

- [x] **Constants.kt** ✅
  - Notification channel ID, name, description
  - WorkManager tag names và prefix
  - Database table names
  - Shared preferences keys
  - Medicine units list
  - Intake advice options
  - Repeat options
- [x] **PreferencesManager.kt** ✅
  - Quản lý SharedPreferences
  - First launch flag
  - User nickname
  - Reminder tone preference (tên)
  - Reminder tone URI (để sử dụng với RingtoneManager)
  - Theme preference
- [x] **SoundHelper.kt** ✅
  - Lấy danh sách notification sounds từ hệ thống Android
  - Chuyển đổi giữa URI và String để lưu vào Preferences
  - @Singleton với Hilt injection

---

## 🧪 Testing & Polish

### 13. Testing
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 4-5 giờ

- [ ] Unit tests cho ViewModels
- [ ] Unit tests cho Repository
- [ ] Unit tests cho DAOs
- [ ] Instrumented tests cho UI flows

### 14. Bug Fixes & Polish
**Người nhận:** Tất cả  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 3-4 giờ

- [ ] Fix lỗi runtime
- [ ] UI/UX improvements
- [ ] Performance optimization
- [ ] Code cleanup và refactoring
- [ ] Add comments và documentation

---

## 📝 Notes

### Công việc quan trọng cần hoàn thành trước:
1. ✅ **Database Layer** - Đã hoàn thành
2. ✅ **Repository & DI** - Đã hoàn thành
3. ✅ **Navigation** - Đã hoàn thành
4. ✅ **Onboarding Flow** - Đã hoàn thành
5. ✅ **HomeScreen** - Đã hoàn thành
6. ✅ **AddMedScreen** - Đã hoàn thành
7. ✅ **Reminder System** - Đã hoàn thành
8. ✅ **StatisticsScreen** - Đã hoàn thành
9. ✅ **SettingScreen** - Đã hoàn thành

### Công việc còn lại cần ưu tiên:
1. 🚧 **Các chức năng đang phát triển trong Settings:**
   - Reminder Mode (hiển thị thông báo "đang phát triển")
   - Remove ADS (hiển thị thông báo "đang phát triển")
   - Light or Dark Theme (hiển thị thông báo "đang phát triển")
2. 🧪 **Testing** - Unit tests và instrumented tests
3. 🧹 **Polish** - Bug fixes, performance optimization, code cleanup

### Tips:
- Làm theo thứ tự ưu tiên để tránh block nhau
- Commit code thường xuyên với message rõ ràng
- Test trên thiết bị thật, đặc biệt là notifications
- Chú ý Android version compatibility (minSdk 26)

---

---

## 📊 Tổng kết tiến độ

### ✅ Đã hoàn thành (98%):
- ✅ Architecture: MVVM, Hilt DI, Room Database
- ✅ Navigation: Navigation Compose với type-safe routes
- ✅ Onboarding: 6 màn hình onboarding hoàn chỉnh
- ✅ Home Screen: Hiển thị overdue, today's schedule, completed
- ✅ Add/Edit Medicine: Form đầy đủ với validation
- ✅ Reminder System: WorkManager + HiltWork integration
- ✅ Statistics Screen: ViewModel, UI với Charts và List tabs, StatPieChart
- ✅ Settings Screen: ViewModel, UI với Reminder Settings và General sections, Sound picker dialog
- ✅ Sound Helper: Lấy danh sách notification sounds từ hệ thống Android
- ⏭️ WeeklyTracker: Bỏ qua (không cần implement)
- ✅ Permissions: POST_NOTIFICATIONS, SCHEDULE_EXACT_ALARM
- ✅ Utilities: Constants, PreferencesManager, SoundHelper

### 🚧 Đang phát triển (1%):
- 🚧 Các chức năng trong Settings: Reminder Mode, Remove ADS, Light or Dark Theme (hiển thị thông báo "đang phát triển")

### 📝 Cần làm (1%):
- 📝 Testing: Unit tests, instrumented tests
- 📝 Polish: Bug fixes, performance, documentation

---

**Cập nhật lần cuối:** Tháng 12, 2024

### 📈 Tiến độ mới nhất:
- ✅ **SettingScreen hoàn thành** - Đã implement đầy đủ ViewModel, UI với Reminder Settings và General sections
- ✅ **SoundHelper hoàn thành** - Lấy danh sách notification sounds từ hệ thống Android, Sound picker dialog
- ✅ **Sound Picker Dialog** - Dialog chọn sound với danh sách scrollable, highlight sound đã chọn
- ⏭️ **WeeklyTracker** - Bỏ qua (không cần implement)
- ✅ **Thông báo "đang phát triển"** - Snackbar hiển thị cho các chức năng chưa hoạt động (Reminder Mode, Remove ADS, Light or Dark Theme)
- 🎯 **Tiến độ tổng thể: 98%** - Chỉ còn Testing và Polish, một số chức năng trong Settings đang phát triển

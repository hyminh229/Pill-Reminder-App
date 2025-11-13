# 📋 Danh sách Công việc - Pill Reminder App

## ✅ Đã hoàn thành
- [x] Thiết kế Figma (Hy, Thịnh)
- [x] Setup project và thêm Dependencies (Hy)

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
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 4-5 giờ

- [ ] **HomeViewModel.kt**
  - State: list of medicines, overdue medicines, today's schedule
  - Functions: loadMedicines(), markAsTaken(), markAsSkipped(), deleteMedicine()
  - Sử dụng StateFlow/Flow từ Repository
- [ ] **HomeScreen.kt** - UI
  - Top app bar với logo pills và title "Pills Reminder"
  - Section "Overdue" với danh sách thuốc quá hạn
  - Section "Today's Schedule" với danh sách thuốc hôm nay
  - ReminderCard component cho mỗi thuốc (với status: Completed, Before Eating, Skipped)
  - Checkbox để mark as taken
  - FAB button (+) để thêm thuốc mới
  - Bottom navigation bar (Home, Progress, Setting)
  - Empty state khi chưa có thuốc

### 7. Màn hình thêm/sửa thuốc (AddMedScreen)
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 5-6 giờ

- [ ] **AddMedViewModel.kt**
  - State: form fields (name, dosage, quantity, unit, intakeAdvice, startDate, dueDate, reminderTimes, repeat)
  - Validation logic
  - Functions: saveMedicine(), updateMedicine(), deleteMedicine()
  - Handle intake advice (None, Before meal, With meal, After meal)
  - Handle unit selection (pills, ampoule, application, drop, gram, injection, miligram, teaspoon)
- [ ] **AddMedScreen.kt** - UI
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
    - "Add reminder time" button
    - Repeat button với cycle icon
  - Done button
  - Error handling và validation messages

### 8. Utility Classes - Nhắc nhở
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 3-4 giờ

- [ ] **AlarmScheduler.kt**
  - Function scheduleReminder(medicine, times)
  - Function cancelReminder(medicineId)
  - Function updateReminder(medicine)
  - Sử dụng WorkManager để schedule
- [ ] **NotificationHelper.kt**
  - Function createNotificationChannel()
  - Function showNotification(title, message, medicineId)
  - Custom notification layout nếu cần
- [ ] **ReminderWorker.kt**
  - Extend CoroutineWorker
  - Logic hiển thị notification khi đến giờ
  - Mark missed nếu quá giờ
  - OneTimeWorkRequest hoặc PeriodicWorkRequest

### 9. Màn hình Progress/Statistics
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 4-5 giờ

- [ ] **StatisticsViewModel.kt**
  - State: statistics data, chart data, history list
  - Functions: loadStatistics(), loadHistory(), filterByDate()
  - Tính toán tỷ lệ taken/missed cho mỗi loại thuốc
- [ ] **StatisticsScreen.kt** - UI với 2 tabs
  - Tab "Charts":
    - Title "Progress Charts" và "List" tab
    - Donut chart với các segment màu (teal, yellow, orange, pink, purple, blue)
    - Total value ở giữa chart
    - Data list với columns: Label, Value, %
    - Color-coded dots cho mỗi medicine
  - Tab "List":
    - Title "Progress" và "Charts" tab, "List" tab active
  - LazyColumn với history items
    - Mỗi item: Date (DD.MM.YYYY), pill icon + count, meal icon, time (HH:MM AM/PM)
    - Horizontal divider giữa các items
  - Bottom navigation bar (Home, Progress active, Setting)

---

## 🟢 Ưu tiên thấp - Enhancements

### 10. UI Components
**Người nhận:** ___  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 3-4 giờ

- [ ] **ReminderCard.kt** - Component hiển thị thông tin thuốc
  - Medicine name, dosage
  - Next reminder time
  - Status badge
  - Action buttons (taken, skip, edit, delete)
- [ ] **StatPieChart.kt** - Biểu đồ thống kê
  - Sử dụng thư viện chart (nếu cần)
  - Hiển thị tỷ lệ taken/missed
- [ ] **WeeklyTracker.kt** - Theo dõi tuần
  - Calendar view
  - Mark days với taken/missed status

### 11. Màn hình Settings
**Người nhận:** ___  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 2-3 giờ

- [ ] **SettingsScreen.kt**
  - Header với tabs: "History" (inactive), "Setting" (active với gear icon)
  - Section "Reminder Settings":
    - "Reminder Sound" option (navigate to sound picker)
    - "Reminder Mode" với value "As device settings"
  - Section "General":
    - "Remove ADS" option
    - "Light or Dark Theme" với value "Light"
  - Bottom navigation bar (Home, Stats, Setting active)

### 12. Permissions Handling
**Người nhận:** ___  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 1-2 giờ

- [ ] Request POST_NOTIFICATIONS permission (Android 13+)
- [ ] Request SCHEDULE_EXACT_ALARM permission (Android 12+)
- [ ] Permission handling UI
- [ ] Graceful degradation khi không có quyền

### 13. Constants và Utils
**Người nhận:** ___  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 1 giờ

- [ ] **Constants.kt**
  - Notification channel ID
  - WorkManager tag names
  - Database table names
  - Shared preferences keys

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
1. **Database Layer** - Cần làm đầu tiên, là nền tảng cho tất cả features
2. **Repository & DI** - Cần làm ngay sau database
3. **Navigation** - Cần để có thể điều hướng giữa các màn hình
4. **Onboarding Flow** - Cần để hướng dẫn người dùng lần đầu sử dụng
5. **HomeScreen** - Màn hình chính, cần hoàn thiện sớm
6. **AddMedScreen** - Tính năng core, cần để thêm thuốc
7. **Reminder System** - Core feature, cần để app hoạt động đúng mục đích

### Tips:
- Làm theo thứ tự ưu tiên để tránh block nhau
- Commit code thường xuyên với message rõ ràng
- Test trên thiết bị thật, đặc biệt là notifications
- Chú ý Android version compatibility (minSdk 26)

---

**Cập nhật lần cuối:** 2024


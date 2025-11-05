# 📋 Danh sách Công việc - Pill Reminder App

## ✅ Đã hoàn thành
- [x] Thiết kế Figma (Hy, Thịnh)
- [x] Setup project và thêm Dependencies (Hy)

---

## 🔥 Ưu tiên cao - Cần làm ngay

### 1. Database Layer (Data Models)
**Người nhận:** ___  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 2-3 giờ

- [ ] **MedicineEntity.kt** - Định nghĩa entity cho thông tin thuốc
  - id, name, dosage, frequency, times (list), startDate, endDate, notes, isActive
- [ ] **HistoryEntity.kt** - Định nghĩa entity cho lịch sử uống thuốc
  - id, medicineId, takenDate, takenTime, status (taken/missed/skipped)
- [ ] **MedicineDao.kt** - CRUD operations cho Medicine
  - getAllMedicines(), getActiveMedicines(), insert(), update(), delete()
  - getMedicineById(), getMedicinesByDate()
- [ ] **HistoryDao.kt** - CRUD operations cho History
  - insertHistory(), getHistoryByMedicineId(), getHistoryByDateRange()
  - getTodayHistory(), getStatistics()
- [ ] **AppDatabase.kt** - Room database configuration
  - Database class với @Database annotation
  - Export schema = true
  - Version management

### 2. Repository Layer
**Người nhận:** ___  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 1-2 giờ

- [ ] **MedicineRepository.kt** - Interface repository
  - Định nghĩa các method cần thiết
- [ ] **MedicineRepositoryImpl.kt** - Implementation
  - Inject MedicineDao, HistoryDao
  - Implement các method từ interface
  - Sử dụng Flow cho reactive data

### 3. Dependency Injection (Hilt)
**Người nhận:** ___  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 1 giờ

- [ ] **AppModule.kt** - Hilt module
  - @Provides AppDatabase
  - @Provides MedicineDao
  - @Provides HistoryDao
  - @Provides MedicineRepository
  - @Provides WorkManager
  - @Provides Context

### 4. Navigation Setup
**Người nhận:** ___  
**Ưu tiên:** 🔴 Cao  
**Thời gian ước tính:** 1 giờ

- [ ] **Screen.kt** - Định nghĩa sealed class Screen
  - Home, AddMed, EditMed, History, Settings
- [ ] **AppNavigation.kt** - Navigation graph
  - NavHost với NavController
  - Routes giữa các màn hình
  - Bottom navigation bar (nếu cần)

---

## 🟡 Ưu tiên trung bình - Core Features

### 5. Màn hình chính (HomeScreen)
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 4-5 giờ

- [ ] **HomeViewModel.kt**
  - State: list of medicines, today's schedule
  - Functions: loadMedicines(), markAsTaken(), deleteMedicine()
  - Sử dụng StateFlow/Flow từ Repository
- [ ] **HomeScreen.kt** - UI
  - Top app bar với title
  - FAB button để thêm thuốc mới
  - Danh sách thuốc hôm nay (LazyColumn)
  - ReminderCard component cho mỗi thuốc
  - Empty state khi chưa có thuốc
  - Pull to refresh

### 6. Màn hình thêm/sửa thuốc (AddMedScreen)
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 5-6 giờ

- [ ] **AddMedViewModel.kt**
  - State: form fields (name, dosage, frequency, times, dates, notes)
  - Validation logic
  - Functions: saveMedicine(), updateMedicine(), deleteMedicine()
- [ ] **AddMedScreen.kt** - UI
  - Form fields với TextField
  - Time picker cho các lần uống
  - Date picker cho start/end date
  - Frequency selector (daily, weekly, custom)
  - Save/Cancel buttons
  - Error handling và validation messages

### 7. Utility Classes - Nhắc nhở
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

### 8. Màn hình lịch sử (HistoryScreen)
**Người nhận:** ___  
**Ưu tiên:** 🟡 Trung bình  
**Thời gian ước tính:** 3-4 giờ

- [ ] **HistoryViewModel.kt**
  - State: history list, filter options
  - Functions: loadHistory(), filterByDate(), filterByMedicine()
- [ ] **HistoryScreen.kt** - UI
  - Filter options (today, week, month, all)
  - LazyColumn với history items
  - Status indicators (taken/missed)
  - Statistics summary

---

## 🟢 Ưu tiên thấp - Enhancements

### 9. UI Components
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

### 10. Màn hình Settings
**Người nhận:** ___  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 2-3 giờ

- [ ] **SettingsScreen.kt**
  - Notification settings
  - Theme preferences (light/dark)
  - Language settings (nếu có)
  - About/Help section
  - Clear data option

### 11. Permissions Handling
**Người nhận:** ___  
**Ưu tiên:** 🟢 Thấp  
**Thời gian ước tính:** 1-2 giờ

- [ ] Request POST_NOTIFICATIONS permission (Android 13+)
- [ ] Request SCHEDULE_EXACT_ALARM permission (Android 12+)
- [ ] Permission handling UI
- [ ] Graceful degradation khi không có quyền

### 12. Constants và Utils
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
4. **HomeScreen** - Màn hình chính, cần hoàn thiện sớm
5. **AddMedScreen** - Tính năng core, cần để thêm thuốc
6. **Reminder System** - Core feature, cần để app hoạt động đúng mục đích

### Tips:
- Làm theo thứ tự ưu tiên để tránh block nhau
- Commit code thường xuyên với message rõ ràng
- Test trên thiết bị thật, đặc biệt là notifications
- Chú ý Android version compatibility (minSdk 26)

---

**Cập nhật lần cuối:** 2024


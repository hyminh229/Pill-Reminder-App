# 📚 Dependency Injection (DI) - Giải thích chi tiết

## 🎯 Dependency Injection là gì?

**Dependency Injection (DI)** là một design pattern cho phép một object nhận các dependencies (phụ thuộc) từ bên ngoài thay vì tự tạo ra chúng.

### Khái niệm đơn giản:
Thay vì một class tự tạo các object mà nó cần, chúng ta sẽ "tiêm" (inject) các object đó vào từ bên ngoài.

---

## ❌ Vấn đề khi KHÔNG dùng DI (Cách làm cũ)

### Ví dụ không có DI:

```kotlin
// ❌ Cách làm CŨ - Tự tạo dependencies
class HomeViewModel {
    // ViewModel tự tạo Repository
    private val repository = MedicineRepositoryImpl(
        // Repository tự tạo DAO
        MedicineDao(
            // DAO tự tạo Database...
            AppDatabase.getInstance(context)
        )
    )
    
    fun loadMedicines() {
        repository.getAllMedicines()
    }
}
```

**Vấn đề:**
1. ❌ **Tight Coupling** - Các class phụ thuộc chặt chẽ vào nhau
2. ❌ **Khó test** - Không thể thay thế bằng mock objects
3. ❌ **Khó bảo trì** - Muốn thay đổi một class phải sửa nhiều nơi
4. ❌ **Tạo object nhiều lần** - Không tái sử dụng được

---

## ✅ Giải pháp với Dependency Injection

### Ví dụ với DI:

```kotlin
// ✅ Cách làm MỚI - Inject dependencies
class HomeViewModel(
    private val repository: MedicineRepository  // Inject từ bên ngoài
) : ViewModel() {
    
    fun loadMedicines() {
        repository.getAllMedicines()
    }
}
```

**Lợi ích:**
1. ✅ **Loose Coupling** - Các class độc lập với nhau
2. ✅ **Dễ test** - Có thể inject mock objects
3. ✅ **Dễ bảo trì** - Thay đổi một class không ảnh hưởng class khác
4. ✅ **Tái sử dụng** - Chia sẻ cùng một instance

---

## 🔧 Hilt - Dependency Injection Framework cho Android

**Hilt** là framework DI của Google, được xây dựng trên **Dagger** nhưng đơn giản hơn và tích hợp tốt với Android.

### Cách Hilt hoạt động:

#### 1. **Application Class** - Entry point

```kotlin
@HiltAndroidApp  // 👈 Annotation này bật Hilt
class PillReminderApplication : Application()
```

#### 2. **Module** - Nơi cung cấp dependencies

```kotlin
@Module
@InstallIn(SingletonComponent::class)  // 👈 Scope: Toàn bộ app
object AppModule {
    
    // Cung cấp Database
    @Provides
    @Singleton  // 👈 Chỉ tạo 1 instance duy nhất
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pill_reminder_database"
        ).build()
    }
    
    // Cung cấp MedicineDao
    @Provides
    fun provideMedicineDao(database: AppDatabase): MedicineDao {
        return database.medicineDao()  // 👈 Hilt tự động inject database ở trên
    }
    
    // Cung cấp HistoryDao
    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }
    
    // Cung cấp Repository
    @Provides
    fun provideMedicineRepository(
        medicineDao: MedicineDao,      // 👈 Hilt tự động inject
        historyDao: HistoryDao         // 👈 Hilt tự động inject
    ): MedicineRepository {
        return MedicineRepositoryImpl(medicineDao, historyDao)
    }
    
    // Cung cấp WorkManager
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
```

#### 3. **Sử dụng trong ViewModel**

```kotlin
@HiltViewModel  // 👈 Annotation này bật Hilt cho ViewModel
class HomeViewModel @Inject constructor(
    private val repository: MedicineRepository  // 👈 Hilt tự động inject
) : ViewModel() {
    
    val medicines = repository.getAllMedicines()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
```

#### 4. **Sử dụng trong Activity/Fragment**

```kotlin
@AndroidEntryPoint  // 👈 Annotation này bật Hilt cho Activity
class MainActivity : ComponentActivity() {
    
    // Có thể inject trực tiếp (nếu cần)
    // @Inject lateinit var someDependency: SomeClass
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
    }
}
```

---

## 📊 So sánh: Có DI vs Không có DI

### Scenario: HomeViewModel cần MedicineRepository

#### ❌ Không có DI:
```kotlin
class HomeViewModel : ViewModel() {
    // Phải tự tạo
    private val context = Application().applicationContext
    private val database = AppDatabase.getInstance(context)
    private val dao = database.medicineDao()
    private val repository = MedicineRepositoryImpl(dao)
    
    // Vấn đề: Khó test, khó thay đổi
}
```

#### ✅ Có DI (Hilt):
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MedicineRepository  // Hilt tự động inject
) : ViewModel() {
    // Sạch sẽ, dễ test, dễ bảo trì
}
```

---

## 🎯 Các Annotation quan trọng trong Hilt

| Annotation | Mục đích | Ví dụ |
|------------|----------|-------|
| `@HiltAndroidApp` | Bật Hilt trong Application class | `@HiltAndroidApp class App : Application()` |
| `@Module` | Đánh dấu class là Module cung cấp dependencies | `@Module object AppModule` |
| `@Provides` | Method cung cấp một dependency | `@Provides fun provideDao() = ...` |
| `@Inject` | Yêu cầu Hilt inject dependency | `@Inject constructor(repo: Repository)` |
| `@HiltViewModel` | Đánh dấu ViewModel sử dụng Hilt | `@HiltViewModel class MyViewModel` |
| `@AndroidEntryPoint` | Đánh dấu Activity/Fragment sử dụng Hilt | `@AndroidEntryPoint class MainActivity` |
| `@Singleton` | Tạo một instance duy nhất cho toàn app | `@Singleton @Provides fun provideDB()` |
| `@ApplicationContext` | Inject Application Context | `@Provides fun provide(@ApplicationContext ctx: Context)` |

---

## 🔄 Flow hoạt động của Hilt

```
1. App khởi động
   ↓
2. Hilt scan @HiltAndroidApp
   ↓
3. Hilt đọc @Module để biết cách tạo dependencies
   ↓
4. Khi ViewModel được tạo với @HiltViewModel
   ↓
5. Hilt xem constructor cần gì (MedicineRepository)
   ↓
6. Hilt tìm trong @Module xem ai provide MedicineRepository
   ↓
7. Thấy cần MedicineDao và HistoryDao
   ↓
8. Hilt tìm cách tạo MedicineDao và HistoryDao
   ↓
9. Thấy cần AppDatabase
   ↓
10. Hilt tạo AppDatabase → MedicineDao → HistoryDao → Repository
   ↓
11. Inject Repository vào ViewModel
   ↓
12. ViewModel sử dụng Repository
```

---

## 💡 Ví dụ thực tế trong Pill Reminder App

### Cấu trúc Dependency Tree:

```
HomeViewModel
    └── needs MedicineRepository
            └── needs MedicineDao
            └── needs HistoryDao
                    └── both need AppDatabase
                            └── needs Context
```

### Code thực tế:

```kotlin
// 1. Module cung cấp dependencies
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pill_reminder_db"
        ).build()
    }
    
    @Provides
    fun provideMedicineDao(db: AppDatabase) = db.medicineDao()
    
    @Provides
    fun provideHistoryDao(db: AppDatabase) = db.historyDao()
    
    @Provides
    fun provideRepository(
        medicineDao: MedicineDao,
        historyDao: HistoryDao
    ): MedicineRepository = MedicineRepositoryImpl(medicineDao, historyDao)
}

// 2. ViewModel sử dụng
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MedicineRepository  // Hilt tự động inject
) : ViewModel() {
    // Sử dụng repository...
}

// 3. Activity setup
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Activity có thể inject ViewModelFactory tự động
}
```

---

## 🧪 Lợi ích khi Test

### Test với Mock:

```kotlin
// Test không cần database thật
class HomeViewModelTest {
    @Test
    fun `test load medicines`() {
        // Tạo mock repository
        val mockRepository = mockk<MedicineRepository>()
        every { mockRepository.getAllMedicines() } returns flowOf(emptyList())
        
        // Inject mock vào ViewModel
        val viewModel = HomeViewModel(mockRepository)
        
        // Test logic...
    }
}
```

---

## ✅ Tóm tắt

1. **Dependency Injection** = Cung cấp dependencies từ bên ngoài thay vì tự tạo
2. **Hilt** = Framework DI của Google, đơn giản và mạnh mẽ
3. **Lợi ích**: Code sạch, dễ test, dễ bảo trì, tái sử dụng
4. **Cách dùng**: 
   - `@HiltAndroidApp` trong Application
   - `@Module` với `@Provides` để cung cấp dependencies
   - `@HiltViewModel` và `@Inject` trong ViewModel
   - `@AndroidEntryPoint` trong Activity/Fragment

---

## 📚 Tài liệu tham khảo

- [Hilt Documentation](https://developer.android.com/training/dependency-injection/hilt-android)
- [Dependency Injection Guide](https://developer.android.com/training/dependency-injection)

---

**Lưu ý**: Trong dự án Pill Reminder App, bạn đã có `@HiltAndroidApp` trong `PillReminderApplication`, bây giờ cần hoàn thiện `AppModule.kt` để cung cấp các dependencies cần thiết!


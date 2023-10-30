package app.kotleni.cats

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Timer::class],
    version = 3,
//    autoMigrations = [
//        AutoMigration (from = 2, to = 3)
//    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao
}
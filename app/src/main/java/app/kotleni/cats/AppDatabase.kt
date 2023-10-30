package app.kotleni.cats

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Timer::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao
}
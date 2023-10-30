package app.kotleni.cats

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimerDao {
    @Query("SELECT * FROM timer")
    suspend fun getAll(): List<Timer>

    @Insert
    suspend fun insertAll(vararg timers: Timer)

    @Delete
    suspend fun delete(timer: Timer)

    @Update
    suspend fun update(timer: Timer)
}
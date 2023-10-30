package app.kotleni.cats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timer(
    @PrimaryKey val id: Long,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("iconId") var iconId: Int,
    @ColumnInfo("totalWorkTime") var totalWorkTime: Long,
    @ColumnInfo("totalBreakTime") var totalBreakTime: Long,
    @ColumnInfo("shortBreakTime") var shortBreakTime: Int,
    @ColumnInfo("longBreakTime") var longBreakTime: Int,
    @ColumnInfo("workTime") var workTime: Int,
)
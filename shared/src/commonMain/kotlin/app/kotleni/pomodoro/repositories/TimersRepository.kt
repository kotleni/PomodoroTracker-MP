package app.kotleni.pomodoro.repositories

import app.kotleni.pomodoro.Database
import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.Timer
import org.koin.core.component.KoinComponent

interface TimersRepository {
    suspend fun fetchTimers(): List<Timer>
    suspend fun addTimer(timer: Timer)
    suspend fun updateTimer(timer: Timer)
    suspend fun removeTimer(timer: Timer)
}

class TimersRepositoryImpl(databaseDriverFactory: DatabaseDriverFactory) : KoinComponent, TimersRepository {
    private val database = Database(databaseDriverFactory.createDriver())
    private val dbQuery = database.timerQueries

    override suspend fun fetchTimers(): List<Timer> {
        return dbQuery.selectAllTimers().executeAsList()
    }

    override suspend fun addTimer(timer: Timer) {
        dbQuery.insertTimer(
            name = timer.name, iconId = timer.iconId,
            totalWorkTime = timer.totalWorkTime,
            totalBreakTime = timer.totalBreakTime,
            shortBreakTime = timer.shortBreakTime,
            longBreakTime = timer.longBreakTime,
            workTime = timer.workTime
        )
    }

    override suspend fun updateTimer(timer: Timer) {
        dbQuery.updateTimer(
            name = timer.name, iconId = timer.iconId,
            totalWorkTime = timer.totalWorkTime,
            totalBreakTime = timer.totalBreakTime,
            shortBreakTime = timer.shortBreakTime,
            longBreakTime = timer.longBreakTime,
            workTime = timer.workTime,
            id = timer.id
        )
    }

    override suspend fun removeTimer(timer: Timer) {
        dbQuery.deleteTimer(timer.id)
    }
}
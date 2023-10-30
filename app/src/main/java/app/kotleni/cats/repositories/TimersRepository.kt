package app.kotleni.cats.repositories

import app.kotleni.cats.AppDatabase
import app.kotleni.cats.Timer
import javax.inject.Inject

class TimersRepository @Inject constructor(db: AppDatabase) {
    private val dao = db.timerDao()

    suspend fun fetchTimers(): List<Timer> {
        return dao.getAll()
    }

    suspend fun addTimer(timer: Timer) {
        dao.insertAll(timer)
    }

    suspend fun removeTimer(timer: Timer) {
        dao.delete(timer)
    }

    suspend fun update(timer: Timer) {
        dao.update(timer)
    }
}
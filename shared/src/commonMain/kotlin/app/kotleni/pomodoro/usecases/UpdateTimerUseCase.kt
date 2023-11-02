package app.kotleni.pomodoro.usecases

import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.repositories.TimersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateTimerUseCase(
    private val timersRepository: TimersRepository
) {
    suspend operator fun invoke(updatedTimer: Timer) {
        withContext(Dispatchers.IO) {
            timersRepository.updateTimer(updatedTimer)
        }
    }
}
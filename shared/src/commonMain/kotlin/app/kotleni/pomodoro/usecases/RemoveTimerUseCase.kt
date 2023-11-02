package app.kotleni.pomodoro.usecases

import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.repositories.TimersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoveTimerUseCase(
    private val timersRepository: TimersRepository
) {
    suspend operator fun invoke(timer: Timer, finishCallback: () -> Unit) {
        withContext(Dispatchers.IO) {
            timersRepository.removeTimer(timer)
        }
        finishCallback()
    }
}
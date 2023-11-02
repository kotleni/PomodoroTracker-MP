package app.kotleni.pomodoro.usecases

import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.repositories.TimersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadTimerByIdUseCase(
    private val timersRepository: TimersRepository
) {
    suspend operator fun invoke(timerId: Long, finishCallback: (Timer) -> Unit) {
        val timer = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers().find { it.id == timerId }
        } ?: return
        finishCallback(timer)
    }
}
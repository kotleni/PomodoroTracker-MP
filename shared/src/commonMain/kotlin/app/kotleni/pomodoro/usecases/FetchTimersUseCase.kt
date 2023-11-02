package app.kotleni.pomodoro.usecases

import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.repositories.TimersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchTimersUseCase(
    private val timersRepository: TimersRepository
) {
    suspend operator fun invoke(finishCallback: (timers: List<Timer>) -> Unit) {
        val timers = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers()
        }
        finishCallback(timers)
    }
}
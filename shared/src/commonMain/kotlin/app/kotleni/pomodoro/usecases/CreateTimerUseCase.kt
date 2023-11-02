package app.kotleni.pomodoro.usecases

import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.repositories.TimersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CreateTimerUseCase(
    private val timersRepository: TimersRepository
) {
    suspend operator fun invoke(name: String, iconId: Int, workTime: Int, sbrakeTime: Int, lbrakeTime: Int, finishCallback: () -> Unit) {
        // Debug purpose, if started with prefix 'debug: ' use seconds instead of minutes
        val isDebugSecs = name.startsWith("debug: ")

        withContext(Dispatchers.IO) {
            val timer = Timer(
                name = name.removePrefix("debug: "),
                iconId = iconId.toLong(),
                totalWorkTime = if(isDebugSecs) Random.nextLong(0, 9999) else 0,
                totalBreakTime = if(isDebugSecs) Random.nextLong(0, 999) else 0,
                workTime = (if(isDebugSecs) workTime else workTime * 60).toLong(), // in minutes
                shortBreakTime = (if(isDebugSecs) sbrakeTime else sbrakeTime * 60).toLong(), // in minutes
                longBreakTime = (if(isDebugSecs) lbrakeTime else lbrakeTime * 60).toLong(), // in minutes
                id = 0 // automatic
            )
            timersRepository.addTimer(timer)
        }
        finishCallback()
    }
}
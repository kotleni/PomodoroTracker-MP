package app.kotleni.pomodoro.ui.main

import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.TimerService
import app.kotleni.pomodoro.TimerServiceFactory
import app.kotleni.pomodoro.repositories.TimersRepository
import app.kotleni.pomodoro.repositories.TimersRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class MainViewModel : KoinComponent {
    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val databaseDriverFactory: DatabaseDriverFactory by inject()
    private val timersRepository: TimersRepository = TimersRepositoryImpl(databaseDriverFactory)

    private val _timers: MutableStateFlow<List<Timer>> = MutableStateFlow(listOf())
    val timers: StateFlow<List<Timer>> = _timers

    private val _activeTimer: MutableStateFlow<Timer?> = MutableStateFlow(null)
    val activeTimer: StateFlow<Timer?> = _activeTimer

    private val timerServiceFactory: TimerServiceFactory by inject()
    private var service: TimerService? = null

    fun loadTimers() = viewModelScope.launch {
        _timers.value = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers()
        }
    }

    fun bindToService() {
        service = timerServiceFactory.createTimerService()
        loadActiveTimer()
    }

    fun loadActiveTimer() {
        _activeTimer.value = service?.getTimer()
    }

    fun createTimer(name: String, iconId: Int, workTime: Int, sbrakeTime: Int, lbrakeTime: Int) = viewModelScope.launch {
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
        loadTimers()
    }

    fun removeTimer(timer: Timer) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            timersRepository.removeTimer(timer)
        }
        loadTimers()
    }
}
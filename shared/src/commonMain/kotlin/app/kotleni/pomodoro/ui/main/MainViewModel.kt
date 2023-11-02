package app.kotleni.pomodoro.ui.main

import TimerServiceImpl
import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.TimerService
import app.kotleni.pomodoro.repositories.TimersRepository
import app.kotleni.pomodoro.repositories.TimersRepositoryImpl
import app.kotleni.pomodoro.usecases.CreateTimerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class MainUIState(
    val timers: List<Timer> = listOf(),
    val activeTimer: Timer? = null
)

class MainViewModel : KoinComponent {
    private val createTimerUseCase: CreateTimerUseCase by inject()

    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val databaseDriverFactory: DatabaseDriverFactory by inject()
    private val timersRepository: TimersRepository = TimersRepositoryImpl(databaseDriverFactory)

    private val _uiState: MutableStateFlow<MainUIState> = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState

    private var service: TimerService = TimerServiceImpl()

    fun loadTimers() = viewModelScope.launch {
        _uiState.update {
            it.copy(timers = withContext(Dispatchers.IO) {
                timersRepository.fetchTimers()
            })
        }
    }

    fun bindToService() {
        loadActiveTimer()
    }

    fun loadActiveTimer() {
        _uiState.update {
            it.copy(activeTimer = service.getTimer())
        }
    }

    fun createTimer(name: String, iconId: Int, workTime: Int, sbrakeTime: Int, lbrakeTime: Int) = viewModelScope.launch {
        createTimerUseCase(
            name,
            iconId,
            workTime,
            sbrakeTime,
            lbrakeTime,
            ::loadTimers
        )
    }

    fun removeTimer(timer: Timer) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            timersRepository.removeTimer(timer)
        }
        loadTimers()
    }
}
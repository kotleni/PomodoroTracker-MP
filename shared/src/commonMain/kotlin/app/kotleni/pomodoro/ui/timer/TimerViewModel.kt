package app.kotleni.pomodoro.ui.timer

import TimerServiceImpl
import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.TimerListener
import app.kotleni.pomodoro.TimerService
import app.kotleni.pomodoro.TimerStage
import app.kotleni.pomodoro.TimerState
import app.kotleni.pomodoro.repositories.TimersRepository
import app.kotleni.pomodoro.repositories.TimersRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class TimerUIState(
    val timer: Timer? = null,
    val timerStage: TimerStage = TimerStage.WORK,
    val timerState: TimerState = TimerState.STOPPED,
    val currentSeconds: Int = 0
)

class TimerViewModel : KoinComponent, TimerListener {
    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val databaseDriverFactory: DatabaseDriverFactory by inject()
    private val timersRepository: TimersRepository = TimersRepositoryImpl(databaseDriverFactory)

    private var service: TimerService = TimerServiceImpl()

    private val _uiState: MutableStateFlow<TimerUIState> = MutableStateFlow(TimerUIState())
    val uiState: StateFlow<TimerUIState> = _uiState

    override fun onTimeUpdated(timer: Timer, secs: Int) {
        _uiState.update {
            it.copy(currentSeconds = secs)
        }
    }

    override fun onStateUpdated(timer: Timer, state: TimerState) {
        _uiState.update {
            it.copy(timerState = state)
        }

        when(state) {
            TimerState.STOPPED -> {
                val timerStage = uiState.value.timerStage
                val currentStartTime = (if(timerStage == TimerStage.WORK) timer.workTime else timer.shortBreakTime)
                adjustTimerStats(timerStage, currentStartTime.toInt())

                nextTimerStage()
            }
            TimerState.PAUSED -> {}
            TimerState.STARTED -> {}
        }
    }

    override fun onStageUpdated(timer: Timer, stage: TimerStage) {
        _uiState.update {
            it.copy(timerStage = stage)
        }
    }

    fun resetServiceIsNotStarted() {
        if(uiState.value.timerState != TimerState.STARTED) {
            // Remove listener first, because events not needed
            service.setListener(null)

            service.stop()
            service.setTimer(null)
        }
    }

    private fun bindToService() {
        service.setListener(this)
        service.setTimer(uiState.value.timer)
    }

    fun loadTimer(timerId: Long) = viewModelScope.launch {
        val timer = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers().find { it.id == timerId }
        }

        _uiState.update {
            it.copy(timer = timer)
        }
        bindToService()
    }

    fun nextTimerStage() {
        val timerStage = uiState.value.timerStage
        service.setStage(if(timerStage == TimerStage.WORK) TimerStage.BREAK else TimerStage.WORK)
    }

    private fun adjustTimerStats(stage: TimerStage, seconds: Int) = viewModelScope.launch {
        val currentTimer = uiState.value.timer
        if (currentTimer != null) {
            val updatedTimer = when (stage) {
                TimerStage.WORK -> currentTimer.copy(totalWorkTime = currentTimer.totalWorkTime + seconds)
                TimerStage.BREAK -> currentTimer.copy(totalBreakTime = currentTimer.totalBreakTime + seconds)
            }

            _uiState.update {
                it.copy(timer = updatedTimer)
            }

            withContext(Dispatchers.IO) {
                timersRepository.updateTimer(updatedTimer)
            }
        }
    }

    fun start() = service.start()
    fun resume() = service.resume()
    fun pause() = service.pause()
}
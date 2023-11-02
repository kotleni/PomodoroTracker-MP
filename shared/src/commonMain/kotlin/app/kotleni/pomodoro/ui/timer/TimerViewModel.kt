package app.kotleni.pomodoro.ui.timer

import TimerServiceImpl
import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.TimerListener
import app.kotleni.pomodoro.TimerService
import app.kotleni.pomodoro.TimerStage
import app.kotleni.pomodoro.TimerState
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
import app.kotleni.pomodoro.DatabaseDriverFactory

class TimerViewModel : KoinComponent, TimerListener {
    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val databaseDriverFactory: DatabaseDriverFactory by inject()
    private val timersRepository: TimersRepository = TimersRepositoryImpl(databaseDriverFactory)

    //private val timerServiceFactory: TimerServiceFactory by inject()
    private var service: TimerService = TimerServiceImpl()

    private val _timer: MutableStateFlow<Timer?> = MutableStateFlow(null)
    val timer: StateFlow<Timer?> = _timer

    private val _currentSeconds: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentSeconds: StateFlow<Int> = _currentSeconds

    private val _timerStage: MutableStateFlow<TimerStage> = MutableStateFlow(TimerStage.WORK)
    val timerStage: StateFlow<TimerStage> = _timerStage

    private val _timerState: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState

//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
//            Log.d("TimerViewModel", "onServiceConnected: ${className.className}")
//            val binder = binder as TimerServiceImpl.TimerBinder
//            service = binder.getService()
//
//            service?.setListener(this@TimerViewModel)
//            _timer.value?.let { service?.setTimer(it) }
//        }
//
//        override fun onServiceDisconnected(className: ComponentName) {
//            Log.d("TimerViewModel", "onServiceDisconnected: ${className.className}")
//            service = null
//        }
//    }

    override fun onTimeUpdated(timer: Timer, secs: Int) {
        _currentSeconds.value = secs
    }

    override fun onStateUpdated(timer: Timer, state: TimerState) {
        //Log.d("TimerViewModel", "onStateUpdated: ${state.name}")
        _timerState.value = state

        when(state) {
            TimerState.STOPPED -> {
                val currentStartTime = (if(timerStage.value == TimerStage.WORK) timer.workTime else timer.shortBreakTime) ?: 0
                adjustTimerStats(timerStage.value, currentStartTime.toInt())

                nextTimerStage()
            }
            TimerState.PAUSED -> {}
            TimerState.STARTED -> {}
        }
    }

    override fun onStageUpdated(timer: Timer, stage: TimerStage) {
        //Log.d("TimerViewModel", "onStageUpdated: ${stage.name}")
        _timerStage.value = stage
    }

    fun resetServiceIsNotStarted() {
        if(timerState.value != TimerState.STARTED) {
            // Remove listener first, because events not needed
            service?.setListener(null)

            service?.stop()
            service?.setTimer(null)
            //service = null
        }
    }

    fun bindToService() {
        //service = timerServiceFactory.createTimerService()
        service?.setListener(this)
        _timer.value?.let { service?.setTimer(it) }
    }

    fun loadTimer(timerId: Long) = viewModelScope.launch {
        val timer = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers().find { it.id == timerId }
        }

        _timer.value = timer
        bindToService()
    }

    fun nextTimerStage() {
        service?.setStage(if(timerStage.value == TimerStage.WORK) TimerStage.BREAK else TimerStage.WORK)
    }

    private fun adjustTimerStats(stage: TimerStage, seconds: Int) = viewModelScope.launch {
        val currentTimer = timer.value
        if (currentTimer != null) {
            val updatedTimer = when (stage) {
                TimerStage.WORK -> currentTimer.copy(totalWorkTime = currentTimer.totalWorkTime + seconds)
                TimerStage.BREAK -> currentTimer.copy(totalBreakTime = currentTimer.totalBreakTime + seconds)
            }

            _timer.value = updatedTimer

            withContext(Dispatchers.IO) {
                timersRepository.updateTimer(updatedTimer)
            }
        }
    }

    fun start() {
        service?.start()
    }

    fun resume() {
        service?.resume()
    }

    fun pause() {
        service?.pause()
    }
}
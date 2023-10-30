package app.kotleni.cats.ui.timer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kotleni.cats.Timer
import app.kotleni.cats.TimerListener
import app.kotleni.cats.TimerService
import app.kotleni.cats.TimerState
import app.kotleni.cats.repositories.TimersRepository
import app.kotleni.cats.services.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val timersRepository: TimersRepository,
    val notificationService: NotificationService
): ViewModel(), TimerListener {
    private var service: TimerService? = null

    private val _timer: MutableStateFlow<Timer?> = MutableStateFlow(null)
    val timer: StateFlow<Timer?> = _timer

    private val _currentSeconds: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentSeconds: StateFlow<Int> = _currentSeconds

    private val _timerStage: MutableStateFlow<TimerStage> = MutableStateFlow(TimerStage.WORK)
    val timerStage: StateFlow<TimerStage> = _timerStage

    private val _timerState: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            Log.d("TimerViewModel", "onServiceConnected: ${className.className}")
            val binder = binder as TimerService.TimerBinder
            service = binder.getService()

            service?.setListener(this@TimerViewModel)
            _timer.value?.let { service?.setTimer(it) }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Log.d("TimerViewModel", "onServiceDisconnected: ${className.className}")
            service = null
        }
    }

    override fun onTimeUpdated(timer: Timer, secs: Int) {
        // Log.d("TimerViewModel", "onTimeUpdated: $secs")
        _currentSeconds.value = secs
    }

    override fun onStateUpdated(timer: Timer, state: TimerState) {
        Log.d("TimerViewModel", "onStateUpdated: ${state.name}")
        _timerState.value = state

        when(state) {
            TimerState.STOPPED -> {
                val currentStartTime = (if(timerStage.value == TimerStage.WORK) timer.workTime else timer.shortBreakTime) ?: 0
                adjustTimerStats(timerStage.value, currentStartTime)

                nextTimerStage()
            }
            TimerState.PAUSED -> {}
            TimerState.STARTED -> {}
        }
    }

    override fun onStageUpdated(timer: Timer, stage: TimerStage) {
        Log.d("TimerViewModel", "onStageUpdated: ${stage.name}")
        _timerStage.value = stage
    }

    fun resetServiceIsNotStarted() {
        if(timerState.value != TimerState.STARTED) {
            service?.stop()
            service?.setTimer(null)
            service?.setListener(null)
            service = null
        }
    }

    fun bindToService() {
        Intent(context, TimerService::class.java).also { intent ->
            context.startService(intent)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
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

    fun adjustTimerStats(stage: TimerStage, seconds: Int) = viewModelScope.launch {
        if(timer.value == null) return@launch

        if(stage == TimerStage.WORK)
            timer.value!!.totalWorkTime += seconds
        else if(stage == TimerStage.BREAK)
            timer.value!!.totalBreakTime += seconds
        Log.d("dsa", timer.value.toString())

        withContext(Dispatchers.IO) {
            timersRepository.update(timer.value!!)
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
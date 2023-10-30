package app.kotleni.cats.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kotleni.cats.Timer
import app.kotleni.cats.TimerService
import app.kotleni.cats.repositories.TimersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val timersRepository: TimersRepository
) : ViewModel() {
    private var service: TimerService? = null

    private val _timers: MutableStateFlow<List<Timer>> = MutableStateFlow(listOf())
    val timers: StateFlow<List<Timer>> = _timers

    private val _activeTimer: MutableStateFlow<Timer?> = MutableStateFlow(null)
    val activeTimer: StateFlow<Timer?> = _activeTimer

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            Log.d("MainViewModel", "onServiceConnected: ${className.className}")
            val binder = binder as TimerService.TimerBinder
            service = binder.getService()

            loadActiveTimer()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Log.d("MainViewModel", "onServiceDisconnected: ${className.className}")
            service = null
        }
    }

    fun bindToService() {
        Intent(context, TimerService::class.java).also { intent ->
            //context.startService(intent)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun loadActiveTimer() {
        _activeTimer.value = service?.getTimer()
    }

    fun loadTimers() = viewModelScope.launch {
        _timers.value = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers()
        }
    }

    fun createTimer(name: String, iconId: Int, workTime: Int, sbrakeTime: Int, lbrakeTime: Int) = viewModelScope.launch {
        val isDebugSecs = name.startsWith("debug: ")

        withContext(Dispatchers.IO) {
            val timer = Timer(
                //id = timers.value.size.toLong(),
                name = name.removePrefix("debug: "),
                iconId = iconId,
                totalWorkTime = if(isDebugSecs) Random.nextLong(0, 9999) else 0,
                totalBreakTime = if(isDebugSecs) Random.nextLong(0, 999) else 0,
                workTime = if(isDebugSecs) workTime else workTime * 60, // in minutes
                shortBreakTime = if(isDebugSecs) sbrakeTime else sbrakeTime * 60, // in minutes
                longBreakTime = if(isDebugSecs) lbrakeTime else lbrakeTime * 60 // in minutes
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
package app.kotleni.cats.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kotleni.cats.Timer
import app.kotleni.cats.repositories.TimersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    val timersRepository: TimersRepository
) : ViewModel() {
    private val _timers: MutableStateFlow<List<Timer>> = MutableStateFlow(listOf())
    val timers: StateFlow<List<Timer>> = _timers

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
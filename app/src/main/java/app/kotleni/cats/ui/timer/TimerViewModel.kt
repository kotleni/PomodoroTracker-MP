package app.kotleni.cats.ui.timer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kotleni.cats.Timer
import app.kotleni.cats.repositories.TimersRepository
import app.kotleni.cats.services.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    val timersRepository: TimersRepository,
    val notificationService: NotificationService
): ViewModel() {
    private val _timer: MutableStateFlow<Timer?> = MutableStateFlow(null)
    val timer: StateFlow<Timer?> = _timer

    fun loadTimer(timerId: Long) = viewModelScope.launch {
        val timer = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers().find { it.id == timerId }
        }

        _timer.value = timer
    }

    fun showAlarmNotification(title: String, description: String) {
        notificationService.showAlarmNotification(title, description)
    }

    fun showPermanentNotification(title: String, description: String) {
        notificationService.showPermanentNotification(title, description)
    }

    fun closePermanentNotification() {
        notificationService.closePermanentNotification()
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
}
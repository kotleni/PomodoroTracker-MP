package app.kotleni.pomodoro

import kotlinx.coroutines.CoroutineScope

expect open class ViewModel() {
    val viewModelScope: CoroutineScope
}
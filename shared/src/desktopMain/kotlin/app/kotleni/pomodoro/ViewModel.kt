package app.kotleni.pomodoro

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual open class ViewModel {
    actual val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
}
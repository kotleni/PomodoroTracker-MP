package app.kotleni.pomodoro

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual open class ViewModel : ViewModel() {
    actual val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
}
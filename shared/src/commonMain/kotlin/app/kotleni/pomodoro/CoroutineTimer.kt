package app.kotleni.pomodoro

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/***
 * Multiplatform implementation of interval timer
 */
class CoroutineTimer {
    private val coroutineContext = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    private var callback: (() -> Unit)? = null
    private var startDelay: Long = 0
    private var period: Long = 0

    /***
     * Prepare timer to scheduled work
     * @param delay - Start delay in ms
     * @param period - Callback invoke period in ms
     * @param callback - Callback-listener for timer tick
     */
    fun scheduled(delay: Long, period: Long, callback: () -> Unit) {
        this.startDelay = delay
        this.period = period
        this.callback = callback
    }

    /***
     * Run timer
     */
    fun run() {
        job = coroutineContext.launch {
            delay(startDelay)

            while(isActive) {
                callback?.invoke()
                delay(period)
            }
        }
    }

    /***
     * Stop timer
     */
    fun stop() {
        job?.cancel()
    }
}
package app.kotleni.cats

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import app.kotleni.cats.helpers.NotificationsHelper
import app.kotleni.cats.ui.timer.TimerStage
import java.util.Timer as JavaTimer
import java.util.TimerTask
import kotlin.concurrent.schedule


enum class TimerState {
    STOPPED, PAUSED, STARTED
}

interface TimerListener {
    fun onTimeUpdated(timer: Timer, secs: Int)
    fun onStateUpdated(timer: Timer, state: TimerState)
    fun onStageUpdated(timer: Timer, stage: TimerStage)
}

interface TimerService {
    fun stop()

    fun start()

    fun pause()

    fun resume()

    fun setTimer(timer: Timer?)

    fun setStage(stage: TimerStage)

    fun setListener(listener: TimerListener?)

    fun getTimer(): Timer?
}

class TimerServiceImpl : Service(), TimerService {
    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerServiceImpl
    }

    private val jtimer = JavaTimer()
    private var timerTask: TimerTask? = null
    private var binder: Binder? = null
    private var listener: TimerListener? = null

    private var notificationsHelper: NotificationsHelper? = null

    private var state = TimerState.STOPPED
    private var stage = TimerStage.WORK
    private var currentSeconds = 0
    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()

        notificationsHelper = NotificationsHelper(applicationContext)

        timerTask = jtimer.schedule(0, 1_000) { onTimerTick() }
        timerTask?.run()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        binder = TimerBinder()
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask?.cancel()
    }

    private fun onTimerTick() {
        if(state == TimerState.STARTED && currentSeconds > 0) {
            currentSeconds -= 1

            val timeName = if(stage == TimerStage.WORK) "Work time" else "Break time"
            notificationsHelper?.showPermanentNotification(timeName, "Time left ${currentSeconds.toTimeString()}")

            notifyTimeChanged()

            if(currentSeconds == 0) {
                stop()

                notificationsHelper?.closePermanentNotification()
                notificationsHelper?.showAlarmNotification("$timeName is ended", "Press here to open app")
            }
        }
    }

    private fun notifyStateChanged() {
        timer?.let {
            listener?.onStateUpdated(it, state)
        }
    }

    private fun notifyStageChanged() {
        timer?.let {
            listener?.onStageUpdated(it, stage)
        }
    }

    private fun notifyTimeChanged() {
        timer?.let {
            listener?.onTimeUpdated(it, currentSeconds)
        }
    }

    private fun resetTime() {
        val currentStartTime = (if(stage == TimerStage.WORK) timer?.workTime else timer?.shortBreakTime) ?: 0
        currentSeconds = currentStartTime
        notifyTimeChanged()
    }

    override fun stop() {
        state = TimerState.STOPPED
        notifyStateChanged()

        notificationsHelper?.closePermanentNotification()
    }

    override fun start() {
        resetTime()
        state = TimerState.STARTED
        notifyStateChanged()
    }

    override fun pause() {
        state = TimerState.PAUSED
        notifyStateChanged()

        notificationsHelper?.closePermanentNotification()
    }

    override fun resume() {
        state = TimerState.STARTED
        notifyStateChanged()
    }

    override fun setTimer(timer: Timer?) {
        this.timer = timer

        if(timer != null) {
            // Notify all updated data
            notifyStageChanged()
            notifyStateChanged()
            resetTime()
        }
    }

    override fun setStage(stage: TimerStage) {
        if(state != TimerState.STOPPED)
            stop()

        this.stage = stage
        resetTime()
        notifyStageChanged()
    }

    override fun setListener(listener: TimerListener?) {
        this.listener = listener
    }

    override fun getTimer(): Timer? {
        return timer
    }
}
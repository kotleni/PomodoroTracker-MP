package app.kotleni.cats

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import app.kotleni.cats.services.NotificationService
import app.kotleni.cats.ui.timer.TimerStage
import java.io.FileDescriptor
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

class TimerService : Service() {
    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    private val jtimer = JavaTimer()
    private var timerTask: TimerTask? = null
    private var binder: Binder? = null
    private var listener: TimerListener? = null

    private var notificationService: NotificationService? = null

    private var state = TimerState.STOPPED
    private var stage = TimerStage.WORK
    private var currentSeconds = 0
    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()

        notificationService = NotificationService(applicationContext)

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
            notificationService?.showPermanentNotification(timeName, "Time left ${currentSeconds.toTimeString()}")

            notifyTimeChanged()

            if(currentSeconds == 0) {
                stop()

                notificationService?.closePermanentNotification()
                notificationService?.showAlarmNotification("$timeName is ended", "Press here to open app")
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

    fun stop() {
        state = TimerState.STOPPED
        notifyStateChanged()

        notificationService?.closePermanentNotification()
    }

    fun start() {
        resetTime()
        state = TimerState.STARTED
        notifyStateChanged()
    }

    fun pause() {
        state = TimerState.PAUSED
        notifyStateChanged()

        notificationService?.closePermanentNotification()
    }

    fun resume() {
        state = TimerState.STARTED
        notifyStateChanged()
    }

    fun setTimer(timer: Timer) {
        this.timer = timer

        // Notify all updated data
        notifyStageChanged()
        notifyStateChanged()
        resetTime()
    }

    fun setStage(stage: TimerStage) {
        if(state != TimerState.STOPPED)
            stop()

        this.stage = stage
        resetTime()
        notifyStageChanged()
    }

    fun setListener(listener: TimerListener?) {
        this.listener = listener
    }
}
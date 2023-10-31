package app.kotleni.pomodoro

import java.util.TimerTask
import kotlin.concurrent.schedule
import java.util.Timer as JavaTimer

actual class TimerServiceImpl : TimerService {
    private val jtimer = JavaTimer()
    private var timerTask: TimerTask? = null
    private var listener: TimerListener? = null

    private var state = TimerState.STOPPED
    private var stage = TimerStage.WORK
    private var currentSeconds = 0
    private var timer: Timer? = null

    init {
        timerTask = jtimer.schedule(0, 1_000) { onTimerTick() }
        timerTask?.run()
    }

    private fun onTimerTick() {
        if(state == TimerState.STARTED && currentSeconds > 0) {
            currentSeconds -= 1

            val timeName = if(stage == TimerStage.WORK) "Work time" else "Break time"
            //notificationsHelper?.showPermanentNotification(timeName, "Time left ${currentSeconds.toTimeString()}")

            notifyTimeChanged()

            if(currentSeconds == 0) {
                stop()

               // notificationsHelper?.closePermanentNotification()
               // notificationsHelper?.showAlarmNotification("$timeName is ended", "Press here to open app")
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
        currentSeconds = currentStartTime.toInt()
        notifyTimeChanged()
    }

    override fun stop() {
        state = TimerState.STOPPED
        notifyStateChanged()

        // notificationsHelper?.closePermanentNotification()
    }

    override fun start() {
        resetTime()
        state = TimerState.STARTED
        notifyStateChanged()
    }

    override fun pause() {
        state = TimerState.PAUSED
        notifyStateChanged()

        // notificationsHelper?.closePermanentNotification()
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
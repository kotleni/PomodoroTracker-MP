package app.kotleni.pomodoro

interface TimerListener {
    fun onTimeUpdated(timer: Timer, secs: Int)
    fun onStateUpdated(timer: Timer, state: TimerState)
    fun onStageUpdated(timer: Timer, stage: TimerStage)
}
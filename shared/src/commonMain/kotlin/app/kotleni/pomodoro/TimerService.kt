package app.kotleni.pomodoro

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

// expect class TimerServiceImpl : TimerService
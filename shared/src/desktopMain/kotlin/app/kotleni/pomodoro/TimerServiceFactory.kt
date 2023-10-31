package app.kotleni.pomodoro

actual class TimerServiceFactory {
    actual fun createTimerService(): TimerService {
        return TimerServiceImpl()
    }
}
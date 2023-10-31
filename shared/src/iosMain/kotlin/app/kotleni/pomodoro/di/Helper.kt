package app.kotleni.pomodoro.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(listOf(commonModule, iosModule))
    }
}
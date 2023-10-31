package app.kotleni.pomodoro.di

import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.TimerServiceFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val iosModule: Module = module {
    factory { DatabaseDriverFactory() }
    factory { TimerServiceFactory() }
}
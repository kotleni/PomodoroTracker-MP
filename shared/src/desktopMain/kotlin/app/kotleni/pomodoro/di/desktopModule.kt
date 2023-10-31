package app.kotleni.pomodoro.di

import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.TimerServiceFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val desktopModule: Module = module {
    factory { DatabaseDriverFactory() }
    factory { TimerServiceFactory() }
}
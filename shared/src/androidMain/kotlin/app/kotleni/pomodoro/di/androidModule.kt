package app.kotleni.pomodoro.di

import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.TimerServiceFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val androidModule: Module = module {
    factory { DatabaseDriverFactory(androidContext()) }
    factory { TimerServiceFactory() }
}
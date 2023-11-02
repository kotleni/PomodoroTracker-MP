package app.kotleni.pomodoro.di

import org.koin.core.module.Module
import org.koin.dsl.module
import app.kotleni.pomodoro.DatabaseDriverFactory

val desktopModule: Module = module {
    factory { DatabaseDriverFactory() }
}
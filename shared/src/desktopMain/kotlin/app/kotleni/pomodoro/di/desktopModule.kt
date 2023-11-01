package app.kotleni.pomodoro.di

import app.kotleni.pomodoro.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val desktopModule: Module = module {
    factory { DatabaseDriverFactory() }
}
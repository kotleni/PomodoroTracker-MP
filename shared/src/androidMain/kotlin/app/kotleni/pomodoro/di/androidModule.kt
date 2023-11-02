package app.kotleni.pomodoro.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

import app.kotleni.pomodoro.DatabaseDriverFactory

val androidModule: Module = module {
    factory { DatabaseDriverFactory(androidContext()) }
}
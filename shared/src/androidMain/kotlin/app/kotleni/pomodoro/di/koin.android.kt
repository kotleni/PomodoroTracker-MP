package app.kotleni.pomodoro.di

import org.koin.core.module.Module

actual fun platformModule(): Module {
    return androidModule
}
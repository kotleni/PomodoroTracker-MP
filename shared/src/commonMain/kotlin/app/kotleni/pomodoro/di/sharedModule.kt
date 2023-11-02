package app.kotleni.pomodoro.di

import app.kotleni.pomodoro.usecases.CreateTimerUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule: Module = module {
    single { CreateTimerUseCase(get()) }
}
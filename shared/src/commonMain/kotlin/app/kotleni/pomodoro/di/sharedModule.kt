package app.kotleni.pomodoro.di

import app.kotleni.pomodoro.repositories.TimersRepository
import app.kotleni.pomodoro.repositories.TimersRepositoryImpl
import app.kotleni.pomodoro.ui.main.MainViewModel
import app.kotleni.pomodoro.ui.timer.TimerViewModel
import app.kotleni.pomodoro.usecases.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule: Module = module {
    singleOf(::MainViewModel)
    singleOf(::TimerViewModel)

    singleOf(::TimersRepositoryImpl) bind TimersRepository::class

    singleOf(::CreateTimerUseCase)
    singleOf(::RemoveTimerUseCase)
    singleOf(::FetchTimersUseCase)
    singleOf(::LoadTimerByIdUseCase)
    singleOf(::UpdateTimerUseCase)
}
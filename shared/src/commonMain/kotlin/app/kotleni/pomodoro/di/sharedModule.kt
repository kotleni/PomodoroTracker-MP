package app.kotleni.pomodoro.di

import app.kotleni.pomodoro.repositories.TimersRepository
import app.kotleni.pomodoro.repositories.TimersRepositoryImpl
import app.kotleni.pomodoro.usecases.CreateTimerUseCase
import app.kotleni.pomodoro.usecases.FetchTimersUseCase
import app.kotleni.pomodoro.usecases.RemoveTimerUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule: Module = module {
    singleOf(::TimersRepositoryImpl) bind TimersRepository::class

    single { CreateTimerUseCase(get()) }
    single { RemoveTimerUseCase(get()) }
    single { FetchTimersUseCase(get()) }
}
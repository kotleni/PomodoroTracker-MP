package app.kotleni.pomodoro.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

val koinModules = listOf(commonModule, platformModule())

expect fun platformModule(): Module

//fun initKoin() {
//    startKoin {
//        modules(modules)
//    }
//}
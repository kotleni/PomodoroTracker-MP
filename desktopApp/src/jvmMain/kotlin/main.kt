import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.kotleni.pomodoro.di.koinModules
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(koinModules)
    }

    Window(onCloseRequest = ::exitApplication) {
        MainView()
    }
}
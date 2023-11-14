
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.kotleni.pomodoro.di.koinModules
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(koinModules)
    }

    val state = rememberWindowState(
        size = DpSize(420.dp, 700.dp),
        position = WindowPosition(Alignment.Center)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "PomodoroTracker",
        state = state
    ) {
        MainView()
    }
}
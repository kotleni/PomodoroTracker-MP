
import androidx.compose.runtime.Composable
import app.kotleni.pomodoro.ui.main.MainScreen
import app.kotleni.pomodoro.ui.theme.AppTheme
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    AppTheme {
        Navigator(MainScreen())
    }
}

enum class PlatformType {
    DESKTOP, ANDROID, IOS
}

expect fun getPatformType(): PlatformType
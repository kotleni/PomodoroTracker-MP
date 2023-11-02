package app.kotleni.pomodoro

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject
import org.koin.core.parameter.ParametersDefinition

@Composable
actual inline fun <reified TSM : ViewModel> stateMachine(
    noinline parameters: ParametersDefinition?,
): TSM = koinInject<TSM>(
    parameters = parameters,
)
package app.kotleni.pomodoro.ui.timer

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerExitWarningDialog(onPositive: () -> Unit, onNegative: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = "Paused timer")
        },
        text = {
            Text(text = "Do you want stop timer?")
        },
        onDismissRequest = {
            onNegative()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositive()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onNegative()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

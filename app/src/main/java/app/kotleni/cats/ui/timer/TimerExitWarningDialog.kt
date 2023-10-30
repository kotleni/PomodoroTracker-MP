package app.kotleni.cats.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerExitWarningDialog(onPositive: () -> Unit, onNegative: () -> Unit) {
    AlertDialog(
//        icon = {
//            Icon(Icons.Default.Warning, contentDescription = "Icon")
//        },
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

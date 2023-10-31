package app.kotleni.pomodoro.ui.main


import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.kotleni.pomodoro.defaultTimerIcons
import app.kotleni.pomodoro.isDigitsOnly
import getPatformType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformSpecificModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    if(getPatformType() == PlatformType.DESKTOP) {
        AlertDialog(
            modifier = modifier
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            onDismissRequest = onDismissRequest,
            content = content
        )
    } else {
        ModalBottomSheet(
            sheetState = SheetState(skipPartiallyExpanded = true),
            onDismissRequest = onDismissRequest,
            content = { content() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimerDialog(onCreate: (name: String, iconId: Int, workTime: Int, sbrakeTime: Int, lbrakeTime: Int) -> Unit, onDismiss: () -> Unit) {
    var name: String by remember { mutableStateOf("") }
    var iconId: Int by remember { mutableIntStateOf(0) }
    var workTime: String by remember { mutableStateOf("30") }
    var sbrakeTime: String by remember { mutableStateOf("5") }
    var lbrakeTime: String by remember { mutableStateOf("15") }

    PlatformSpecificModal(
        onDismissRequest = onDismiss
    ) {
        //TopAppBar(title = { Text(text = "New timer") })
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = name,
                label = { Text(text = "Name") },
                onValueChange = { name = it }
            )

            Row(
                modifier = Modifier.padding(start = 8.dp, top = 0.dp, bottom = 0.dp, end = 8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    value = workTime,
                    label = { Text(text = "Work time") },
                    suffix = { Text(text = " min") },
                    onValueChange = { workTime = it }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    value = sbrakeTime,
                    label = { Text(text = "Break time") },
                    suffix = { Text(text = " min") },
                    onValueChange = { sbrakeTime = it }
                )
            }

            IconSelector(
                defaultTimerIcons,
                onSelected = { index, _ ->
                    iconId = index
                }
            )

            Row(
                modifier = Modifier.padding(start = 8.dp, top = 0.dp, bottom = 0.dp, end = 8.dp)
            ) {
                OutlinedButton(modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .weight(1f), onClick = onDismiss)
                {
                    Text(text = "Cancel")
                }
                Button(modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .weight(1f), onClick = {
                    if(workTime.isDigitsOnly() && sbrakeTime.isDigitsOnly() && lbrakeTime.isDigitsOnly()) {
                        onCreate(name, iconId, workTime.toInt(), sbrakeTime.toInt(), lbrakeTime.toInt())
                    }
                }) {
                    Text(text = "Create")
                }
            }
        }
    }
}
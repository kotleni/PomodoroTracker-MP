package app.kotleni.pomodoro.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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
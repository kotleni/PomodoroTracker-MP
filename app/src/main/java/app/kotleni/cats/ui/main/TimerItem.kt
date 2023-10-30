package app.kotleni.cats.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.kotleni.cats.Timer
import app.kotleni.cats.defaultTimerIcons
import app.kotleni.cats.toTimeString

@Composable
fun TimerItem(timer: Timer, onItemSelected: () -> Unit) {
    Row(
        modifier = Modifier.clickable { onItemSelected() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding(start = 16.dp, top = 0.dp, bottom = 0.dp, end = 8.dp)
        ) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
                imageVector = defaultTimerIcons[timer.iconId],
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(modifier = Modifier.padding(0.dp), text = timer.name, fontWeight = FontWeight.Bold)
            Text(modifier = Modifier.padding(0.dp), text = "Total work time: ${timer.totalWorkTime.toInt().toTimeString()}")
        }
    }
}
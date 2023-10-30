package app.kotleni.cats.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.kotleni.cats.Timer
import app.kotleni.cats.defaultTimerIcons
import app.kotleni.cats.toTimeString

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TimerItem(timer: Timer, onItemSelected: () -> Unit, onItemRemoved: () -> Unit) {
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                onItemRemoved()
                false
            } else {
                true
            }
        }
    )

    SwipeToDismiss(
        //modifier = Modifier.animateItemPlacement(),
        state = dismissState,
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                else -> Color.Transparent
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
//                    Spacer(modifier = Modifier.heightIn(8.dp))
//                    Text(
//                        text = "Move to Bin",
//                        textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )

                }
            }
        },
        directions = setOf(DismissDirection.EndToStart),
        dismissContent = {
            Box(
                modifier = Modifier
                    //.padding(16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .clip(RectangleShape)
            ) {
                Row(
                    modifier = Modifier
                        .clickable { onItemSelected() }
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
        }
    )
}
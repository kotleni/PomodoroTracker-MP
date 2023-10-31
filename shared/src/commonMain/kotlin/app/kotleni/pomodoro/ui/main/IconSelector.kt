package app.kotleni.pomodoro.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconSelector(icons: List<ImageVector>, onSelected: (index: Int, icon: ImageVector) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, top = 8.dp, bottom = 8.dp, end = 0.dp)
    ) {
        item { Spacer(modifier = Modifier.width(8.dp)) }
        items(icons.size) { index ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        selectedIndex = index
                        onSelected(index, icons[index])
                    }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedIndex == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Image(
                        modifier = Modifier
                            .padding(16.dp),
                        imageVector = icons[index],
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.width(8.dp)) }
    }
}
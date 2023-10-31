package app.kotleni.pomodoro.ui.main

import PlatformType
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import getPatformType

@Composable
fun IconSelector(icons: List<ImageVector>, onSelected: (index: Int, icon: ImageVector) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    if(getPatformType() == PlatformType.DESKTOP) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 8.dp, bottom = 8.dp, end = 0.dp)
        ) {
            items(icons.size) { index ->
                IconSelectorItem(icons[index], selectedIndex == index, onSelected = {
                    selectedIndex = index
                    onSelected(index, icons[index])
                })
            }
        }
    } else {
        LazyRow (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 8.dp, bottom = 8.dp, end = 0.dp)
        ) {
            item { Spacer(modifier = Modifier.width(8.dp)) }
            items(icons.size) { index ->
                IconSelectorItem(icons[index], selectedIndex == index, onSelected = {
                    selectedIndex = index
                    onSelected(index, icons[index])
                })
            }
            item { Spacer(modifier = Modifier.width(8.dp)) }
        }
    }
}
package app.kotleni.cats

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star

val defaultTimerIcons = listOf(
    Icons.Default.Favorite,
    Icons.Default.Star,
    Icons.Default.Build,
    Icons.Default.LocationOn,
    Icons.Default.Home,
    Icons.Default.ShoppingCart,
    Icons.Default.DateRange,
)

fun Int.toTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "$minutes:${if(seconds.toString().length > 1) seconds else "0$seconds"}"
}

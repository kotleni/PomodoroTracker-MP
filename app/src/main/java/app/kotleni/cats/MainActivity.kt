package app.kotleni.cats

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.kotleni.cats.ui.main.MainScreen
import app.kotleni.cats.ui.theme.CatsTheme
import app.kotleni.cats.ui.timer.TimerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val notificationManager = getSystemService(NotificationManager::class.java)
        if (!notificationManager.areNotificationsEnabled()) {
            // Notifications are not enabled, so request the permission.
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        }

        setContent {
            CatsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val rootNavController = rememberNavController()

                    NavHost(
                        navController = rootNavController,
                        startDestination = "main"
                    ) {
                        composable("main") {
                            MainScreen(rootNavController)
                        }
                        composable("timer/{timerId}", arguments = listOf(navArgument("timerId") { type = NavType.IntType})) { backStackEntry ->
                            TimerScreen(rootNavController, backStackEntry.arguments?.getInt("timerId") ?: 0)
                        }
                    }
                }
            }
        }
    }
}









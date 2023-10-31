package app.kotleni.cats.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import app.kotleni.cats.MainActivity
import app.kotleni.cats.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// MARK: Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
class NotificationsHelper @Inject constructor(@ApplicationContext val context: Context) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Pomodoro",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.canBypassDnd()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            channel.canBubble()
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showAlarmNotification(title: String, content: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = System.currentTimeMillis().toString()
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        notificationManager.notify(ALARM_NOTIFICATION_ID, notification)
    }

    fun showPermanentNotification(title: String, content: String) {
//        val intent = Intent(context, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setShowWhen(false)
            .setOngoing(true)
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager.notify(PERMANENT_NOTIFICATION_ID, notification)
    }

    fun closePermanentNotification() {
        notificationManager.cancel(PERMANENT_NOTIFICATION_ID)
    }

    companion object {
        private const val CHANNEL_ID = "my_app_channel"
        private const val PERMANENT_NOTIFICATION_ID = 1
        private const val ALARM_NOTIFICATION_ID = 1
    }
}
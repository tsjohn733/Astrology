package com.example.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class DailyReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val tip = intent?.getStringExtra(EXTRA_TIP)
            ?: "Align your high-leverage tasks before noon; planetary discipline is at peak support."
        showNotification(context, tip)
    }

    companion object {
        const val CHANNEL_ID = "daily_astrology_insights"
        const val CHANNEL_NAME = "Daily Astrological Transits & Tips"
        const val EXTRA_TIP = "extra_tip"
        const val NOTIFICATION_ID = 1001

        fun showNotification(context: Context, tip: String) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Daily cosmic weather, transit changes, and personalized tips"
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            val launchIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("✦ JyotishVeda • Daily Transit Insights")
                .setContentText(tip)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("$tip\n\nTap to explore your active Gochar transits, divisional charts, and planetary intelligence.")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}

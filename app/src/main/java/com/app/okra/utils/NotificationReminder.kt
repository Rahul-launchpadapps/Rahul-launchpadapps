package com.app.okra.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.okra.R
import com.app.okra.ui.my_reminder.SetReminderFragment

fun displayNotification(context: Context, broadCastIntent: Intent) {
    val uniqueId = System.currentTimeMillis().toInt()
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(context, SetReminderFragment::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    var pendingIntent: PendingIntent
    /*pendingIntent = if (isAppIsInBackground(context)) {
        PendingIntent.getActivity(context, uniqueId, intent, PendingIntent.FLAG_ONE_SHOT)
    } else {*/
    pendingIntent =
        PendingIntent.getActivity(context, uniqueId, Intent(), PendingIntent.FLAG_ONE_SHOT)
    //  }
    val defaultSoundUri: Uri =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val id = "channel$uniqueId"
    if (notificationManager != null) {
        val notificationId = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                id,
                context.resources.getString(R.string.app_name),
                importance
            )
            // Configure the notification channel.
            mChannel.description = ""
            mChannel.setShowBadge(true)
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(mChannel)
        }
    }
    val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        "0"
    )
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentText("")
        .setAutoCancel(true)
        .setContentTitle("Reminder")
        .setSound(defaultSoundUri)
        .setColor(Color.rgb(109, 2, 92))
        .setStyle(NotificationCompat.BigTextStyle())
        .setChannelId(id)
        .setContentIntent(pendingIntent)
    notificationManager?.notify(uniqueId, notificationBuilder.build())
    val notification: Notification = notificationBuilder.build()
    notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    notification.flags =
        notification.flags or Notification.FLAG_AUTO_CANCEL //Do not clear  the notification
    notification.defaults = notification.defaults or Notification.DEFAULT_LIGHTS // LED
    notification.defaults =
        notification.defaults or Notification.DEFAULT_VIBRATE //Vibration
    notificationManager?.notify(uniqueId, notification)
}

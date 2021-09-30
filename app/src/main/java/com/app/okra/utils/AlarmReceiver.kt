package com.app.okra.utils

import android.content.BroadcastReceiver
import android.app.NotificationManager
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    private val manager: NotificationManager? = null
    private val NOTIFICATION_ID = 0
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && context != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                return
            }
        }
        //Trigger the notification
        displayNotification(context, intent)
    }
}
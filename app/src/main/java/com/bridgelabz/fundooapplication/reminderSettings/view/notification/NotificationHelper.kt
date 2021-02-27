package com.bridgelabz.fundooapplication.reminderSettings.view.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bridgelabz.fundooapplication.R

class NotificationHelper(base: Context) : ContextWrapper(base) {
    lateinit var notificationManager: NotificationManager
    val CHANNELID = "REMINDER"
    val CHANNELNAME = "REMINDER_NOTIFICATION"


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel()
    }

    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNELID, CHANNELNAME, importance)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    fun getNotification(
        noteTitle: String,
        noteDescription: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNELID)
            .setSmallIcon(R.drawable.ic_baseline_event_note_24)
            .setContentTitle(noteTitle)
            .setContentText(noteDescription)
            .setColor(Color.BLUE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
}
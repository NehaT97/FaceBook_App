package com.bridgelabz.fundooapplication.reminderSettings.view.alarmReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bridgelabz.fundooapplication.reminderSettings.view.notification.NotificationHelper

class MyAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i("COMING","HERE U R IN")
        Log.d("Alarm Bell", "Alarm just fired ${System.currentTimeMillis()}")
        val noteTitle = intent?.getStringExtra("noteTitle")
        val noteDescription = intent?.getStringExtra("noteDescription")
        Log.i("NOTE_DATA_REACHED",noteTitle)
        Log.i("NOTE_DESCRIPTION_REACHED",noteDescription)

        val notificationHelper: NotificationHelper = NotificationHelper(context)
        val notificationBuilder:NotificationCompat.Builder = notificationHelper.getNotification(
            noteTitle!!,noteDescription!!)
        notificationHelper.notificationManager.notify(1,notificationBuilder.build())

    }
}

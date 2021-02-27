package com.bridgelabz.fundooapplication.service

import android.util.Log
import androidx.core.app.NotificationCompat
import com.bridgelabz.fundooapplication.reminderSettings.view.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    val TAG = "FCM Service"
    @Override
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived : Message Receive From: " + remoteMessage!!.from);
        Log.d(TAG, "onMessageReceived : Notification Message Body: " + remoteMessage.notification?.body);

        if (remoteMessage.notification != null){
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

            Log.d("TAG","onMessageReceived : My custom Notification:To give notification when i am in foreground")

            val notificationHelper: NotificationHelper = NotificationHelper(this)
            val notificationBuilder: NotificationCompat.Builder = notificationHelper.getNotification(
                title!!,body!!)
            notificationHelper.notificationManager.notify(1,notificationBuilder.build())

        }

        if (remoteMessage.data.isNotEmpty()){
            Log.d(TAG,"onMessageReceived Data:When i give key and value" + remoteMessage.data.toString())
        }
    }

    @Override
    override fun onNewToken(refreshToken: String) {
        super.onNewToken(refreshToken)
        Log.d("after Uninstall", "Refreshed token: $refreshToken")
    }

}


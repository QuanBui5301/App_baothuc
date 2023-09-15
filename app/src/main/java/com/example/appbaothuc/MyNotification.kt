package com.example.appbaothuc

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyNotification : Application() {
    companion object {
        const val CHANNEL_ID : String = "channel_service_example"
    }


    override fun onCreate() {
        super.onCreate()
        createChannelNotification()
    }

    private fun createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                CHANNEL_ID,
                "My Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            )
            chan.setSound(null, null)
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(chan)
        }
    }
}
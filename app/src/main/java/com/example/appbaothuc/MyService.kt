package com.example.appbaothuc

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.appbaothuc.MainActivity.Companion.isLoop
import com.example.appbaothuc.MainActivity.Companion.lifecycleOwner
import com.example.appbaothuc.MainActivity.Companion.myViewModel
import com.example.appbaothuc.MyNotification.Companion.CHANNEL_ID

class MyService : Service() {
    lateinit var mediaPlayer: MediaPlayer
    private var close_action : Int = 1
    var time : String = ""
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("Action", "Alarm begin")
        Toast.makeText(this, "Alarm", Toast.LENGTH_SHORT).show()
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.start()
        if (isLoop) {
            mediaPlayer.setLooping(true)
            Log.d("isLoop", "Yes")
        }
        if (!isLoop) {
            mediaPlayer.setLooping(false)
            Log.d("isLoop", "No")
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendNotification()
        var action_receive : Int = intent!!.getIntExtra("action", 0)
        time = intent!!.getStringExtra("alarm").toString()
        if (action_receive == close_action) {
            stopService(intent)
        }
        return START_STICKY
    }
    private fun sendNotification() {
        var intent1:Intent = Intent(this, MainActivity::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0 , intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        var remoteViews : RemoteViews = RemoteViews(getPackageName(), R.layout.notification_layout)
        myViewModel.currentAlarm.observe(lifecycleOwner, Observer {
            remoteViews.setTextViewText(R.id.text_tittle, it)
        })
        remoteViews.setImageViewResource(R.id.img_song, R.drawable.ic_baseline_notifications_24)
        remoteViews.setImageViewResource(R.id.play_btn, R.drawable.ic_baseline_close_24)
        remoteViews.setOnClickPendingIntent(R.id.play_btn, getPendingIntent(this, close_action))


        var notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_alarm_on_24)
            .setSound(null)
            .setCustomContentView(remoteViews)
            .build()
        startForeground(1, notification)
        stopForeground(false)
    }

    private fun getPendingIntent(context : Context, action : Int) : PendingIntent {
        var intent : Intent = Intent(this, ServiceBroadcast::class.java)
        intent.putExtra("action", action)

        return PendingIntent.getBroadcast(context.applicationContext, action, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
    }
}
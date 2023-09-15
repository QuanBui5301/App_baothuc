package com.example.appbaothuc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmBroadCast : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        var intent = Intent(p0, MyService::class.java)
        p0!!.startService(intent)
    }
}
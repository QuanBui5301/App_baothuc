package com.example.appbaothuc

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var btnSet : ImageButton
    lateinit var checkBox: CheckBox
    lateinit var timeText: EditText
    private var mSaveDay = 0
    private var mSaveMonth = 0
    private var mSaveYear = 0
    private var mSaveHour = 0
    private var mSaveMinute = 0

    var state : Boolean = false
    companion object {
        lateinit var myViewModel: alarmModelView
        lateinit var lifecycleOwner: LifecycleOwner
        var isLoop : Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myViewModel = ViewModelProvider(this).get(alarmModelView::class.java)
        lifecycleOwner = this
        btnSet = findViewById(R.id.imageButton)
        checkBox = findViewById(R.id.checkbox)
        timeText = findViewById(R.id.time)
        checkBox.setOnClickListener() {
            isLoop = checkBox.isChecked
        }
        btnSet.setOnClickListener() {
            if (!state) {
                createTime()
                state = true
                myViewModel.currentState.value = state

            }
            else {
                cancelAlarm()
                state = false
                timeText.text.clear()
                myViewModel.currentState.value = state
            }
        }
        myViewModel.currentState.observe(this, Observer {
            if (it) {
                btnSet.setImageResource(R.drawable.ic_baseline_alarm_on_24)
            }
            if (!it) {
                btnSet.setImageResource(R.drawable.ic_baseline_alarm_24)
            }
        })
    }

    fun createTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        DatePickerDialog(this, { _, year, month, day ->
            TimePickerDialog(this, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                mSaveYear = year
                mSaveMonth = month
                mSaveDay = day
                mSaveHour = hour
                mSaveMinute = minute
                currentDateTime.set(mSaveYear, mSaveMonth, mSaveDay, mSaveHour, mSaveMinute)
                val time: Long = currentDateTime.timeInMillis
                val isDay : Int =  boolToInt(mSaveDay<10)
                val isMonth : Int =  boolToInt(mSaveMonth<10)
                val isHour : Int = boolToInt(mSaveHour<10)
                val isMinute : Int = boolToInt(mSaveMinute<10)
                val isTime : String = "$isMonth$isDay$isHour$isMinute"
                val timeHour : String = timeFormat(isTime)
                myViewModel.currentAlarm.value=timeHour
                timeText.setText(timeHour)
                createAlarm(time)
            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    fun createAlarm(time : Long) {
        val intent = Intent(this, AlarmBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }
    private fun cancelAlarm() {
        val intent = Intent(this, AlarmBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
    private fun boolToInt(item : Boolean) : Int {
        return if (item) {
            1
        } else 0
    }
    fun timeFormat(isTime : String) : String {
        when (isTime) {
            "0000" -> return "$mSaveDay/${mSaveMonth+1}/$mSaveYear $mSaveHour:$mSaveMinute"
            "1000" -> return "$mSaveDay/0${mSaveMonth+1}/$mSaveYear $mSaveHour:$mSaveMinute"
            "0100" -> return "0$mSaveDay/${mSaveMonth+1}/$mSaveYear $mSaveHour:$mSaveMinute"
            "0010" -> return "$mSaveDay/${mSaveMonth+1}/$mSaveYear 0$mSaveHour:$mSaveMinute"
            "0001" -> return "$mSaveDay/${mSaveMonth+1}/$mSaveYear $mSaveHour:0$mSaveMinute"
            "1100" -> return "0$mSaveDay/0${mSaveMonth+1}/$mSaveYear $mSaveHour:$mSaveMinute"
            "1010" -> return "$mSaveDay/0${mSaveMonth+1}/$mSaveYear 0$mSaveHour:$mSaveMinute"
            "1001" -> return "$mSaveDay/0${mSaveMonth+1}/$mSaveYear $mSaveHour:0$mSaveMinute"
            "0101" -> return "0$mSaveDay/${mSaveMonth+1}/$mSaveYear $mSaveHour:0$mSaveMinute"
            "0110" -> return "0$mSaveDay/${mSaveMonth+1}/$mSaveYear 0$mSaveHour:$mSaveMinute"
            "0011" -> return "$mSaveDay/${mSaveMonth+1}/$mSaveYear 0$mSaveHour:0$mSaveMinute"
            "1110" -> return "0$mSaveDay/0${mSaveMonth+1}/$mSaveYear 0$mSaveHour:$mSaveMinute"
            "1101" -> return "0$mSaveDay/0${mSaveMonth+1}/$mSaveYear $mSaveHour:0$mSaveMinute"
            "1011" -> return "$mSaveDay/0${mSaveMonth+1}/$mSaveYear 0$mSaveHour:0$mSaveMinute"
            "0111" -> return "0$mSaveDay/${mSaveMonth+1}/$mSaveYear 0$mSaveHour:0$mSaveMinute"
            "1111" -> return "0$mSaveDay/0${mSaveMonth+1}/$mSaveYear 0$mSaveHour:0$mSaveMinute"
            else -> return "$mSaveDay/${mSaveMonth+1}/$mSaveYear $mSaveHour:$mSaveMinute"
        }
    }
}
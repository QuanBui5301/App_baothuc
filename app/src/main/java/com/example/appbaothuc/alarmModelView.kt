package com.example.appbaothuc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class alarmModelView : ViewModel() {
    val currentState = MutableLiveData<Boolean>()
    val currentAlarm = MutableLiveData<String>()
}
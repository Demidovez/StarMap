package com.nikolaydemidovez.starmap.controllers.event_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventV1ControllerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Event Fragment"
    }

    val text: LiveData<String> = _text
}
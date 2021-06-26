package com.nikolaydemidovez.starmap.ui.template.controllers.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventV1ControllerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Event Fragment"
    }

    val text: LiveData<String> = _text
}
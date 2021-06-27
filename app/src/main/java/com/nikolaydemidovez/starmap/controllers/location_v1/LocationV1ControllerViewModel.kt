package com.nikolaydemidovez.starmap.controllers.location_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationV1ControllerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Location Fragment"
    }

    val text: LiveData<String> = _text
}
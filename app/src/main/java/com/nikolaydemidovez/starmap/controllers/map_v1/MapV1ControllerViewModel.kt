package com.nikolaydemidovez.starmap.controllers.map_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapV1ControllerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Map Fragment"
    }

    val text: LiveData<String> = _text
}
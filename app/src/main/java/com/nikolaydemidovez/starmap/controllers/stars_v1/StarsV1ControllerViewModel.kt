package com.nikolaydemidovez.starmap.controllers.stars_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StarsV1ControllerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Stars Fragment"
    }

    val text: LiveData<String> = _text
}
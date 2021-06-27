package com.nikolaydemidovez.starmap.controllers.canvas_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CanvasV1ControllerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Canvas Fragment"
    }

    val text: LiveData<String> = _text
}
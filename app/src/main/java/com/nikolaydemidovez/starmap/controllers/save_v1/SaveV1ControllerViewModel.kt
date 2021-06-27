package com.nikolaydemidovez.starmap.controllers.save_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SaveV1ControllerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Save Fragment"
    }

    val text: LiveData<String> = _text
}
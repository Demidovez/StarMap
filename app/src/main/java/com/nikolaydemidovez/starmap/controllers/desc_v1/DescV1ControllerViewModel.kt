package com.nikolaydemidovez.starmap.controllers.desc_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DescV1ControllerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Desc Fragment"
    }

    val text: LiveData<String> = _text
}
package com.nikolaydemidovez.starmap.controllers.separator_v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeparatorV1ControllerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Separator Fragment"
    }

    val text: LiveData<String> = _text
}
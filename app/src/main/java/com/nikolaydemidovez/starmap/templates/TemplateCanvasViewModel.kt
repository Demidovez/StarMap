package com.nikolaydemidovez.starmap.templates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TemplateCanvasViewModel : ViewModel() {
    private val _backgroundColorCanvas = MutableLiveData<String>().apply {
        value = "Canvas Fragment"
    }

    val backgroundColorCanvas: LiveData<String> = _backgroundColorCanvas
}
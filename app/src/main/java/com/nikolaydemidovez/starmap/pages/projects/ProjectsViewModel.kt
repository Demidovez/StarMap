package com.nikolaydemidovez.starmap.pages.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProjectsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is projects Fragment"
    }
    val text: LiveData<String> = _text
}
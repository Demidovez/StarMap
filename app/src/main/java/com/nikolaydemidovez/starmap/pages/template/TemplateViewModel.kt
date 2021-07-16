package com.nikolaydemidovez.starmap.pages.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikolaydemidovez.starmap.retrofit.common.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TemplateViewModel(private val templateName: String) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is template Fragment"
    }

    val text: LiveData<String> = _text
}
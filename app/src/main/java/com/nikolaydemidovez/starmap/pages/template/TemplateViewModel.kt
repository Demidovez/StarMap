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

    private val _controllerList = MutableLiveData<List<Controller>>().apply {

        Common.retrofitService.getControllerList(templateName).enqueue(object : Callback<List<Controller>> {
            override fun onFailure(call: Call<List<Controller>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<Controller>>?, response: Response<List<Controller>>?) {
                if(response?.body() != null)
                    value = response.body()!!
            }
        })
    }

    val text: LiveData<String> = _text
    val controllerList: LiveData<List<Controller>> = _controllerList
}
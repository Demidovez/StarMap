package com.nikolaydemidovez.starmap.pages.templates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikolaydemidovez.starmap.retrofit.common.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TemplatesViewModel : ViewModel() {

    private val _templateList = MutableLiveData<List<Template>>().apply {


        Common.retrofitService.getTemplateList().enqueue(object : Callback<List<Template>> {
            override fun onFailure(call: Call<List<Template>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<Template>>?, response: Response<List<Template>>?) {
                if(response?.body() != null)
                value = response.body()!!
            }
        })
    }

    val templateList: LiveData<List<Template>> = _templateList
}


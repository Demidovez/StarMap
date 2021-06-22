package com.nikolaydemidovez.starmap.retrofit.interfaces

import com.nikolaydemidovez.starmap.ui.templates.Template
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServices {
    @GET("all_templates")
    fun getTemplateList(): Call<List<Template>>
}
package com.nikolaydemidovez.starmap.retrofit.interfaces

import com.nikolaydemidovez.starmap.ui.template.Controller
import com.nikolaydemidovez.starmap.ui.templates.Template
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitServices {
    @GET("all_templates")
    fun getTemplateList(): Call<List<Template>>

    @GET("get_controllers/{template}")
    fun getControllerList(@Path("template") template: String?): Call<List<Controller>>
}
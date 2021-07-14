package com.nikolaydemidovez.starmap.retrofit.interfaces

import com.google.gson.Gson
import com.nikolaydemidovez.starmap.pages.template.Controller
import com.nikolaydemidovez.starmap.pages.templates.Template
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitServices {
    @GET("all_templates")
    fun getTemplateList(): Call<List<Template>>

    @GET("get_controllers/{template}")
    fun getControllerList(@Path("template") template: String?): Call<List<Controller>>

    @POST("get_classic_v1_map")
    fun getClassicV1Map(@Body data: RequestBody): Call<ResponseBody>
}
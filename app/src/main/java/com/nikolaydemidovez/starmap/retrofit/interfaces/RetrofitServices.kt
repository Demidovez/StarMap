package com.nikolaydemidovez.starmap.retrofit.interfaces

import com.nikolaydemidovez.starmap.pojo.Template
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitServices {
    @GET("all_templates")
    fun getTemplateList(): Call<List<Template>>

    @POST("get_classic_v1_map")
    fun getClassicV1Map(@Body data: RequestBody): Call<ResponseBody>
}
package com.nikolaydemidovez.starmap.retrofit.common

import com.nikolaydemidovez.starmap.retrofit.RetrofitClient
import com.nikolaydemidovez.starmap.retrofit.interfaces.RetrofitServices

object Common {
    private const val BASE_URL = "http://80.78.247.152:3000/"

    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}
package com.example.paylite_mobile.helper.retrofit

import com.example.paylite_mobile.helper.retrofit.response.BaseResponse
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {

    @GET("/")
    suspend fun getApi():Response<BaseResponse>
}
package com.example.paylite_mobile.helper.retrofit.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("status") var status:String? = "",
    @SerializedName("message") var message:String? = ""
)

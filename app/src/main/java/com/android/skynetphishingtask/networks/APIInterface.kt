package com.android.skynetphishingtask.networks

import com.android.skynetphishingtask.models.ResponseModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIInterface {

    @FormUrlEncoded
    @POST("checkurl/")
    fun checkURL(
        @Field("url") url: String,
        @Field("format") format: String
    ): Call<ResponseModel>
}
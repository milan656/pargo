package com.tntra.pargo.networkApi.login

import com.tntra.pargo.model.generes.GeneresListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface GeneresApi {

    @GET("api/v1/genres")
    fun generes(
            @Header("Authorization") Authorization: String
    ): Call<GeneresListModel>

}
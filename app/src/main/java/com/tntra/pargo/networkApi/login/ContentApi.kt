package com.tntra.pargo.networkApi.login

import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.content_list.ContentListModel
import com.tntra.pargo.model.content_list.show.ContentShowModel
import com.tntra.pargo.model.contentcreate.ContentCreateModel
import com.tntra.pargo.model.treading_content.TreadingContentModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ContentApi {

    @GET("api/v1/contents")
    fun contentList(
            @Header("Authorization") Authorization: String
    ): Call<ContentListModel>

    @GET("api/v1/contents/{id}")
    fun contentShow(
            @Header("Authorization") Authorization: String,
            @Path("id") id: Int
    ): Call<ContentShowModel>

    @GET("api/v1/contents")
    fun treadingContent(
            @Header("Authorization") Authorization: String,
            @Query("type") type: String, @Query("page") page: Int, @Query("type_wise_content") type_wise_content: String
    ): Call<TreadingContentModel>

    @POST("api/v1/contents/{id}/likes")
    fun likeUnLikeApi(
            @Header("Authorization") Authorization: String,
            @Path("id") id: Int
    ): Call<CommonResponseModel>

//    @Multipart
//    @POST("api/v1/contents")
//    fun uploadContent(
//            @Part file: MultipartBody.Part,
//            @Header("Authorization") authorizationToke: String,
//            @Part("content[title]") title: RequestBody,
//            @Part("content[body]") description: RequestBody,
//            @Part("content[genre_id]") genersId: RequestBody,
//            @Part("content[accessibility]") accessibility: RequestBody,
//            @Part("content[post_type]") post_type: RequestBody,
//            @Part("content[posts]") posts: RequestBody
//    ): Call<ResponseBody>


    @Multipart
    @POST("api/v1/contents")
    fun uploadContent(
            @Header("Authorization") authorizationToke: String,
            @Part file: MultipartBody.Part,
            @Part("content[title]") title: RequestBody,
            @Part("content[body]") description: RequestBody,
            @Part("content[genre_id]") genersId: RequestBody,
            @Part("content[accessibility]") accessibility: RequestBody,
            @Part("content[post_type]") post_type: RequestBody,
            @Part("content[posts]") posts: RequestBody,
            @Part("content[duration]") duration: RequestBody
    ): Call<ContentCreateModel>?

}
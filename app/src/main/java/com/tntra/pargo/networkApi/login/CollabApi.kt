package com.tntra.pargo.networkApi.login

import com.google.gson.JsonObject
import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.collab_req.CollabRequestModel
import com.tntra.pargo.model.collabroom.CollabRoomCreateModel
import com.tntra.pargo.model.collabsession.CollabSessionModel
import com.tntra.pargo.model.followers.FollowerListModel
import com.tntra.pargo.model.followers.FollowerModel
import com.tntra.pargo.model.login_response.UserLoginModel
import retrofit2.Call
import retrofit2.http.*

interface CollabApi {

    @POST("api/v1/collab_sessions")
    fun collabSession(
            @Header("Authorization") Authorization: String,
            @Body jsonObject: JsonObject,
    ): Call<CollabSessionModel>

    @POST("api/v1/collab_requests")
    fun collabReq(
            @Header("Authorization") Authorization: String,
            @Body jsonObject: JsonObject,
    ): Call<CollabRequestModel>

    @GET("api/v1/follows")
    fun followersList(
            @Header("Authorization") Authorization: String,
            @Query("user_id") user_id: Int,
            @Query("page") page: Int,
            @Query("type") type: String
    ): Call<FollowerListModel>

    @POST("api/v1/collab_rooms")
    fun createCollabroom(
            @Header("Authorization") Authorization: String,
            @Body jsonObject: JsonObject
    ): Call<CollabRoomCreateModel>

    @PUT("api/v1/collab_requests/{id}")
    fun collabAcceptReject(
//            @Header("Authorization") Authorization: String,
            @Body jsonObject: JsonObject,
            @Path("id") id: Int,
    ): Call<CommonResponseModel>
}
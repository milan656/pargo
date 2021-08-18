package com.tntra.pargo.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.RetrofitCommonClass
import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.collab_req.CollabRequestModel
import com.tntra.pargo.model.collabroom.CollabRoomCreateModel
import com.tntra.pargo.model.collabsession.CollabSessionModel
import com.tntra.pargo.model.followers.FollowerListModel
import com.tntra.pargo.model.followers.FollowerModel
import com.tntra.pargo.networkApi.login.CollabApi

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CollabSessionRepository {

    var collabSessionRepository: CollabSessionRepository? = null

    var collabApi: CollabApi

    constructor() {
        collabApi = RetrofitCommonClass.createService(CollabApi::class.java)
    }


    companion object

    fun getInstance(): CollabSessionRepository {
        if (collabSessionRepository == null) {
            collabSessionRepository = CollabSessionRepository()
        }
        return collabSessionRepository as CollabSessionRepository
    }

    fun collabSessionApi(
            authorizationToke: String,
            jsonObject: JsonObject
    ): MutableLiveData<CollabSessionModel> {
        val loginData = MutableLiveData<CollabSessionModel>()
        collabApi.collabSession(
                authorizationToke, jsonObject
        ).enqueue(object : Callback<CollabSessionModel> {
            override fun onResponse(
                    call: Call<CollabSessionModel>,
                    response: Response<CollabSessionModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val CollabSessionModel: CollabSessionModel =
                                Common.getErrorModel(jsonObjectError, "CollabSessionModel") as CollabSessionModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = CollabSessionModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<CollabSessionModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun collabReqApi(
            authorizationToke: String,
            jsonObject: JsonObject
    ): MutableLiveData<CollabRequestModel> {
        val loginData = MutableLiveData<CollabRequestModel>()
        collabApi.collabReq(
                authorizationToke, jsonObject
        ).enqueue(object : Callback<CollabRequestModel> {
            override fun onResponse(
                    call: Call<CollabRequestModel>,
                    response: Response<CollabRequestModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val CollabRequestModel: CollabRequestModel =
                                Common.getErrorModel(jsonObjectError, "CollabRequestModel") as CollabRequestModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = CollabRequestModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<CollabRequestModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun followersListApi(
            authorizationToke: String,
            userId: Int,
            page: Int,
            type: String
    ): MutableLiveData<FollowerListModel> {
        val loginData = MutableLiveData<FollowerListModel>()
        collabApi.followersList(
                authorizationToke, userId, page, type
        ).enqueue(object : Callback<FollowerListModel> {
            override fun onResponse(
                    call: Call<FollowerListModel>,
                    response: Response<FollowerListModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val FollowerListModel: FollowerListModel =
                                Common.getErrorModel(jsonObjectError, "FollowerListModel") as FollowerListModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = FollowerListModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<FollowerListModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun callApiCollabRoom(
            authorizationToke: String,
            jsonObject: JsonObject

    ): MutableLiveData<CollabRoomCreateModel> {
        val loginData = MutableLiveData<CollabRoomCreateModel>()
        collabApi.createCollabroom(
                authorizationToke, jsonObject
        ).enqueue(object : Callback<CollabRoomCreateModel> {
            override fun onResponse(
                    call: Call<CollabRoomCreateModel>,
                    response: Response<CollabRoomCreateModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val CollabRoomCreateModel: CollabRoomCreateModel =
                                Common.getErrorModel(jsonObjectError, "CollabRoomCreateModel") as CollabRoomCreateModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = CollabRoomCreateModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<CollabRoomCreateModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun collabAcceptReject(
            authorizationToke: String,
            jsonObject: JsonObject,
            id: Int
    ): MutableLiveData<CommonResponseModel> {
        val loginData = MutableLiveData<CommonResponseModel>()
        collabApi.collabAcceptReject(
                /*authorizationToke,*/ jsonObject, id
        ).enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                    call: Call<CommonResponseModel>,
                    response: Response<CommonResponseModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val CommonResponseModel: CommonResponseModel =
                                Common.getErrorModel(jsonObjectError, "CommonResponseModel") as CommonResponseModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = CommonResponseModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }
}
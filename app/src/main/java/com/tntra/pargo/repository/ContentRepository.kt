package com.tntra.pargo.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.RetrofitCommonClass
import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.collabsession.CollabSessionModel
import com.tntra.pargo.model.comments.CommentListModel
import com.tntra.pargo.model.comments.list.CommentsListingModel
import com.tntra.pargo.model.content_list.Content
import com.tntra.pargo.model.content_list.ContentListModel
import com.tntra.pargo.model.content_list.show.ContentShowModel
import com.tntra.pargo.model.treading_content.TreadingContentModel
import com.tntra.pargo.networkApi.login.CollabApi
import com.tntra.pargo.networkApi.login.ContentApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContentRepository {

    var contentRepository: ContentRepository? = null

    var contentApi: ContentApi? = null

    constructor() {
        contentApi = RetrofitCommonClass.createService(ContentApi::class.java)
    }


    companion object

    fun getInstance(): ContentRepository {
        if (contentRepository == null) {
            contentRepository = ContentRepository()
        }
        return contentRepository as ContentRepository
    }

    fun contentListApi(
            authorizationToke: String,
    ): MutableLiveData<ContentListModel> {
        val loginData = MutableLiveData<ContentListModel>()
        contentApi?.contentList(
                authorizationToke
        )?.enqueue(object : Callback<ContentListModel> {
            override fun onResponse(
                    call: Call<ContentListModel>,
                    response: Response<ContentListModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val ContentListModel: ContentListModel =
                                Common.getErrorModel(jsonObjectError, "ContentListModel") as ContentListModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = ContentListModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ContentListModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun contentShowApi(
            authorizationToke: String,
            id: Int
    ): MutableLiveData<ContentShowModel> {
        val loginData = MutableLiveData<ContentShowModel>()
        contentApi?.contentShow(
                authorizationToke, id
        )?.enqueue(object : Callback<ContentShowModel> {
            override fun onResponse(
                    call: Call<ContentShowModel>,
                    response: Response<ContentShowModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val ContentShowModel: ContentShowModel =
                                Common.getErrorModel(jsonObjectError, "ContentShowModel") as ContentShowModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = ContentShowModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ContentShowModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun commentAddApi(
            authorizationToke: String,
            jsonObject: JsonObject,
            id: Int
    ): MutableLiveData<CommentListModel> {
        val loginData = MutableLiveData<CommentListModel>()
        contentApi?.commentAdd(
                authorizationToke, jsonObject, id
        )?.enqueue(object : Callback<CommentListModel> {
            override fun onResponse(
                    call: Call<CommentListModel>,
                    response: Response<CommentListModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val CommentListModel: CommentListModel =
                                Common.getErrorModel(jsonObjectError, "CommentListModel") as CommentListModel
                        //recoveryData.setValue(recoveryPasswordModel)

                        loginData.value = CommentListModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<CommentListModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun commentListApi(
            authorizationToke: String,
            id: Int
    ): MutableLiveData<CommentsListingModel> {
        val loginData = MutableLiveData<CommentsListingModel>()
        contentApi?.commentList(
                authorizationToke, id
        )?.enqueue(object : Callback<CommentsListingModel> {
            override fun onResponse(
                    call: Call<CommentsListingModel>,
                    response: Response<CommentsListingModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val CommentsListingModel: CommentsListingModel =
                                Common.getErrorModel(jsonObjectError, "CommentsListingModel") as CommentsListingModel
                        //recoveryData.setValue(recoveryPasswordModel)

                        loginData.value = CommentsListingModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<CommentsListingModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun treadingContentApi(
            authorizationToke: String,
            type: String,
            page: Int,
            type_wise_content: String
    ): MutableLiveData<TreadingContentModel> {
        val loginData = MutableLiveData<TreadingContentModel>()
        contentApi?.treadingContent(
                authorizationToke, type, page, type_wise_content
        )?.enqueue(object : Callback<TreadingContentModel> {
            override fun onResponse(
                    call: Call<TreadingContentModel>,
                    response: Response<TreadingContentModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val TreadingContentModel: TreadingContentModel =
                                Common.getErrorModel(jsonObjectError, "TreadingContentModel") as TreadingContentModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = TreadingContentModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<TreadingContentModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun likeUnLikeApi(
            authorizationToke: String, id: Int
    ): MutableLiveData<CommonResponseModel> {
        val loginData = MutableLiveData<CommonResponseModel>()
        contentApi?.likeUnLikeApi(
                authorizationToke, id
        )?.enqueue(object : Callback<CommonResponseModel> {
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
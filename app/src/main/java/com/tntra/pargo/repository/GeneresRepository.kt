package com.tntra.pargo.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.RetrofitCommonClass
import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.collab_req.CollabRequestModel
import com.tntra.pargo.model.collabsession.CollabSessionModel
import com.tntra.pargo.model.followers.FollowerModel
import com.tntra.pargo.model.generes.GeneresListModel
import com.tntra.pargo.networkApi.login.CollabApi
import com.tntra.pargo.networkApi.login.GeneresApi
import com.walkins.aapkedoorstep.networkApi.login.LoginApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class GeneresRepository {

    var generesRepository: GeneresRepository? = null

    var collabApi: GeneresApi

    constructor() {
        collabApi = RetrofitCommonClass.createService(GeneresApi::class.java)
    }


    companion object

    fun getInstance(): GeneresRepository {
        if (generesRepository == null) {
            generesRepository = GeneresRepository()
        }
        return generesRepository as GeneresRepository
    }

    fun callApiGeneres(
            authorizationToke: String,context: Context
    ): MutableLiveData<GeneresListModel> {
        val loginData = MutableLiveData<GeneresListModel>()
        collabApi.generes(
                authorizationToke
        ).enqueue(object : Callback<GeneresListModel> {
            override fun onResponse(
                    call: Call<GeneresListModel>,
                    response: Response<GeneresListModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                    Log.e("TAGG", "onResponse: " + response.body())
//                    Toast.makeText(context,""+response.body(),Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)
                        Toast.makeText(context,""+jsonObjectError,Toast.LENGTH_SHORT).show()
                        val GeneresListModel: GeneresListModel =
                                Common.getErrorModel(jsonObjectError, "GeneresListModel") as GeneresListModel
                        //recoveryData.setValue(recoveryPasswordModel)

                        Log.e("TAGG", "onResponse: " + GeneresListModel.message)

                        loginData.value = GeneresListModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<GeneresListModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }


}
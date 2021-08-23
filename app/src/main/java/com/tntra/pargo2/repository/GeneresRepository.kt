package com.tntra.pargo2.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.common.RetrofitCommonClass
import com.tntra.pargo2.model.generes.GeneresListModel
import com.tntra.pargo2.networkApi.login.GeneresApi
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
package com.tntra.pargo.viewmodel.generes

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.collab_req.CollabRequestModel
import com.tntra.pargo.model.collabsession.CollabSessionModel
import com.tntra.pargo.model.followers.FollowerModel
import com.tntra.pargo.model.generes.GeneresListModel
import com.tntra.pargo.model.login_response.UserLoginModel
import com.tntra.pargo.repository.CollabSessionRepository
import com.tntra.pargo.repository.GeneresRepository
import com.tntra.pargo.repository.LoginRepository
import org.json.JSONObject

class GeneresViewModel : ViewModel() {

    private var generesRepository: GeneresRepository? = null
    var generesListModel: MutableLiveData<GeneresListModel>? = null

    fun callApiGeneresList(authorizationToke: String,context: Context) {
        generesRepository = GeneresRepository().getInstance()
        generesListModel = generesRepository?.callApiGeneres(authorizationToke,context)
    }

    fun getGeneresList(): LiveData<GeneresListModel>? {
        return generesListModel
    }
}
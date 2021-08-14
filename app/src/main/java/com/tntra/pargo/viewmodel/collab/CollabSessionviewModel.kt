package com.tntra.pargo.viewmodel.collab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.collab_req.CollabRequestModel
import com.tntra.pargo.model.collabroom.CollabRoomModel
import com.tntra.pargo.model.collabsession.CollabSessionModel
import com.tntra.pargo.model.followers.FollowerModel
import com.tntra.pargo.model.login.OtpModel
import com.tntra.pargo.model.login_response.UserLoginModel
import com.tntra.pargo.repository.CollabSessionRepository
import com.tntra.pargo.repository.LoginRepository
import org.json.JSONObject

class CollabSessionviewModel : ViewModel() {

    private var collabSessionRepository: CollabSessionRepository? = null
    var collabsessionModel: MutableLiveData<CollabSessionModel>? = null
    var commonResponseModel: MutableLiveData<CommonResponseModel>? = null
    var collabRequestModel: MutableLiveData<CollabRequestModel>? = null
    var followersModel: MutableLiveData<FollowerModel>? = null
    var collabRoomListModel: MutableLiveData<CollabRoomModel>? = null

    //    var uploadImageModel: MutableLiveData<UploadImageModel>? = null
    var otpModel: MutableLiveData<OtpModel>? = null
    var userLoginModel: MutableLiveData<UserLoginModel>? = null

    fun getCollabSession(): LiveData<CollabSessionModel>? {
        return collabsessionModel!!
    }

    fun getUserInfo(): LiveData<UserLoginModel>? {
        return userLoginModel
    }

    fun sendOtp(): LiveData<OtpModel>? {
        return otpModel
    }

    fun collabSessionApi(
            authorizationToke: String,
            jsonObject: JsonObject
    ) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        collabsessionModel = collabSessionRepository?.collabSessionApi(
                authorizationToke,
                jsonObject
        )
    }

    fun collabRequestApi(
            authorizationToke: String,
            jsonObject: JsonObject
    ) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        collabRequestModel = collabSessionRepository?.collabReqApi(
                authorizationToke,
                jsonObject
        )
    }

    fun getCollabReq(): LiveData<CollabRequestModel>? {
        return collabRequestModel!!
    }

    fun collabAcceptReject(
            authorizationToke: String,
            jsonObject: JsonObject,
            id: Int
    ) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        commonResponseModel = collabSessionRepository?.collabAcceptReject(
                authorizationToke,
                jsonObject, id
        )
    }

    fun getCollabAcceptReject(): LiveData<CommonResponseModel>? {
        return commonResponseModel!!
    }

    fun callApiFollowerslist(authorizationToke: String) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        followersModel = collabSessionRepository?.followersListApi(authorizationToke)
    }

    fun getFollowers(): LiveData<FollowerModel>? {
        return followersModel
    }

    fun callApiRoomList(authorizationToke: String) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        collabRoomListModel = collabSessionRepository?.callApiCollabRoom(authorizationToke)
    }

    fun getCollabRoom(): LiveData<CollabRoomModel>? {
        return collabRoomListModel
    }
}
package com.tntra.pargo2.viewmodel.collab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo2.model.CommonResponseModel
import com.tntra.pargo2.model.collabRoomList.CollabRoomListModel
import com.tntra.pargo2.model.collab_req.CollabRequestModel
import com.tntra.pargo2.model.collabroom.CollabRoomCreateModel

import com.tntra.pargo2.model.collabsession.CollabSessionModel
import com.tntra.pargo2.model.followers.FollowersListModel
import com.tntra.pargo2.model.login.OtpModel
import com.tntra.pargo2.model.login_response.UserLoginModel
import com.tntra.pargo2.model.notification.NotificationListModel
import com.tntra.pargo2.repository.CollabSessionRepository
import com.tntra.pargo2.repository.LoginRepository
import org.json.JSONObject

class CollabSessionviewModel : ViewModel() {

    private var collabSessionRepository: CollabSessionRepository? = null
    var collabsessionModel: MutableLiveData<CollabSessionModel>? = null
    var commonResponseModel: MutableLiveData<CommonResponseModel>? = null
    var collabRequestModel: MutableLiveData<CollabRequestModel>? = null
    var followersModel: MutableLiveData<FollowersListModel>? = null
    var collabRoomCreateModel: MutableLiveData<CollabRoomCreateModel>? = null
    var notificationListModel: MutableLiveData<NotificationListModel>? = null
    var collabRoomListModel: MutableLiveData<CollabRoomListModel>? = null
    var deleteNotification: MutableLiveData<CommonResponseModel>? = null

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

    fun callApiFollowerslist(authorizationToke: String, userId: Int,
                             page: Int,
                             type: String) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        followersModel = collabSessionRepository?.followersListApi(authorizationToke, userId, page, type)
    }

    fun getFollowers(): LiveData<FollowersListModel>? {
        return followersModel
    }

    fun callApiCollabRoomCreate(authorizationToke: String, jsonObject: JsonObject) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        collabRoomCreateModel = collabSessionRepository?.callApiCollabRoom(authorizationToke, jsonObject)
    }

    fun getCollabRoomCreated(): LiveData<CollabRoomCreateModel>? {
        return collabRoomCreateModel
    }

    fun callApiNotificationlist(authorizationToke: String) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        notificationListModel = collabSessionRepository?.callApiNotificationList(authorizationToke)
    }

    fun getNotificationList(): LiveData<NotificationListModel> {
        return notificationListModel!!
    }

    fun callApiCollabRoomList(authorizationToke: String) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        collabRoomListModel = collabSessionRepository?.callApiCollabRoomList(authorizationToke)
    }

    fun getCollabRoomList(): LiveData<CollabRoomListModel> {
        return collabRoomListModel!!
    }

    fun callApiDeleteNotification(authorizationToke: String, id: Int) {
        collabSessionRepository = CollabSessionRepository().getInstance()
        deleteNotification = collabSessionRepository?.callApiDeleteNoti(authorizationToke, id)
    }

    fun getDeleteNoti(): LiveData<CommonResponseModel> {
        return deleteNotification!!
    }
}
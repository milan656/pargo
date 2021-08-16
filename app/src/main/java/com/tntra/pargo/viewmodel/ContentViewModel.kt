package com.tntra.pargo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tntra.pargo.common.Common
import com.tntra.pargo.model.CommonResponseModel
import com.tntra.pargo.model.collabsession.CollabSessionModel
import com.tntra.pargo.model.comments.CommentListModel
import com.tntra.pargo.model.comments.list.CommentsListingModel
import com.tntra.pargo.model.content_list.ContentListModel
import com.tntra.pargo.model.content_list.show.ContentShowModel
import com.tntra.pargo.model.login_response.UserLoginModel
import com.tntra.pargo.model.treading_content.TreadingContentModel
import com.tntra.pargo.repository.CollabSessionRepository
import com.tntra.pargo.repository.ContentRepository

class ContentViewModel : ViewModel() {

    private var contentRepository: ContentRepository? = null
    var commonResponseModel: MutableLiveData<CommonResponseModel>? = null
    var contentListModel: MutableLiveData<ContentListModel>? = null
    var contentShowModel: MutableLiveData<ContentShowModel>? = null
    var commentListModel: MutableLiveData<CommentListModel>? = null
    var commentsListingModel: MutableLiveData<CommentsListingModel>? = null
    var treadingContentModel: MutableLiveData<TreadingContentModel>? = null


    fun contentListApi(
            authorizationToke: String,
    ) {
        contentRepository = ContentRepository().getInstance()
        contentListModel = contentRepository?.contentListApi(
                authorizationToke
        )
    }

    fun getContent(): LiveData<ContentListModel>? {
        return contentListModel
    }

    fun contentShowApi(
            authorizationToke: String,
            id: Int
    ) {
        contentRepository = ContentRepository().getInstance()
        contentShowModel = contentRepository?.contentShowApi(
                authorizationToke, id
        )
    }

    fun commentAdd(
            authorizationToke: String,
            jsonObject: JsonObject,
            id: Int
    ) {
        contentRepository = ContentRepository().getInstance()
        commentListModel = contentRepository?.commentAddApi(
                authorizationToke, jsonObject, id
        )
    }

    fun commentList(
            authorizationToke: String,
            id: Int
    ) {
        contentRepository = ContentRepository().getInstance()
        commentsListingModel = contentRepository?.commentListApi(
                authorizationToke, id
        )
    }
    fun commentShow(
            authorizationToke: String,
            id: Int
    ) {
        contentRepository = ContentRepository().getInstance()
        commentsListingModel = contentRepository?.commentListApi(
                authorizationToke, id
        )
    }

    fun getCommentsList(): LiveData<CommentsListingModel> {
        return commentsListingModel!!
    }

    fun getCommentAdd(): LiveData<CommentListModel> {
        return commentListModel!!
    }

    fun getContentShow(): LiveData<ContentShowModel>? {
        return contentShowModel
    }

    fun treadingContentApi(
            authorizationToke: String,
            type: String,
            page: Int,
            type_wise_content: String
    ) {
        contentRepository = ContentRepository().getInstance()
        treadingContentModel = contentRepository?.treadingContentApi(
                authorizationToke, type, page, type_wise_content
        )
    }

    fun uploadContentApi(
            authorizationToke: String,
            type: String,
            page: Int,
            type_wise_content: String
    ) {
        contentRepository = ContentRepository().getInstance()
        treadingContentModel = contentRepository?.treadingContentApi(
                authorizationToke, type, page, type_wise_content
        )
    }

    fun getTreadingContent(): LiveData<TreadingContentModel>? {
        return treadingContentModel
    }

    fun likeUnLikeApi(
            authorizationToke: String, id: Int
    ) {
        contentRepository = ContentRepository().getInstance()
        commonResponseModel = contentRepository?.likeUnLikeApi(
                authorizationToke, id
        )
    }

    fun getLikeUnLike(): LiveData<CommonResponseModel>? {
        return commonResponseModel
    }
}
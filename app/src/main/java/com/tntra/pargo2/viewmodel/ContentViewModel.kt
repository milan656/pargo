package com.tntra.pargo2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.model.CommonResponseModel
import com.tntra.pargo2.model.collabsession.CollabSessionModel
import com.tntra.pargo2.model.comments.CommentListModel
import com.tntra.pargo2.model.comments.list.CommentsListingModel
import com.tntra.pargo2.model.content_list.ContentListModel
import com.tntra.pargo2.model.content_list.show.ContentShowModel
import com.tntra.pargo2.model.login_response.UserLoginModel
import com.tntra.pargo2.model.treading_content.TreadingContentModel
import com.tntra.pargo2.repository.CollabSessionRepository
import com.tntra.pargo2.repository.ContentRepository

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

    fun contentShowTopContent(
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
            id: Int,
            page: Int
    ) {
        contentRepository = ContentRepository().getInstance()
        commentsListingModel = contentRepository?.commentListApi(
                authorizationToke, id, page
        )
    }

    fun commentShow(
            authorizationToke: String,
            id: Int, page: Int
    ) {
        contentRepository = ContentRepository().getInstance()
        commentsListingModel = contentRepository?.commentListApi(
                authorizationToke, id, page
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

    fun topLatestContentApi(
            authorizationToke: String,
            type: String,
            type_wise_content: String
    ) {
        contentRepository = ContentRepository().getInstance()
        treadingContentModel = contentRepository?.topLatestContentApi(
                authorizationToke, type, type_wise_content
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

    fun getTopLatestContent(): LiveData<TreadingContentModel>? {
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
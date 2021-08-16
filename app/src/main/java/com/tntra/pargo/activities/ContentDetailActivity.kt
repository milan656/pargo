package com.tntra.pargo.activities

import android.annotation.SuppressLint
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.gson.JsonObject
import com.tntra.pargo.R
import com.tntra.pargo.adapter.ContentMessageAdapter
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.comments.CommentListModel
import com.tntra.pargo.model.comments.list.Comment
import com.tntra.pargo.viewmodel.ContentViewModel
import java.lang.Exception

class ContentDetailActivity : AppCompatActivity(), onClickAdapter, Player.EventListener, View.OnClickListener {

    private var commentsRecycView: RecyclerView? = null
    private var commentsList: ArrayList<Comment>? = ArrayList()
    private var commentAdapter: ContentMessageAdapter? = null
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0
    private var mp4Url = "https://html5demos.com/assets/dizzy.mp4"
    private var dashUrl = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"

    private var exoplayerView: PlayerView? = null
    private var progressBar: ProgressBar? = null
    private var ivLike: ImageView? = null
    private var isLiked: Boolean = false

    private var video_link: String = ""
    private var id: String = ""
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    private var player: SimpleExoPlayer? = null
    private var prefManager: PrefManager? = null
    private var edtComment: EditText? = null
    private var btnSend: Button? = null
    private var ivUserImg: ImageView? = null
    private var tvUsername: TextView? = null
    private var tvdesc: TextView? = null
    private var tvTitleContent: TextView? = null

    private var accessKey: String = "AKIAYI7XP4UNI7KFDUFZ"
    private var secreteKey: String = "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"
    private var bucket_name: String = "pargo-back-end-devlopment"

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    var contentViewModel: ContentViewModel? = null

    var dataSourceFactory_: DataSource.Factory = DefaultHttpDataSourceFactory(
            "exoplayer-sample", null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            10800000,
            true)

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_detail)
        prefManager = PrefManager(this)
        contentViewModel = ViewModelProviders.of(this).get(ContentViewModel::class.java)
        initView()
    }

    private fun initView() {

        tvUsername = findViewById(R.id.tvUsername)
        tvTitleContent = findViewById(R.id.tvTitleContent)
        tvdesc = findViewById(R.id.tvdesc)
        ivUserImg = findViewById(R.id.ivUserImg)
        btnSend = findViewById(R.id.btnSend)
        edtComment = findViewById(R.id.edtComment)
        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)
        ivLike = findViewById(R.id.ivLike)
        progressBar = findViewById(R.id.progressBar)
        exoplayerView = findViewById(R.id.exoplayerView)
        commentsRecycView = findViewById(R.id.commentsRecycView)

        for (i in 0..9) {
//            commentsList?.add(CommentListModel("Hi", "http://placehold.it/120x120&text=image1", "", if (i % 2 == 0) 1 else 2))
        }
        commentAdapter = ContentMessageAdapter(commentsList!!, this, this)
        commentsRecycView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        commentsRecycView?.adapter = commentAdapter

        ivLike?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)
        btnSend?.setOnClickListener(this)

        if (intent != null) {
            if (intent.hasExtra("video_link")) {
                video_link = intent.getStringExtra("video_link")!!
                mp4Url = video_link
                dashUrl=video_link
            }
            if (intent.hasExtra("id")) {
                id = intent.getStringExtra("id")!!
            }
        }

        Log.e("TAGG", "initView: " + mp4Url)
        initializePlayer()

        listComment()

        showContent()
//        addComment()
    }

    private fun showContent() {
        contentViewModel?.contentShowApi(prefManager?.getAccessToken()!!, id.toInt())
        contentViewModel?.getContentShow()?.observe(this, Observer {
            if (it != null) {
                if (it.success) {

                    Log.e("TAG", "showContent: " + it.content.attributes)
                    try {
                        Glide.with(this).load(Common.url + it.content.attributes.user_profile_img_path).into(ivUserImg!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    tvUsername?.text = it.content.attributes.name
                    tvdesc?.text = it.content.attributes.body
                    tvTitleContent?.text = it.content.attributes.title
                }
            }
        })
    }

    private fun listComment() {
        Common.showLoader(this)
        contentViewModel?.commentList(prefManager?.getAccessToken()!!, id.toInt())
        contentViewModel?.getCommentsList()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Log.e("TAG", "addComment: " + it.message)

                    commentsList?.clear()
                    commentsList?.addAll(it.comments)
                    commentAdapter?.notifyDataSetChanged()

                } else {
                    try {
                        if (it.message != null) {
                            Log.e("TAG", "addComment: " + it.message)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    private fun addComment() {
//        {
//            "comment":{
//            "message": "My Third Comment"
//        }
//        }
        val mainObj = JsonObject()
        val commentObj = JsonObject()
        commentObj.addProperty("message", edtComment?.text?.toString())
        mainObj.add("comment", commentObj)

        Common.showLoader(this)
        contentViewModel?.commentAdd(prefManager?.getAccessToken()!!, mainObj, id.toInt())
        contentViewModel?.getCommentAdd()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Log.e("TAG", "addComment: " + it.message)
                    edtComment?.setText("")
                    listComment()
                } else {
                    try {
                        if (it.message != null) {
                            Log.e("TAG", "addComment: " + it.message)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onStart() {
        super.onStart()

    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        val urlList_ = listOf(mp4Url to "default", dashUrl to "dash")
        val randomUrl = urlList_.random()
        preparePlayer(randomUrl.first, randomUrl.first)
        exoplayerView?.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
        exoplayerView = null

    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {

        Log.e("TAG", "buildMediaSource: " + type)
        if (type == "dash") {
            return DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
        } else if (uri.lastPathSegment!!.contains("m3u8") ||
                uri.lastPathSegment!!.contains("mpd")) {
            val userAgent = "exoplayer-sample"
            return HlsMediaSource.Factory(dataSourceFactory_)
                    .createMediaSource(uri)
        } else {
            return ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
        }
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        Log.e("TAG", "preparePlayer: " + mediaSource)
        simpleExoplayer.prepare(mediaSource, false, false)
    }


    override fun onPlayerError(error: ExoPlaybackException) {
        Log.e("TAGG", "onPlayerError: " + error.message + " " + error.cause)
        initializePlayer()
        // handle error
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            progressBar?.visibility = View.VISIBLE
        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            progressBar?.visibility = View.INVISIBLE
        }
        Log.e("TAG", "onPlayerStateChanged: " + playWhenReady + " " + playbackState)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivLike -> {

                likeApi()
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnSend -> {
                addComment()
            }
        }
    }

    private fun likeApi() {
        Common.showLoader(this)
        contentViewModel?.likeUnLikeApi(prefManager?.getAccessToken()!!, id.toInt())
        contentViewModel?.getLikeUnLike()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Toast.makeText(this, "You " + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
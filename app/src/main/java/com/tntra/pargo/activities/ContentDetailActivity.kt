package com.tntra.pargo.activities

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tntra.pargo.model.comments.list.Comment
import com.tntra.pargo.viewmodel.ContentViewModel
import com.vanniktech.emoji.EmojiEditText
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.EmojiTextView
import java.util.*
import kotlin.collections.ArrayList


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
    private var ivLiked: ImageView? = null
    private var isLiked: Boolean = false

    private var video_link: String = ""
    private var id: String = ""
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    private var player: SimpleExoPlayer? = null
    private var prefManager: PrefManager? = null
    private var edtComment: EmojiEditText? = null
    private var btnSend: ImageView? = null
    private var ivUserImg: ImageView? = null
    private var tvUsername: TextView? = null
    private var tvdesc: TextView? = null
    private var tvTitleContent: TextView? = null
    private var tvViewCount: TextView? = null
    private var llparent: LinearLayout? = null

    private var timer: Timer? = null
    private var isDataFinishComment: Boolean = false

    var HI_BITRATE = 2097152
    var MI_BITRATE = 1048576
    var LO_BITRATE = 524288

    private val MAX_LINES_COLLAPSED = 1
    private val INITIAL_IS_COLLAPSED = true

    private val IDLE_ANIMATION_STATE = 1
    private val EXPANDING_ANIMATION_STATE = 2
    private val COLLAPSING_ANIMATION_STATE = 3
    private var mCurrentAnimationState = IDLE_ANIMATION_STATE
    private var isCollapsed = INITIAL_IS_COLLAPSED
    private var accessKey: String = "AKIAYI7XP4UNI7KFDUFZ"
    private var secreteKey: String = "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"
    private var bucket_name: String = "pargo-back-end-devlopment"
//    val bandwidthMeter = DefaultBandwidthMeter()
//    val trackSelector = DefaultTrackSelector()
//    val defaultTrackParam: DefaultTrackSelector.Parameters = trackSelector.buildUponParameters().build()

//    val defaultMaxInitialBitrate = Int.MAX_VALUE.toLong()
//    val defaultBandwidthMeter: DefaultBandwidthMeter = DefaultBandwidthMeter.Builder(this)
//            .setInitialBitrateEstimate(defaultMaxInitialBitrate)
//            .build()

    //    final val videoTrackSelectionFactory: AdaptiveTrackSelection.Factory  =AdaptiveTrackSelection.Factory(defaultBandwidthMeter,0,0,0f)
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    var contentViewModel: ContentViewModel? = null
    var ivEmoji: ImageView? = null

    var dataSourceFactory_: DataSource.Factory = DefaultHttpDataSourceFactory(
            "exoplayer-sample",
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            10800000,
            true)

    var emojiPopup: EmojiPopup? = null
    var rootView: RelativeLayout? = null
    var containNestedScoll: NestedScrollView? = null
    var commentPage = 1

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_detail)
        prefManager = PrefManager(this)
        contentViewModel = ViewModelProviders.of(this).get(ContentViewModel::class.java)
        initView()
    }

    private fun initView() {
        containNestedScoll = findViewById(R.id.containNestedScoll)
        edtComment = findViewById(R.id.etEmoji)
        rootView = findViewById(R.id.rootView)
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(edtComment!!)
        ivEmoji = findViewById(R.id.ivEmoji)
        llparent = findViewById(R.id.llparent)
        tvViewCount = findViewById(R.id.tvViewCount)
        tvUsername = findViewById(R.id.tvUsername)
        tvTitleContent = findViewById(R.id.tvTitleContent)
        tvdesc = findViewById(R.id.tvdesc)
        ivUserImg = findViewById(R.id.ivUserImg)
        btnSend = findViewById(R.id.btnSend)

        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)
        ivLike = findViewById(R.id.ivLike)
        ivLiked = findViewById(R.id.ivLiked)
        progressBar = findViewById(R.id.progressBar)
        exoplayerView = findViewById(R.id.exoplayerView)
        commentsRecycView = findViewById(R.id.commentsRecycView)

        tvdesc?.text = "lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description with description lorem ilpsum text with description lorem ilpsum text with description"

        commentAdapter = ContentMessageAdapter(commentsList!!, this, this)
        commentsRecycView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        commentsRecycView?.adapter = commentAdapter


        ivLike?.setOnClickListener(this)
        ivLiked?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)
        btnSend?.setOnClickListener(this)
        ivEmoji?.setOnClickListener(this)

        if (intent != null) {
            if (intent.hasExtra("video_link")) {
                video_link = intent.getStringExtra("video_link")!!
                mp4Url = video_link
                dashUrl = video_link
            }
            if (intent.hasExtra("id")) {
                id = intent.getStringExtra("id")!!
            }
        }

        tvdesc?.setOnClickListener {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        isActivityTransitionRunning
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }) {
                llparent?.setLayoutTransition(llparent?.getLayoutTransition())
            }
            if (isCollapsed) {
                mCurrentAnimationState = EXPANDING_ANIMATION_STATE
                tvdesc?.setMaxLines(Int.MAX_VALUE)
            } else {
                mCurrentAnimationState = COLLAPSING_ANIMATION_STATE
                tvdesc?.setMaxLines(MAX_LINES_COLLAPSED)
                tvdesc?.post(Runnable { tvdesc?.setMaxLines(Int.MAX_VALUE) })
            }
            isCollapsed = !isCollapsed
        }

        if (isCollapsed) {
            tvdesc?.setMaxLines(MAX_LINES_COLLAPSED)
        } else {
//            tvdesc?.setMaxLines(Int.MAX_VALUE)
            mCurrentAnimationState = COLLAPSING_ANIMATION_STATE
            tvdesc?.setMaxLines(MAX_LINES_COLLAPSED)
            tvdesc?.post(Runnable { //workaround:prevent text trimming at start collapsing
                tvdesc?.setMaxLines(Int.MAX_VALUE)
            })
        }

        applyLayoutTransition();
        Log.e("TAGG", "initView: " + mp4Url)
        initializePlayer()

        listComment(true)

        showContent()

//        timer = Timer()
//        timer?.schedule(
//                object : TimerTask() {
//                    override fun run() {
//                        runOnUiThread {
//                            showContentUpdates()
//                        }
//                    }
//                }, 60 * 1000
//        )
//        addComment()

        containNestedScoll?.viewTreeObserver
                ?.addOnScrollChangedListener {
                    if (containNestedScoll?.getChildAt(0)?.bottom!! <= containNestedScoll?.height!! + containNestedScoll?.scrollY!!) {
                        //scroll view is at bottom
                        Log.e("TAG", "initView: at bottom")
                        if (!isDataFinishComment) {
                            commentPage += 1
                            listComment(false)
                        }
                    }
                }

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
                    if (it.content.attributes.total_visits != 0 ||
                            it.content.attributes.total_visits == 1) {
                        tvViewCount?.text = "" + it.content.attributes.total_visits + " " + "View"
                    } else {
                        tvViewCount?.text = "" + it.content.attributes.total_visits + " " + "Views"
                    }

                    if (it.content.attributes.liked) {
                        ivLiked?.visibility = View.VISIBLE
                        ivLike?.visibility = View.GONE
                    } else {
                        ivLiked?.visibility = View.GONE
                        ivLike?.visibility = View.VISIBLE
                    }

//                    tvdesc?.text = "lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with"

                    updateWithNewText(tvdesc?.text?.toString()!!)
//                    addReadMore("lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with description lorem ilpsum text with ", tvdesc!!)
                }
            }
        })
    }

    private fun showContentUpdates() {
        contentViewModel?.contentShowApi(prefManager?.getAccessToken()!!, id.toInt())
        contentViewModel?.getContentShow()?.observe(this, Observer {
            if (it != null) {
                if (it.success) {

                    if (it.content.attributes.total_visits != 0 ||
                            it.content.attributes.total_visits == 1) {
                        tvViewCount?.text = "" + it.content.attributes.total_visits + " " + "View"
                    } else {
                        tvViewCount?.text = "" + it.content.attributes.total_visits + " " + "Views"

                    }
                }
            }
        })
    }

    private fun listComment(isClear: Boolean) {
//        Common.showLoader(this)
        contentViewModel?.commentList(prefManager?.getAccessToken()!!, id.toInt(), commentPage)
        contentViewModel?.getCommentsList()?.observe(this, Observer {
//            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Log.e("TAG", "addComment: " + it.message)

                    if (isClear) {
                        commentsList?.clear()
                    }
                    commentsList?.addAll(it.comments)
                    commentAdapter?.notifyDataSetChanged()

                    if (it.comments.size == 0) {
                        isDataFinishComment = true
                    }

                    Log.e("TAG", "listComment: scroll" )
                    containNestedScoll?.fullScroll(View.FOCUS_DOWN)
                    commentsRecycView?.smoothScrollToPosition(commentsList?.size!!)
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
                    Common.hideKeyboard(this)
                    commentPage = 1
                    listComment(true)
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


//        val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

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

        // Produces DataSource instances through which media data is loaded.
        // Produces DataSource instances through which media data is loaded.

        // This is the MediaSource representing the media to be played.
        // This is the MediaSource representing the media to be played.
//        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(mUri)
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
            R.id.ivLike, R.id.ivLiked -> {
                likeApi()
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnSend -> {
                if (edtComment?.text?.toString()?.isEmpty()!!) {
                    Toast.makeText(this, "Please add comment", Toast.LENGTH_SHORT).show()
                } else {
                    addComment()
                }
            }
            R.id.ivEmoji -> {
                try {
                    emojiPopup?.toggle()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun likeApi() {
//        Common.showLoader(this)
        contentViewModel?.likeUnLikeApi(prefManager?.getAccessToken()!!, id.toInt())
        contentViewModel?.getLikeUnLike()?.observe(this, Observer {
//            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    if (it.message.equals("Liked", ignoreCase = true)) {
                        ivLiked?.visibility = View.VISIBLE
                        ivLike?.visibility = View.GONE
                    } else {
                        ivLiked?.visibility = View.GONE
                        ivLike?.visibility = View.VISIBLE
                    }
                    Toast.makeText(this, "You " + it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (timer != null) {
                timer?.cancel()
                timer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun updateWithNewText(text: String) {
        tvdesc?.setText(text)
        tvdesc?.getViewTreeObserver()?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (isTextUnlimited()) {
                    if (canBeCollapsed()) {
                        tvdesc?.setClickable(false)
                        tvdesc?.setEllipsize(null)
                    } else {
                        tvdesc?.setClickable(true)
                        tvdesc?.setEllipsize(TextUtils.TruncateAt.END)
                    }
                } else {
                    if (isTrimmedWithLimitLines()) {
                        tvdesc?.setClickable(false)
                        tvdesc?.setEllipsize(null)
                    } else {
                        tvdesc?.setClickable(true)
                        tvdesc?.setEllipsize(TextUtils.TruncateAt.END)
                    }
                }
                tvdesc?.getViewTreeObserver()?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun isTextUnlimited(): Boolean {
        return tvdesc?.getMaxLines()!! === Int.MAX_VALUE
    }

    private fun canBeCollapsed(): Boolean {
        return tvdesc?.getLineCount()!! <= MAX_LINES_COLLAPSED
    }

    private fun isTrimmedWithLimitLines(): Boolean {
        return tvdesc?.getLineCount()!! <= tvdesc?.getMaxLines()!!
    }

    private fun applyLayoutTransition() {
        val transition = LayoutTransition()
        transition.setDuration(300)
        transition.enableTransitionType(LayoutTransition.CHANGING)
        llparent?.setLayoutTransition(transition)
        transition.addTransitionListener(object : LayoutTransition.TransitionListener {
            override fun startTransition(transition: LayoutTransition,
                                         container: ViewGroup, view: View, transitionType: Int) {
                //todo
            }

            override fun endTransition(transition: LayoutTransition,
                                       container: ViewGroup, view: View, transitionType: Int) {
                if (COLLAPSING_ANIMATION_STATE === mCurrentAnimationState) {
                    tvdesc?.setMaxLines(MAX_LINES_COLLAPSED)
                }
                mCurrentAnimationState = IDLE_ANIMATION_STATE
            }
        })
    }

    private fun isIdle(): Boolean {
        return mCurrentAnimationState === IDLE_ANIMATION_STATE
    }

    private fun isRunning(): Boolean {
        return !isIdle()
    }


}
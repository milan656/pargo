package com.tntra.pargo.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tntra.pargo.R
import com.tntra.pargo.adapter.ContentMessageAdapter
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.comments.CommentListModel

class ContentDetailActivity : AppCompatActivity(), onClickAdapter, Player.EventListener, View.OnClickListener {

    private var commentsRecycView: RecyclerView? = null
    private var commentsList: ArrayList<CommentListModel>? = ArrayList()
    private var commentAdapter: ContentMessageAdapter? = null
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0
    private var mp4Url = "https://html5demos.com/assets/dizzy.mp4"
    private val dashUrl = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"

    private var exoplayerView: PlayerView? = null
    private var progressBar: ProgressBar? = null
    private var ivLike: ImageView? = null
    private var isLiked: Boolean = false

    private var video_link: String = ""
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null

    private var player: SimpleExoPlayer? = null

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    var dataSourceFactory_: DataSource.Factory = DefaultHttpDataSourceFactory(
            "exoplayer-sample", null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            1800000,
            true)

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_detail)

        initView()
    }

    private fun initView() {

        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)
        ivLike = findViewById(R.id.ivLike)
        progressBar = findViewById(R.id.progressBar)
        exoplayerView = findViewById(R.id.exoplayerView)
        commentsRecycView = findViewById(R.id.commentsRecycView)

        for (i in 0..9) {
            commentsList?.add(CommentListModel("Hi", "http://placehold.it/120x120&text=image1", "", if (i % 2 == 0) 1 else 2))
        }
        commentAdapter = ContentMessageAdapter(commentsList!!, this, this)
        commentsRecycView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        commentsRecycView?.adapter = commentAdapter

        ivLike?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)

        if (intent != null) {
            if (intent.hasExtra("video_link")) {
                video_link = intent.getStringExtra("video_link")!!
                mp4Url = video_link
            }
        }

        Log.e("TAGG", "initView: "+mp4Url )
        initializePlayer()
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

    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {


        if (type == "dash") {
            return DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
        } else if (uri.lastPathSegment!!.contains("m3u8")) {
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
        simpleExoplayer.prepare(mediaSource)
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        Log.e("TAGG", "onPlayerError: " + error.message + " " + error.cause)
        // handle error
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            progressBar?.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            progressBar?.visibility = View.INVISIBLE
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivLike -> {

            }
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

}
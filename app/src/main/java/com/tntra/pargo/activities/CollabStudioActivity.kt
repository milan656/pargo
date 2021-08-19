package com.tntra.pargo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.tntra.pargo.R

class CollabStudioActivity : AppCompatActivity(), View.OnClickListener {

    private var tvPodcast: TextView? = null
    private var tvLiveStream: TextView? = null
    private var tvJugalBandhi: TextView? = null
    private var tvMusicComposition: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collab_studio)

        initView()
    }

    private fun initView() {
        tvPodcast = findViewById(R.id.tvPodcast)
        tvJugalBandhi = findViewById(R.id.tvJugalBandhi)
        tvMusicComposition = findViewById(R.id.tvMusicComposition)
        tvLiveStream = findViewById(R.id.tvLiveStream)

        tvPodcast?.setOnClickListener(this)
        tvJugalBandhi?.setOnClickListener(this)
        tvLiveStream?.setOnClickListener(this)
        tvMusicComposition?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        val intent = Intent(this, CollabStudioDetailActivity::class.java)
        when (id) {
            R.id.tvPodcast -> {
                intent.putExtra("collabType", "podcast")
                startActivity(intent)
            }
            R.id.tvLiveStream -> {
                intent.putExtra("collabType", "live_stream")
                startActivity(intent)
            }
            R.id.tvJugalBandhi -> {
                intent.putExtra("collabType", "jugal_bandi")
                startActivity(intent)
            }
            R.id.tvMusicComposition -> {
                intent.putExtra("collabType", "music_composition")
                startActivity(intent)
            }

        }
    }

}
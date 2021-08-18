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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collab_studio)

        initView()
    }

    private fun initView() {
        tvPodcast = findViewById(R.id.tvPodcast)
        tvJugalBandhi = findViewById(R.id.tvJugalBandhi)
        tvLiveStream = findViewById(R.id.tvLiveStream)
        tvPodcast?.setOnClickListener(this)
        tvJugalBandhi?.setOnClickListener(this)
        tvLiveStream?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.tvPodcast -> {
                val intent = Intent(this, CollabStudioDetailActivity::class.java)
                intent.putExtra("collabType", "podcast")
                startActivity(intent)
            }
            R.id.tvLiveStream -> {
                val intent = Intent(this, CollabStudioDetailActivity::class.java)
                intent.putExtra("collabType", "livestream")
                startActivity(intent)
            }
            R.id.tvJugalBandhi -> {
                val intent = Intent(this, CollabStudioDetailActivity::class.java)
                intent.putExtra("collabType", "jugalbandhi")
                startActivity(intent)
            }
        }
    }

}
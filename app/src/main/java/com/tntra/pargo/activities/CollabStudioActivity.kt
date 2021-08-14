package com.tntra.pargo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.tntra.pargo.R

class CollabStudioActivity : AppCompatActivity(), View.OnClickListener {

    private var tvPodcast: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collab_studio)

        initView()
    }

    private fun initView() {
        tvPodcast = findViewById(R.id.tvPodcast)
        tvPodcast?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.tvPodcast -> {
                val intent=Intent(this,CollabStudioDetailActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
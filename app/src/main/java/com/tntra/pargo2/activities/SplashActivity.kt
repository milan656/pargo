package com.tntra.pargo2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.tntra.pargo2.R
import com.tntra.pargo2.activity.HomeActivity
import com.tntra.pargo2.activity.LoginActivity
import com.tntra.pargo2.common.PrefManager

class SplashActivity : AppCompatActivity() {
    private var prefManager: PrefManager? = null
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startWorking()
    }

    private fun startWorking() {
        prefManager = PrefManager(this@SplashActivity)

        waitForThreeSec()
    }

    private fun waitForThreeSec() {
        Handler().postDelayed({
            navigateScreen()
        }, SPLASH_TIME_OUT)
    }

    private fun navigateScreen() {
        if (prefManager?.getIsLogin()!!) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
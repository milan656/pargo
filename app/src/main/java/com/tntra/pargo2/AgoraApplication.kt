package com.tntra.pargo2

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.tntra.pargo2.common.PrefManager
import com.tntra.pargo2.rtc.AgoraEventHandler
import com.tntra.pargo2.rtc.EngineConfig
import com.tntra.pargo2.rtc.EventHandler
import com.tntra.pargo2.stats.StatsManager
import com.tntra.pargo2.utils.FileUtil
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import io.agora.rtc.RtcEngine


class AgoraApplication : MultiDexApplication() {
    private var mRtcEngine: RtcEngine? = null
    private val mGlobalConfig = EngineConfig()
    private val mHandler = AgoraEventHandler()
    private val mStatsManager = StatsManager()

    init {
        instance = this
    }

    companion object {
        private var instance: AgoraApplication? = null

        //        var firebaseCrashlytics: FirebaseCrashlytics? = null
        fun applicationContext(): Context {
            return instance?.applicationContext!!
        }
    }
    override fun onCreate() {
        super.onCreate()
        try {
            mRtcEngine = RtcEngine.create(applicationContext, getString(R.string.private_app_id), mHandler)
            mRtcEngine?.setLogFile(FileUtil.initializeLogFile(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            MultiDex.install(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        EmojiManager.install(GoogleEmojiProvider())
        initConfig()


    }

    private fun initConfig() {
        val pref = PrefManager(instance!!).getPreferences(applicationContext)
        mGlobalConfig.videoDimenIndex = pref?.getInt(
                Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX)!!
        val showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, false)
        mGlobalConfig.setIfShowVideoStats(showStats)
        mStatsManager.enableStats(showStats)
        mGlobalConfig.mirrorLocalIndex = pref.getInt(Constants.PREF_MIRROR_LOCAL, 0)
        mGlobalConfig.mirrorRemoteIndex = pref.getInt(Constants.PREF_MIRROR_REMOTE, 0)
        mGlobalConfig.mirrorEncodeIndex = pref.getInt(Constants.PREF_MIRROR_ENCODE, 0)
    }

    fun engineConfig(): EngineConfig {
        return mGlobalConfig
    }

    fun rtcEngine(): RtcEngine? {
        return mRtcEngine
    }

    fun statsManager(): StatsManager {
        return mStatsManager
    }

    fun registerEventHandler(handler: EventHandler?) {
        mHandler.addHandler(handler)
    }

    fun removeEventHandler(handler: EventHandler?) {
        mHandler.removeHandler(handler)
    }

    override fun onTerminate() {
        super.onTerminate()
        RtcEngine.destroy()
    }


}
package com.tntra.pargo2.common

import android.annotation.SuppressLint
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.tntra.pargo2.AgoraApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitCommonClass {

    companion object CommonRetrofit {
        @SuppressLint("StaticFieldLeak")
        val context = AgoraApplication.applicationContext()

        val manager = context.packageManager

        private fun getDeviceName(): String? {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                model.capitalize(Locale.getDefault())
            } else manufacturer.capitalize(Locale.getDefault()) + " " + model
        }

        val info = manager.getPackageInfo(context.packageName, 0)
        var versionCode = PackageInfoCompat.getLongVersionCode(info).toInt()

        private fun getOsName(): String? {

            var androidOS = Build.VERSION.RELEASE
            val fields = Build.VERSION_CODES::class.java.fields
            for (field in fields) {
                androidOS = field.name
            }
            return androidOS
        }

//        var tlsSocketFactory = TLSSocketFactory()

        private val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
//                .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager)

                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request =
                                chain.request().newBuilder()
//                                        .addHeader("app", "")
//                                        .addHeader("device_type", "android")
//                                        .addHeader("apk_version", "" + versionCode)
//                                        .addHeader("mobile_model", "" + getDeviceName())
//                                        .addHeader("mobile_os_version", "" + getOsName())

                                        .addHeader("Content-Type", "application/json")
                                        .addHeader("Accept", "application/json").build()
                        return chain.proceed(request)

                    }

                })
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

        private val retrofit = Retrofit.Builder()
                .baseUrl(Common.url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        fun <S> createService(serviceClass: Class<S>): S {
            return retrofit.create(serviceClass)
        }
    }
}
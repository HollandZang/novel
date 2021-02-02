package com.holland.novel.http

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * 通用网络请求类
 */
object BaseClient {
    val INSTANCE = OkHttpClient.Builder()
        .callTimeout(2,TimeUnit.SECONDS)
        .build()

    fun baseRequestAsync(context: Context, request: Request, onResponse: Consumer<Response>?, onFailure: Consumer<IOException>?) {
        INSTANCE.newCall(request).enqueue(
                object : BaseCallback(context, request.url.encodedPath) {
                    override fun onResponse(call: Call, response: Response) {
                        super.onResponse(call, response)
                        onResponse?.accept(response)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        super.onFailure(call, e)
                        onFailure?.accept(e)
                    }
                }
        )
    }

    fun toast(context: Context, msg: String) {
        Handler(Looper.getMainLooper()).run {
            post {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
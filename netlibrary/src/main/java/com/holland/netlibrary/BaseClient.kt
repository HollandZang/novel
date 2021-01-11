package com.holland.netlibrary

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.function.Consumer

/**
 * 通用网络请求类
 */
object BaseClient {
    val INSTANCE = OkHttpClient()

    fun baseRequestAsync(context: Context, request: Request, success: Consumer<Response?>?) {
        INSTANCE.newCall(request).enqueue(
                object : BaseCallback(context, request.url.encodedPath) {
                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        Log.d(this.javaClass.name, "请求成功: ${request.url.encodedPath}")
                        success?.accept(response)
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
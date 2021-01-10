package com.holland.netlibrary

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import okhttp3.*
import java.io.IOException
import java.util.function.Consumer

/**
 * 通用网络请求类
 */
object BaseClient {
    val INSTANCE = OkHttpClient()

    fun baseRequestAsync(context: Context, request: Request, success: Consumer<Response?>?) {
        INSTANCE.newCall(request).enqueue(
            baseCallBack(context, request.url.encodedPath, success)
        )
    }

    fun baseCallBack(context: Context, url: String?, success: Consumer<Response?>?): Callback {
        return object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(this.javaClass.name, "请求失败: $url")
                e.printStackTrace()
                toast(context, "网络错误")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                Log.d(this.javaClass.name, "请求成功: $url")
                success?.accept(response)
            }
        }
    }

    fun toast(context: Context, msg: String) {
        Handler(Looper.getMainLooper()).run {
            post {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package com.holland.netlibrary

import android.content.Context
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import java.io.IOException

abstract class BaseCallback(private val context: Context, private val url: String?) : Callback {
    override fun onFailure(call: Call, e: IOException) {
        Log.d(this.javaClass.name, "请求失败: $url")
        e.printStackTrace()
        BaseClient.toast(context, "网络错误")
    }
}
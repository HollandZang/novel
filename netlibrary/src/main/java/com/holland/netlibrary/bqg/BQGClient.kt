package com.holland.netlibrary.bqg

import android.content.Context
import android.util.Log
import com.holland.netlibrary.BaseCallback
import com.holland.netlibrary.BaseClient.INSTANCE
import com.holland.netlibrary.BaseClient.toast
import com.holland.netlibrary.bqg.bean.Chapter
import com.holland.netlibrary.bqg.bean.Novel
import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.function.Consumer

/**
 * 笔趣阁爬虫类
 */
object BQGClient {

    /**
     * 搜索小说
     */
    fun searchNovel(context: Context, novelName: String?, success: Consumer<List<Novel>>?) {
        INSTANCE.newCall(
                Request.Builder()
                        .url(
                                Url.SEARCH_NOVEL.url.toHttpUrl()
                                        .newBuilder()
                                        .addQueryParameter("q", novelName)
                                        .build()
                        )
                        .build()
        ).enqueue(object : BaseCallback(context, Url.SEARCH_NOVEL.url) {
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                Log.d(this.javaClass.name, "请求成功: ${Url.SEARCH_NOVEL.url}")
                if (null == response.body) {
                    toast(context, "找不到更多了!")
                    return
                }
                val string = response.body!!.string()
                val listNovels = Novel.listNovels(string)
                if (listNovels.isEmpty()) {
                    toast(context, "找不到更多了!")
                    return
                }
                success?.accept(listNovels)
            }
        })
    }

    /**
     * 拉取章节列表
     */
    // TODO: 2021/1/11 存数据库, 然后通过数据库获取分页列表
    fun listChapter(context: Context, url: String?, success: Consumer<List<Chapter>>?) {
        INSTANCE.newCall(Request.Builder().url(url!!).build())
                .enqueue(object : BaseCallback(context, url) {
                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        Log.d(this.javaClass.name, "请求成功: ${Url.SEARCH_NOVEL.url}")
                        if (null == response.body) {
                            toast(context, "章节列表为空!")
                            return
                        }
                        val string = response.body!!.string()
                        val listChapters = Chapter.listChapters(string)
                        if (listChapters.isEmpty()) {
                            toast(context, "章节列表为空!")
                            return
                        }
                        success?.accept(listChapters)
                    }
                })
    }
}

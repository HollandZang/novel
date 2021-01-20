package com.holland.novel.http.bqg

import android.content.Context
import com.holland.novel.domain.Chapter
import com.holland.novel.domain.Content
import com.holland.novel.domain.Novel
import com.holland.novel.enums.Url
import com.holland.novel.http.BaseCallback
import com.holland.novel.http.BaseClient.INSTANCE
import com.holland.novel.http.BaseClient.toast
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
    fun searchNovel(context: Context, novelName: String?, onResponse: Consumer<List<Novel>>?, onFailure: Consumer<IOException>?) {
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
                super.onResponse(call, response)
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
                onResponse?.accept(listNovels)
            }

            override fun onFailure(call: Call, e: IOException) {
                super.onFailure(call, e)
                onFailure?.accept(e)
            }
        })
    }

    /**
     * 拉取章节列表
     */
    // TODO: 2021/1/11 存数据库, 然后通过数据库获取分页列表
    fun listChapter(context: Context, url: String?, onResponse: Consumer<List<Chapter>>?, onFailure: Consumer<IOException>?) {
        INSTANCE.newCall(Request.Builder().url(url!!).build())
                .enqueue(object : BaseCallback(context, url) {
                    override fun onResponse(call: Call, response: Response) {
                        super.onResponse(call, response)
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
                        onResponse?.accept(listChapters)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        super.onFailure(call, e)
                        onFailure?.accept(e)
                    }
                })
    }

    /**
     * 拉取正文
     */
    fun textPart(context: Context, url: String?, onResponse: Consumer<String>?, onFailure: Consumer<IOException>?) {
        INSTANCE.newCall(Request.Builder().get().url(url!!).build())
                .enqueue(object : BaseCallback(context, url) {
                    override fun onResponse(call: Call, response: Response) {
                        super.onResponse(call, response)
                        if (null == response.body) {
                            toast(context, "正文为空!")
                            return
                        }
                        val string = response.body!!.string()
                        val content = Content.getContent(string)
                        if (content.isEmpty()) {
                            toast(context, "正文为空!")
                            return
                        }
                        onResponse?.accept(content)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        super.onFailure(call, e)
                        onFailure?.accept(e)
                    }
                })
    }
}

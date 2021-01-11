package com.holland.netlibrary.bqg.bean

import com.holland.netlibrary.bqg.Url
import org.jsoup.Jsoup

class Chapter(
        val name: String,
        val url: String) {

    companion object {
        fun listChapters(responseBody: String): List<Chapter> {
            return Jsoup.parse(responseBody)
                    .body()
                    .getElementById("wrapper")
                    .getElementsByClass("box_con")[1]
                    .getElementById("list")
                    .child(0)
                    .children()
                    .map {
                        Chapter(
                                it.child(0).text(),
                                Url.BASE.url + it.child(0).attributes().get("href")
                        )
                    }
        }
    }
}
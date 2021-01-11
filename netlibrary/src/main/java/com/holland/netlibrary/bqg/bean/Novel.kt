package com.holland.netlibrary.bqg.bean

import org.jsoup.Jsoup

class Novel(
        val name: String,
        val author: String,
        val latestChapter: String,
        /**
         * yy--MM--dd
         */
        val latestTime: String,
        /**
         * unit: kb
         */
        val size: String,
        val finished: Boolean,
        val homeUrl: String) {

    companion object {
        fun listNovels(responseBody: String): List<Novel> {
            return Jsoup.parse(responseBody)
                    .body()
                    .getElementById("main")
                    .getElementById("content")
                    .getElementById("checkform")
                    .getElementsByClass("grid").first()
                    .allElements.first()
                    .allElements.select("#nr")
                    .map {
                        Novel(
                                it.child(0).child(0).text(),
                                it.child(2).text(),
                                it.child(1).child(0).text(),
                                it.child(4).text(),
                                it.child(3).text(),
                                "完本" == it.child(5).text(),
                                it.child(0).child(0).attributes().get("href")
                        )
                    }
        }
    }
}

package com.holland.netlibrary.bqg

enum class Url(val url: String) {
    /**
     * 基本路径
     */
    BASE("http://www.xinbqg.com"),

    /**
     * method:  GET
     * params:  s = novel name
     * return:  html
     */
    SEARCH_NOVEL("http://www.xinbqg.com/modules/article/search.php"),
}
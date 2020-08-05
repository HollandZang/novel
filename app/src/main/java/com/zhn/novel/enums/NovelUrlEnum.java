package com.example.app.enums;

public enum NovelUrlEnum {
    /**
     * method:  GET
     * params:  s = novel name
     * return:  html
     */
    SEARCH("http://www.xinbqg.com/modules/article/search.php"),;

    private final String url;

    NovelUrlEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

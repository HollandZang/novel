package com.holland.novel.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.stream.Collectors;

public class Content {
    private final static String ENTER = "<br />";
    private final static String SPACE = "&nbsp;";
    private final static String QUADRUPLE_SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private final static String[] ADV = new String[]{"天&nbsp;&nbsp;籁小说",
            "本站重要通知:请使用本站的免费小说APP无广告、破防盗版、更新快会员同步书架,请关注微信公A号&nbsp;appxsyd&nbsp;(按住三秒复制)&nbsp;下载免费阅读器!!",
            "公告：免费小说app安卓，支持安卓，苹果，告别一切广告,请关注微信公众号进入下载安装&nbsp;zuopingshuji&nbsp;按住三秒复制!!",
            "公告：笔趣阁免费APP上线了，支持安卓,苹果。请关注微信公众号进入下载安装&nbsp:wanbenheji&nbsp:(按住三秒复制)!!!",
            "X23US.COM更新最快"};

    /**
     * 从xml格式中获取正文内容
     */
    public static String getContent(String responseBody) {
        Element element = Jsoup.parse(responseBody)
                .body()
                .getElementById("wrapper")
                .getElementsByClass("content_read").first()
                .getElementsByClass("box_con").first()
                .getElementById("content");

//        element.select("br").prepend("\\n");
//        element.select("p").prepend("\\n\\n");

        return element.textNodes().stream()
                .map(textNode -> {
                    String s1 = textNode.toString();
                    for (String adv : ADV) {
                        s1 = s1.replace(adv, "");
                    }
                    return s1;
                })
                .collect(Collectors.joining())
//                .replace(ENTER, "\n")
                .replace(QUADRUPLE_SPACE, "\n\n        ")
                .replace(SPACE, "  ");
    }
}

package com.holland.novel.domain;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Novel {
    private String name;
    private String author;
    private String latestChapter;

    /**
     * yy--MM--dd
     */
    private String latestTime;

    /**
     * unit: kb
     */
    private String size;
    private Boolean finished;

    private String homeUrl;

    public static List<Novel> listNovels(String responseBody) {
        Elements novel = Jsoup.parse(responseBody)
                .body()
                .getElementById("main")
                .getElementById("content")
                .getElementById("checkform")
                .getElementsByClass("grid").first()
                .getAllElements().first()
                .getAllElements().select("#nr");
        return novel.stream().map(
                n -> new Novel()
                        .setName(n.child(0).child(0).text())
                        .setAuthor(n.child(2).text())
                        .setLatestChapter(n.child(1).child(0).text())
                        .setLatestTime(n.child(4).text())
                        .setSize(n.child(3).text())
                        .setFinished("完本".equals(n.child(5).text()))
                        .setHomeUrl(n.child(0).child(0).attributes().get("href")))
                .collect(Collectors.toList());
    }
}

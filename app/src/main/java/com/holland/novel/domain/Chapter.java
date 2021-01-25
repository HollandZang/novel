package com.holland.novel.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.holland.novel.enums.Url;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class Chapter implements Serializable {

    /**
     * 章节序列
     */
    @PrimaryKey
    private int seq;

    //    @ForeignKey(entity = Novel.class)
    private String novelName;

    private String name;
    private String url;

    public static List<Chapter> listChapters(String responseBody) {
        Elements chapters = Jsoup.parse(responseBody)
                .body()
                .getElementById("wrapper")
                .getElementsByClass("box_con").get(1)
                .getElementById("list")
                .child(0)
                .children();
        return chapters.stream().map(c ->
                new Chapter()
                        .setName(c.child(0).text())
                        .setUrl(Url.BASE.getUrl() + c.child(0).attributes().get("href"))
        ).collect(Collectors.toList());
    }
}

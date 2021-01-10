package com.holland.novel.domain;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Chapter implements Serializable {
    private String name;
    private String url;

    public static Function<String, List<Chapter>> fromBQG = s -> {
        Elements chapters = Jsoup.parse(s)
                .body()
                .getElementById("wrapper")
                .getElementsByClass("box_con").get(1)
                .getElementById("list")
                .child(0)
                .children();
        return chapters.stream().map(c ->
                new Chapter()
                        .setName(c.child(0).text())
                        .setUrl("http://www.xinbqg.com" + c.child(0).attributes().get("href"))
        ).collect(Collectors.toList());
    };
}

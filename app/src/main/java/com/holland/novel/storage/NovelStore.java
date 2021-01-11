package com.holland.novel.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.holland.novel.domain.Chapter;

import java.util.List;

public class NovelStore {

    private static final String FILE_NAME = "novel_store";

    private static final String INDEX = "index";
    private static final String CHAPTERS = "chapters";
    private static final String NOVEL_NAME = "novel_name";

    public static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void firstStore(Context context, String novelName, int index, List<Chapter> chapters) {
        getInstance(context).edit()
                .putString(NOVEL_NAME, novelName)
                .putInt(INDEX, index)
                .putString(CHAPTERS, JSON.toJSONString(chapters))
                .apply();
    }

    public static void firstStore1(Context context, String novelName, int index, List<com.holland.netlibrary.bqg.bean.Chapter> chapters) {
        getInstance(context).edit()
                .putString(NOVEL_NAME, novelName)
                .putInt(INDEX, index)
                .putString(CHAPTERS, JSON.toJSONString(chapters))
                .apply();
    }

    public static void nextStore(Context context, int index) {
        getInstance(context).edit()
                .putInt(INDEX, index)
                .apply();
    }

    public static String getNovelName(Context context) {
        return getInstance(context).getString(NOVEL_NAME, null);
    }

    public static int getIndex(Context context) {
        return getInstance(context).getInt(INDEX, 0);
    }

    public static List<Chapter> getChapters(Context context) {
        String s = getInstance(context).getString(CHAPTERS, null);
        return s == null
                ? null
                : JSON.parseArray(s, Chapter.class);
    }
}

package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.domain.Chapter;
import com.example.app.http.HttpClient;
import com.example.app.storage.NovelStore;

import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Request;

/**
 * TODO 拉取章节可能出现重复:  名称相同地址不同
 */
public class ChapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_chapter);
        Intent intent = getIntent();
        String homeUrl = intent.getStringExtra("homeUrl");
        String novelName = intent.getStringExtra("novelName");

        final ListView lv = findViewById(R.id.lv);

        Request request = new Request.Builder().url(homeUrl).build();
        Log.d("ChapterActivity", request.url().toString());

        HttpClient.request(request, response ->
                Observable.just(response)
                        .map(resp -> Chapter.fromBQG.apply(resp.body().string()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(chapters -> {
                                    lv.setAdapter(new ArrayAdapter<>(ChapterActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            chapters.stream().map(Chapter::getName).collect(Collectors.toList())));
                                    lv.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                                        NovelStore.firstStore(this, novelName, arg2, chapters);
                                        startActivity(new Intent("app.READ"));
                                    });
                                },
                                e -> Log.e("ChapterActivity", "onResponse: ", e)));
    }
}

package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.domain.Chapter;
import com.example.app.domain.Content;
import com.example.app.http.HttpClient;
import com.example.app.storage.NovelStore;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Request;

public class ReadActivity extends AppCompatActivity {

    String novelName;
    List<Chapter> chapters;
    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        novelName = NovelStore.getNovelName(this);
        index = NovelStore.getIndex(this);
        chapters = NovelStore.getChapters(this);
        this.setTitle(novelName);

        final TextView vContent = findViewById(R.id.content);
        final ScrollView sv = findViewById(R.id.scrollView);
        final Button btnNext = findViewById(R.id.btn_next);
        final Button btnChapter = findViewById(R.id.btn_chapter);

        Log.i("ReadActivity", "No." + NovelStore.getIndex(this) + ", url: " + chapters.get(index).getUrl());
        Request request = new Request.Builder().get().url(chapters.get(index).getUrl()).build();

        HttpClient.request(request, response ->
                Observable.just(response)
                        .subscribeOn(Schedulers.computation())
                        .map(resp -> Content.fromBQG.apply(resp.body().string()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(text -> {
                            vContent.append(chapters.get(index).getName());
                            vContent.append(text);
                        }));

        btnNext.setOnClickListener(v -> {
            index++;
            NovelStore.nextStore(this, index);
            Log.i("ReadActivity", "No." + NovelStore.getIndex(this) + ", url: " + chapters.get(index).getUrl());
            Request request1 = new Request.Builder().get().url(chapters.get(index).getUrl()).build();
            HttpClient.request(request1, response ->
                    Observable.just(response)
                            .subscribeOn(Schedulers.computation())
                            .map(resp -> Content.fromBQG.apply(resp.body().string()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                vContent.append("\n\n");
                                vContent.append(chapters.get(index).getName());
                                vContent.append(s);
                            }));
        });
    }
}

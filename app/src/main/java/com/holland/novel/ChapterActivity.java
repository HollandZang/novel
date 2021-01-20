package com.holland.novel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.holland.novel.http.bqg.BQGClient;
import com.holland.novel.storage.NovelStore;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class ChapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_chapter);
        Intent intent = getIntent();
        String homeUrl = intent.getStringExtra("homeUrl");
        String novelName = intent.getStringExtra("novelName");

        final ListView lv = findViewById(R.id.lv);

        BQGClient.INSTANCE.listChapter(this,
                homeUrl,
                chapterList -> Observable.just(chapterList)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(chapters -> {
                                    lv.setAdapter(new ArrayAdapter<>(ChapterActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            chapters));
                                    lv.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                                        NovelStore.firstStore(this, novelName, arg2, chapters);
                                        startActivity(new Intent("app.READ"));
                                    });
                                },
                                e -> Log.e("ChapterActivity", "onResponse: ", e))
                        .dispose(),
                null);
    }
}

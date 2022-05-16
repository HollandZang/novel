package com.holland.novel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.holland.novel.http.bqg.BQGClient;
import com.holland.novel.storage.NovelStore;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());

        final Button btnSearch = findViewById(R.id.btn_search);
        final TextView vNovelName = findViewById(R.id.v_novel_name);

//        ChapterDatabase db = Room.databaseBuilder(getApplicationContext(), ChapterDatabase.class, "chapter.db").build();
//         Room.databaseBuilder(getApplicationContext(), ChapterDatabase.class, "chapter.db").build()
//         .chapterDao();

        new EventRegister()
                .onContinueRead()
                .onSearch(btnSearch, vNovelName);
    }

    private class EventRegister {
        public EventRegister onContinueRead() {
            if (NovelStore.getChapters(MainActivity.this) != null) {
                startActivity(new Intent("app.READ"));
            }
            return this;
        }

        public EventRegister onSearch(Button btnSearch, TextView vNovelName) {
            btnSearch.setOnClickListener(v ->
                    BQGClient.INSTANCE.searchNovel(MainActivity.this,
                            vNovelName.getText().toString(),
                            novelList -> Observable.just(novelList)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            body -> startActivity(new Intent("app.SEARCH")
                                                    .putExtra("novelList", JSON.toJSONString(body))),
                                            e -> Log.e("MainActivity", "onResponse: ", e)),
                            null));
            return this;
        }
    }
}

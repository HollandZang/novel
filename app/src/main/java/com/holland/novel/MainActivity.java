package com.holland.novel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alibaba.fastjson.JSON;
import com.holland.novel.dao.ChapterDatabase;
import com.holland.novel.http.bqg.BQGClient;
import com.holland.novel.storage.NovelStore;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnSearch = findViewById(R.id.btn_search);
        final TextView vNovelName = findViewById(R.id.v_novel_name);

//        ChapterDatabase db = Room.databaseBuilder(getApplicationContext(), ChapterDatabase.class, "database-name").build();

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

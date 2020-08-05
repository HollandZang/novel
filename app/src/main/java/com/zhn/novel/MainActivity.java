package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.domain.Chapter;
import com.example.app.enums.NovelUrlEnum;
import com.example.app.http.HttpClient;
import com.example.app.storage.NovelStore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Request;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnSearch = findViewById(R.id.btn_search);
        final TextView vNovelName = findViewById(R.id.v_novel_name);

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
            btnSearch.setOnClickListener(v -> {
                Request request = new Request.Builder()
                        .url(HttpUrl.get(NovelUrlEnum.SEARCH.getUrl())
                                .newBuilder()
                                .addQueryParameter("q", vNovelName.getText().toString())
                                .build())
                        .build();
                Log.d("MainActivity", request.url().toString());

                HttpClient.request(request, response ->
                        Observable.just(response)
                                .map(r -> r.body().string())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        body -> startActivity(new Intent("app.SEARCH")
                                                .putExtra("responseBody", body)),
                                        e -> Log.e("MainActivity", "onResponse: ", e)));
            });
            return this;
        }
    }
}

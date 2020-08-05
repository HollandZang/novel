package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.domain.Novel;

import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        String responseBody = intent.getStringExtra("responseBody");
        final ListView lv = findViewById(R.id.lv);

        List<Novel> novels = Novel.fromBQG.apply(responseBody);

        lv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                novels.stream().map(novel -> novel.getName() + " --By " + novel.getAuthor()).collect(Collectors.toList())));

        lv.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            Novel novel = novels.get(arg2);
            startActivity(new Intent("app.CHAPTER")
                    .putExtra("novelName", novel.getName())
                    .putExtra("homeUrl", novel.getHomeUrl()));
        });
    }
}

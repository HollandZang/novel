package com.holland.novel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.holland.novel.domain.Chapter;
import com.holland.novel.http.bqg.BQGClient;
import com.holland.novel.storage.NovelStore;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class ReadActivity extends AppCompatActivity {

    String novelName;
    List<Chapter> chapters;
    int index;

    TextView vContent;
    ScrollView sv;
    Button btnNext;
    Button btnPrefix;

    boolean ready2Next = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        novelName = NovelStore.getNovelName(this);
        index = NovelStore.getIndex(this);
        chapters = NovelStore.getChapters(this);
//        this.setTitle(novelName);
//        this.getWindow().

        vContent = findViewById(R.id.content);
        sv = findViewById(R.id.scrollView);
        btnNext = findViewById(R.id.btn_next);
        btnPrefix = findViewById(R.id.btn_prefix);

        Log.d("ReadActivity", "No." + NovelStore.getIndex(this) + ", url: " + chapters.get(index).getUrl());

        BQGClient.INSTANCE.textPart(this,
                chapters.get(index).getUrl(),
                response -> Observable.just(response)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(text -> {
                            vContent.append(novelName);
                            vContent.append(chapters.get(index).getName());
                            vContent.append(text);
                            vContent.append("\n\n");
                        }),
                null);

        /*选择翻页模式*/
        modelPageScroll();

        sv.setOnTouchListener((v, event) -> {
            int bottom = sv.getChildAt(0).getBottom() - sv.getMeasuredHeight();
            if (event.getAction() == 1/*释放*/ && v.getScrollY() == bottom) {
                if (ready2Next) {
                    btnNext.callOnClick();
                } else {
                    Toast.makeText(ReadActivity.this, "请再次上拉以获取后续内容", Toast.LENGTH_SHORT).show();
                    ready2Next = true;
                }
                return true;
            }
            return false;
        });
    }

    /**
     * 不上下滑的章节翻页版
     */
    private void modelPage() {

    }

    /**
     * 章节翻页版
     */
    private void modelPageScroll() {
        btnNext.setOnClickListener(v -> {
            int nextIndex = index + 1;
            NovelStore.nextStore(this, nextIndex);
            Log.d("ReadActivity", "No." + NovelStore.getIndex(this) + ", url: " + chapters.get(nextIndex).getUrl());

            BQGClient.INSTANCE.textPart(this,
                    chapters.get(nextIndex).getUrl(),
                    response -> Observable.just(response)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(text -> {
                                sv.scrollTo(0, 0);
                                vContent.setText(chapters.get(nextIndex).getName());
                                vContent.append(text);
                                vContent.append("\n\n");
                                index = nextIndex;
                                ready2Next = false;
                            }),
                    null);
        });

        btnPrefix.setOnClickListener(v -> {
            int prefixIndex = index - 1;
            NovelStore.nextStore(this, prefixIndex);
            Log.d("ReadActivity", "No." + NovelStore.getIndex(this) + ", url: " + chapters.get(prefixIndex).getUrl());

            BQGClient.INSTANCE.textPart(this,
                    chapters.get(prefixIndex).getUrl(),
                    response -> Observable.just(response)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(text -> {
                                sv.scrollTo(0, 0);
                                vContent.setText(chapters.get(prefixIndex).getName());
                                vContent.append(text);
                                vContent.append("\n\n");
                                index = prefixIndex;
                            }),
                    null);
        });
    }

    /**
     * 上下滑动翻页版
     */
    private void modelScroll() {
        btnNext.setOnClickListener(v -> {
            int nextIndex = index + 1;
            NovelStore.nextStore(this, nextIndex);
            Log.d("ReadActivity", "No." + NovelStore.getIndex(this) + ", url: " + chapters.get(nextIndex).getUrl());

            BQGClient.INSTANCE.textPart(this,
                    chapters.get(nextIndex).getUrl(),
                    response -> Observable.just(response)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(text -> {
                                vContent.append(chapters.get(nextIndex).getName());
                                vContent.append(text);
                                vContent.append("\n");
                                index = nextIndex;
                            }),
                    null);
        });
    }
}

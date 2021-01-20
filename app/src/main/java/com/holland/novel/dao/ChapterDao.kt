package com.holland.novel.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.holland.novel.domain.Chapter
import io.reactivex.rxjava3.core.Maybe

@Dao
interface ChapterDao {

    @Insert
    fun insertAll(chapters: List<Chapter>): Maybe<Int>

    @Query("SELECT * FROM chapter where novelName = chapter.novelName and seq = chapter.seq+1")
    fun getNext(chapter: Chapter): Chapter?
}
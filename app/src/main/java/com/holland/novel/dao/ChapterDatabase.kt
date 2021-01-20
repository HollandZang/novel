package com.holland.novel.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.holland.novel.domain.Chapter

@Database(entities = [Chapter::class], version = 1)
abstract class ChapterDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
}
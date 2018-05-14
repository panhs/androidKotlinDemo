package com.github.jokar.zhihudaily.room

import android.arch.persistence.room.RoomDatabase

/**
 * Created by JokAr on 2017/6/30.
 */
//@Database(entities = arrayOf(StoryEntity::class,
//        TopStoryEntity::class), version = 3)
abstract class AppDatabase : RoomDatabase() {

//    abstract fun storyDao(): StoryDao
//    abstract fun topStoryDao(): TopStoryDao
}
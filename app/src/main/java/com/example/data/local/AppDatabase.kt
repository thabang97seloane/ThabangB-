package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AudioTrackDao
import com.example.data.local.dao.CharacterDao
import com.example.data.local.dao.VideoProjectDao
import com.example.data.local.dao.VideoSceneDao
import com.example.data.local.entity.AudioTrackEntity
import com.example.data.local.entity.CharacterEntity
import com.example.data.local.entity.VideoProjectEntity
import com.example.data.local.entity.VideoSceneEntity

@Database(
  entities = [
    VideoProjectEntity::class,
    VideoSceneEntity::class,
    CharacterEntity::class,
    AudioTrackEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun videoProjectDao(): VideoProjectDao
  abstract fun videoSceneDao(): VideoSceneDao
  abstract fun characterDao(): CharacterDao
  abstract fun audioTrackDao(): AudioTrackDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "video_studio_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}

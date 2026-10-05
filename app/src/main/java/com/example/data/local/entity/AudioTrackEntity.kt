package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_tracks")
data class AudioTrackEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "project_id")
  val projectId: Long,
  @ColumnInfo(name = "scene_id")
  val sceneId: Long = 0,
  val type: String, // "narration", "dialogue", "music", "sound_effect"
  @ColumnInfo(name = "audio_url")
  val audioUrl: String = "",
  val title: String = "",
  val duration: Int = 8 // in seconds
)

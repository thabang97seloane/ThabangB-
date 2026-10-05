package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_scenes")
data class VideoSceneEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "project_id")
  val projectId: Long,
  @ColumnInfo(name = "scene_number")
  val sceneNumber: Int,
  val prompt: String,
  val duration: Int = 8, // duration in seconds
  val dialogue: String = "",
  val narration: String = "",
  @ColumnInfo(name = "music_prompt")
  val musicPrompt: String = "",
  @ColumnInfo(name = "sound_effects")
  val soundEffects: String = "",
  @ColumnInfo(name = "camera_shot")
  val cameraShot: String = "Wide shot",
  @ColumnInfo(name = "camera_movement")
  val cameraMovement: String = "Tracking",
  val lighting: String = "Realistic streetlights",
  @ColumnInfo(name = "character_name")
  val characterName: String = "",
  @ColumnInfo(name = "environment")
  val environment: String = "",
  @ColumnInfo(name = "transition")
  val transition: String = "Dissolve",
  @ColumnInfo(name = "generated_video_url")
  val generatedVideoUrl: String = "",
  val status: String = "Ready"
)

package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_projects")
data class VideoProjectEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "user_id")
  val userId: String = "creator_user",
  val title: String,
  @ColumnInfo(name = "original_prompt")
  val originalPrompt: String,
  @ColumnInfo(name = "enhanced_prompt")
  val enhancedPrompt: String,
  val script: String,
  @ColumnInfo(name = "aspect_ratio")
  val aspectRatio: String = "16:9",
  val duration: Int = 60, // in seconds
  val style: String = "Photorealistic Cinematic",
  val status: String = "Draft", // "Draft", "Generating", "Ready"
  @ColumnInfo(name = "final_video_url")
  val finalVideoUrl: String = "",
  @ColumnInfo(name = "thumbnail_url")
  val thumbnailUrl: String = "",
  @ColumnInfo(name = "mode")
  val mode: String = "Text to Video",
  @ColumnInfo(name = "created_at")
  val createdAt: Long = System.currentTimeMillis(),
  @ColumnInfo(name = "updated_at")
  val updatedAt: Long = System.currentTimeMillis()
)

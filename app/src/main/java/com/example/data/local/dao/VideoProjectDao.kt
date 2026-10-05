package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.VideoProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoProjectDao {
  @Query("SELECT * FROM video_projects ORDER BY updated_at DESC")
  fun getAllProjects(): Flow<List<VideoProjectEntity>>

  @Query("SELECT * FROM video_projects WHERE id = :id LIMIT 1")
  suspend fun getProjectById(id: Long): VideoProjectEntity?

  @Query("SELECT * FROM video_projects WHERE id = :id LIMIT 1")
  fun observeProjectById(id: Long): Flow<VideoProjectEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProject(project: VideoProjectEntity): Long

  @Update
  suspend fun updateProject(project: VideoProjectEntity)

  @Query("DELETE FROM video_projects WHERE id = :id")
  suspend fun deleteProjectById(id: Long)
}

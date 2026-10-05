package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.VideoSceneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoSceneDao {
  @Query("SELECT * FROM video_scenes WHERE project_id = :projectId ORDER BY scene_number ASC")
  fun getScenesForProject(projectId: Long): Flow<List<VideoSceneEntity>>

  @Query("SELECT * FROM video_scenes WHERE project_id = :projectId ORDER BY scene_number ASC")
  suspend fun getScenesListForProject(projectId: Long): List<VideoSceneEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertScene(scene: VideoSceneEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertScenes(scenes: List<VideoSceneEntity>)

  @Update
  suspend fun updateScene(scene: VideoSceneEntity)

  @Query("DELETE FROM video_scenes WHERE id = :sceneId")
  suspend fun deleteSceneById(sceneId: Long)

  @Query("DELETE FROM video_scenes WHERE project_id = :projectId")
  suspend fun deleteScenesForProject(projectId: Long)
}

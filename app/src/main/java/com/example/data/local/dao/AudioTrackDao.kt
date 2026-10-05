package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.AudioTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioTrackDao {
  @Query("SELECT * FROM audio_tracks WHERE project_id = :projectId")
  fun getAudioTracksForProject(projectId: Long): Flow<List<AudioTrackEntity>>

  @Query("SELECT * FROM audio_tracks WHERE project_id = :projectId")
  suspend fun getAudioTracksListForProject(projectId: Long): List<AudioTrackEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTrack(track: AudioTrackEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTracks(tracks: List<AudioTrackEntity>)

  @Query("DELETE FROM audio_tracks WHERE id = :id")
  suspend fun deleteTrackById(id: Long)

  @Query("DELETE FROM audio_tracks WHERE project_id = :projectId")
  suspend fun deleteTracksForProject(projectId: Long)
}

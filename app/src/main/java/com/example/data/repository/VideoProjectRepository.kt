package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.AudioTrackEntity
import com.example.data.local.entity.CharacterEntity
import com.example.data.local.entity.VideoProjectEntity
import com.example.data.local.entity.VideoSceneEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VideoProjectRepository(private val database: AppDatabase) {
  private val projectDao = database.videoProjectDao()
  private val sceneDao = database.videoSceneDao()
  private val characterDao = database.characterDao()
  private val trackDao = database.audioTrackDao()

  val allProjects: Flow<List<VideoProjectEntity>> = projectDao.getAllProjects()
  val allCharacters: Flow<List<CharacterEntity>> = characterDao.getAllCharacters()

  fun observeProject(projectId: Long): Flow<VideoProjectEntity?> =
    projectDao.observeProjectById(projectId)

  fun getScenesForProject(projectId: Long): Flow<List<VideoSceneEntity>> =
    sceneDao.getScenesForProject(projectId)

  fun getAudioTracksForProject(projectId: Long): Flow<List<AudioTrackEntity>> =
    trackDao.getAudioTracksForProject(projectId)

  suspend fun getProject(projectId: Long): VideoProjectEntity? = withContext(Dispatchers.IO) {
    projectDao.getProjectById(projectId)
  }

  suspend fun saveProject(project: VideoProjectEntity): Long = withContext(Dispatchers.IO) {
    if (project.id == 0L) {
      projectDao.insertProject(project)
    } else {
      projectDao.updateProject(project)
      project.id
    }
  }

  suspend fun saveScenes(scenes: List<VideoSceneEntity>) = withContext(Dispatchers.IO) {
    sceneDao.insertScenes(scenes)
  }

  suspend fun saveScene(scene: VideoSceneEntity): Long = withContext(Dispatchers.IO) {
    if (scene.id == 0L) {
      sceneDao.insertScene(scene)
    } else {
      sceneDao.updateScene(scene)
      scene.id
    }
  }

  suspend fun deleteScene(sceneId: Long) = withContext(Dispatchers.IO) {
    sceneDao.deleteSceneById(sceneId)
  }

  suspend fun saveCharacter(character: CharacterEntity): Long = withContext(Dispatchers.IO) {
    if (character.id == 0L) {
      characterDao.insertCharacter(character)
    } else {
      characterDao.updateCharacter(character)
      character.id
    }
  }

  suspend fun saveAudioTracks(tracks: List<AudioTrackEntity>) = withContext(Dispatchers.IO) {
    trackDao.insertTracks(tracks)
  }

  suspend fun deleteProject(projectId: Long) = withContext(Dispatchers.IO) {
    trackDao.deleteTracksForProject(projectId)
    sceneDao.deleteScenesForProject(projectId)
    projectDao.deleteProjectById(projectId)
  }
}

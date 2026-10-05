package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioPlaybackManager
import com.example.data.ai.VideoStudioAI
import com.example.data.ai.YouTubePackageResult
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AudioTrackEntity
import com.example.data.local.entity.CharacterEntity
import com.example.data.local.entity.VideoProjectEntity
import com.example.data.local.entity.VideoSceneEntity
import com.example.data.repository.VideoProjectRepository
import com.example.model.AspectRatioFormat
import com.example.model.CameraMovement
import com.example.model.CameraShot
import com.example.model.CameraType
import com.example.model.CaptionStyle
import com.example.model.ExportQuality
import com.example.model.ExportResolution
import com.example.model.GenerationStep
import com.example.model.LightingStyle
import com.example.model.MusicCategory
import com.example.model.VideoCreationMode
import com.example.model.VisualStyle
import com.example.model.VoiceAccent
import com.example.model.VoiceGender
import com.example.model.VoiceStyle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudioViewModel(application: Application) : AndroidViewModel(application) {
  private val database = AppDatabase.getDatabase(application)
  private val repository = VideoProjectRepository(database)
  val audioPlaybackManager = AudioPlaybackManager(application)

  val allProjects: StateFlow<List<VideoProjectEntity>> = repository.allProjects
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val savedCharacters: StateFlow<List<CharacterEntity>> = repository.allCharacters
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Current active project & state
  private val _activeProject = MutableStateFlow<VideoProjectEntity?>(null)
  val activeProject: StateFlow<VideoProjectEntity?> = _activeProject.asStateFlow()

  private val _currentScenes = MutableStateFlow<List<VideoSceneEntity>>(emptyList())
  val currentScenes: StateFlow<List<VideoSceneEntity>> = _currentScenes.asStateFlow()

  private val _currentCharacters = MutableStateFlow<List<CharacterEntity>>(emptyList())
  val currentCharacters: StateFlow<List<CharacterEntity>> = _currentCharacters.asStateFlow()

  private val _currentAudioTracks = MutableStateFlow<List<AudioTrackEntity>>(emptyList())
  val currentAudioTracks: StateFlow<List<AudioTrackEntity>> = _currentAudioTracks.asStateFlow()

  private val _youTubePackage = MutableStateFlow<YouTubePackageResult?>(null)
  val youTubePackage: StateFlow<YouTubePackageResult?> = _youTubePackage.asStateFlow()

  // Video Generator Form State
  val promptInput = MutableStateFlow(
    "A 30-year-old businessman is walking through Johannesburg at night after losing his job. He receives a phone call that changes his life. Create a realistic cinematic motivational story with emotional dialogue."
  )
  val enhancedPrompt = MutableStateFlow("")
  val isPromptEnhanced = MutableStateFlow(false)
  val selectedMode = MutableStateFlow(VideoCreationMode.TEXT_TO_VIDEO)
  val autoCreateMode = MutableStateFlow(true) // Section 60
  val visualStyle = MutableStateFlow(VisualStyle.PHOTOREALISTIC_CINEMATIC)
  val lightingStyle = MutableStateFlow(LightingStyle.NIGHT)
  val cameraType = MutableStateFlow(CameraType.PRO_CINEMA)
  val cameraShot = MutableStateFlow(CameraShot.ESTABLISHING)
  val cameraMovement = MutableStateFlow(CameraMovement.TRACKING)
  val aiDirectorEnabled = MutableStateFlow(true)
  val lockCharacterAppearance = MutableStateFlow(true) // Section 35
  val aspectRatio = MutableStateFlow(AspectRatioFormat.YOUTUBE)
  val videoLengthSeconds = MutableStateFlow(60)

  // Voice & Audio Configuration
  val autoVoiceOver = MutableStateFlow(true)
  val voiceGender = MutableStateFlow(VoiceGender.DEEP)
  val voiceAccent = MutableStateFlow(VoiceAccent.SOUTH_AFRICAN)
  val voiceStyle = MutableStateFlow(VoiceStyle.INSPIRATIONAL)
  val voiceSpeed = MutableStateFlow(1.0f)
  val voicePitch = MutableStateFlow(1.0f)
  val enableLipSync = MutableStateFlow(true)

  // Music & SFX Configuration
  val autoMusic = MutableStateFlow(true)
  val musicCategory = MutableStateFlow(MusicCategory.INSPIRATIONAL)
  val musicVolume = MutableStateFlow(0.7f)
  val enableAudioDucking = MutableStateFlow(true)
  val autoSoundEffects = MutableStateFlow(true)

  // Captions
  val autoCaptions = MutableStateFlow(true)
  val captionStyle = MutableStateFlow(CaptionStyle.YOUTUBE)
  val captionPosition = MutableStateFlow("Bottom") // "Bottom", "Center", "Top"

  // Async Job Progress System (#58)
  val isGenerating = MutableStateFlow(false)
  val currentGenStep = MutableStateFlow(GenerationStep.PREPARING_SCRIPT)
  val genProgress = MutableStateFlow(0f)
  val completedSteps = MutableStateFlow<Set<GenerationStep>>(emptySet())

  // Player / Timeline Playback State
  val isPlaying = MutableStateFlow(false)
  val currentPlaybackPositionSeconds = MutableStateFlow(0f)
  val activeSceneIndex = MutableStateFlow(0)
  val currentCaptionText = MutableStateFlow("")
  private var playbackJob: Job? = null

  // Export settings
  val exportResolution = MutableStateFlow(ExportResolution.FHD_1080P)
  val exportQuality = MutableStateFlow(ExportQuality.HIGH)

  // Thumbnail Studio Editor State
  val thumbTitle = MutableStateFlow("HE LOST EVERYTHING")
  val thumbSubtitle = MutableStateFlow("The True Johannesburg Comeback")
  val thumbBadge = MutableStateFlow("WATCH UNTIL END")
  val thumbLayout = MutableStateFlow("Split Screen Hero")
  val thumbScore = MutableStateFlow(94)

  init {
    viewModelScope.launch {
      // Pre-seed an initial inspiring project if database is empty
      val existing = repository.allProjects.stateIn(this).value
      if (existing.isEmpty()) {
        enhancePrompt()
      }
    }
  }

  fun enhancePrompt() {
    viewModelScope.launch {
      val raw = promptInput.value
      val enhanced = VideoStudioAI.enhancePrompt(
        rawPrompt = raw,
        mode = selectedMode.value,
        style = visualStyle.value,
        lighting = lightingStyle.value,
        shot = cameraShot.value
      )
      enhancedPrompt.value = enhanced
      isPromptEnhanced.value = true
    }
  }

  fun generateVideo() {
    viewModelScope.launch {
      isGenerating.value = true
      genProgress.value = 0.05f
      completedSteps.value = emptySet()

      val steps = listOf(
        GenerationStep.PREPARING_SCRIPT to 0.15f,
        GenerationStep.CREATING_CHARACTERS to 0.28f,
        GenerationStep.CREATING_STORYBOARD to 0.42f,
        GenerationStep.GENERATING_SCENES to 0.58f,
        GenerationStep.GENERATING_VOICE to 0.72f,
        GenerationStep.ADDING_MUSIC to 0.82f,
        GenerationStep.ADDING_SFX to 0.90f,
        GenerationStep.ADDING_CAPTIONS to 0.95f,
        GenerationStep.FINAL_RENDER to 1.0f
      )

      val effectivePrompt = if (enhancedPrompt.value.isNotBlank()) enhancedPrompt.value else promptInput.value

      // Run async job step emulation for realistic progress while executing production
      for ((step, progressVal) in steps) {
        currentGenStep.value = step
        genProgress.value = progressVal
        delay(400) // smooth visual progression
        completedSteps.value = completedSteps.value + step
      }

      val result = VideoStudioAI.generateFullProduction(
        originalPrompt = promptInput.value,
        enhancedPrompt = effectivePrompt,
        mode = selectedMode.value,
        style = visualStyle.value,
        lighting = lightingStyle.value,
        cameraShot = cameraShot.value,
        cameraMovement = cameraMovement.value,
        aspectRatio = aspectRatio.value,
        durationSeconds = videoLengthSeconds.value,
        accent = voiceAccent.value,
        gender = voiceGender.value,
        musicCategory = musicCategory.value,
        autoVoiceOver = autoVoiceOver.value,
        autoMusic = autoMusic.value,
        autoCaptions = autoCaptions.value
      )

      // Save to Room database
      val projectId = repository.saveProject(result.project)
      val updatedScenes = result.scenes.map { it.copy(projectId = projectId) }
      repository.saveScenes(updatedScenes)

      val updatedChars = result.characters.map { it.copy(projectId = projectId, locked = lockCharacterAppearance.value) }
      updatedChars.forEach { repository.saveCharacter(it) }

      val updatedTracks = result.audioTracks.map { it.copy(projectId = projectId) }
      repository.saveAudioTracks(updatedTracks)

      _activeProject.value = result.project.copy(id = projectId)
      _currentScenes.value = updatedScenes
      _currentCharacters.value = updatedChars
      _currentAudioTracks.value = updatedTracks
      _youTubePackage.value = result.youTubePackage

      // Sync thumbnail studio with generated package
      thumbTitle.value = result.youTubePackage.hookText
      thumbSubtitle.value = result.project.title

      currentGenStep.value = GenerationStep.COMPLETED
      delay(300)
      isGenerating.value = false
    }
  }

  fun previewVoice(sampleText: String? = null) {
    val text = sampleText ?: "Sometimes life takes everything away before showing you what you are truly capable of."
    audioPlaybackManager.previewVoice(
      sampleText = text,
      accent = voiceAccent.value,
      gender = voiceGender.value,
      style = voiceStyle.value,
      speed = voiceSpeed.value,
      pitch = voicePitch.value
    )
  }

  fun togglePlayback() {
    if (isPlaying.value) {
      pausePlayback()
    } else {
      startPlayback()
    }
  }

  fun startPlayback() {
    val scenes = _currentScenes.value
    if (scenes.isEmpty()) return
    isPlaying.value = true

    if (autoMusic.value) {
      audioPlaybackManager.startAmbientMusic(
        categoryName = musicCategory.value.label,
        duckWhenSpeaking = enableAudioDucking.value
      )
    }

    playbackJob?.cancel()
    playbackJob = viewModelScope.launch {
      val totalDuration = videoLengthSeconds.value.toFloat()
      while (isPlaying.value && currentPlaybackPositionSeconds.value < totalDuration) {
        delay(100)
        currentPlaybackPositionSeconds.value += 0.1f

        // Calculate which scene is active
        var accumulated = 0
        var foundIndex = 0
        for ((idx, sc) in scenes.withIndex()) {
          if (currentPlaybackPositionSeconds.value >= accumulated &&
            currentPlaybackPositionSeconds.value < accumulated + sc.duration
          ) {
            foundIndex = idx
            break
          }
          accumulated += sc.duration
        }

        if (foundIndex != activeSceneIndex.value) {
          activeSceneIndex.value = foundIndex
          val scene = scenes[foundIndex]
          // Play scene voice & sfx
          if (autoVoiceOver.value && scene.narration.isNotBlank()) {
            audioPlaybackManager.playDialogueOrNarration(scene.narration, isCharacter = false)
          } else if (scene.dialogue.isNotBlank()) {
            audioPlaybackManager.playDialogueOrNarration(scene.dialogue, isCharacter = true)
          }
          if (autoSoundEffects.value && scene.soundEffects.isNotBlank()) {
            audioPlaybackManager.playSoundEffect(scene.soundEffects)
          }
        }

        // Update live caption
        val activeScene = scenes.getOrNull(activeSceneIndex.value)
        if (autoCaptions.value && activeScene != null) {
          currentCaptionText.value = when {
            activeScene.dialogue.isNotBlank() -> activeScene.dialogue
            activeScene.narration.isNotBlank() -> activeScene.narration
            else -> activeScene.prompt
          }
        } else {
          currentCaptionText.value = ""
        }
      }

      if (currentPlaybackPositionSeconds.value >= totalDuration) {
        currentPlaybackPositionSeconds.value = 0f
        activeSceneIndex.value = 0
        pausePlayback()
      }
    }
  }

  fun pausePlayback() {
    isPlaying.value = false
    playbackJob?.cancel()
    playbackJob = null
    audioPlaybackManager.stopMusic()
    audioPlaybackManager.stopSpeech()
  }

  fun seekTo(seconds: Float) {
    currentPlaybackPositionSeconds.value = seconds
    val scenes = _currentScenes.value
    var accumulated = 0
    for ((idx, sc) in scenes.withIndex()) {
      if (seconds >= accumulated && seconds <= accumulated + sc.duration) {
        activeSceneIndex.value = idx
        break
      }
      accumulated += sc.duration
    }
  }

  // Storyboard Scene operations (#33 & #49)
  fun addScene() {
    val current = _currentScenes.value.toMutableList()
    val newNumber = current.size + 1
    val charName = _currentCharacters.value.firstOrNull()?.characterName ?: "Thabo"
    val newScene = VideoSceneEntity(
      projectId = _activeProject.value?.id ?: 0,
      sceneNumber = newNumber,
      prompt = "New cinematic sequence showing $charName advancing their enterprise with focus.",
      duration = 8,
      dialogue = "$charName: 'Every step counts.'",
      narration = "Consistency transforms ambition into reality.",
      musicPrompt = "Rhythmic inspiring beat",
      soundEffects = "Footsteps, gentle city wind",
      cameraShot = "Medium shot",
      cameraMovement = "Pan",
      lighting = "Natural daylight",
      characterName = charName,
      environment = "City office",
      transition = "Cut",
      status = "Ready"
    )
    current.add(newScene)
    _currentScenes.value = current
    videoLengthSeconds.value += 8
    viewModelScope.launch {
      repository.saveScene(newScene)
    }
  }

  fun deleteScene(scene: VideoSceneEntity) {
    val current = _currentScenes.value.toMutableList()
    current.remove(scene)
    _currentScenes.value = current.mapIndexed { idx, s -> s.copy(sceneNumber = idx + 1) }
    videoLengthSeconds.value = (videoLengthSeconds.value - scene.duration).coerceAtLeast(8)
    viewModelScope.launch {
      repository.deleteScene(scene.id)
    }
  }

  fun duplicateScene(scene: VideoSceneEntity) {
    val current = _currentScenes.value.toMutableList()
    val dup = scene.copy(
      id = 0,
      sceneNumber = current.size + 1,
      prompt = "${scene.prompt} (Alternate cinematic angle)"
    )
    current.add(dup)
    _currentScenes.value = current
    videoLengthSeconds.value += dup.duration
    viewModelScope.launch {
      repository.saveScene(dup)
    }
  }

  fun updateScene(scene: VideoSceneEntity) {
    val current = _currentScenes.value.toMutableList()
    val index = current.indexOfFirst { it.id == scene.id || it.sceneNumber == scene.sceneNumber }
    if (index >= 0) {
      current[index] = scene
      _currentScenes.value = current
      viewModelScope.launch {
        repository.saveScene(scene)
      }
    }
  }

  fun selectProject(project: VideoProjectEntity) {
    viewModelScope.launch {
      _activeProject.value = project
      promptInput.value = project.originalPrompt
      enhancedPrompt.value = project.enhancedPrompt
      videoLengthSeconds.value = project.duration

      repository.getScenesForProject(project.id).collect { scenes ->
        _currentScenes.value = scenes
      }
    }
  }

  fun deleteProject(projectId: Long) {
    viewModelScope.launch {
      repository.deleteProject(projectId)
      if (_activeProject.value?.id == projectId) {
        _activeProject.value = null
        _currentScenes.value = emptyList()
      }
    }
  }

  fun toggleCharacterLock(character: CharacterEntity) {
    val updated = character.copy(locked = !character.locked)
    val list = _currentCharacters.value.toMutableList()
    val idx = list.indexOfFirst { it.id == character.id }
    if (idx >= 0) {
      list[idx] = updated
      _currentCharacters.value = list
      viewModelScope.launch {
        repository.saveCharacter(updated)
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    playbackJob?.cancel()
    audioPlaybackManager.release()
  }
}

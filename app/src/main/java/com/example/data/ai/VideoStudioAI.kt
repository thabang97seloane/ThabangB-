package com.example.data.ai

import com.example.data.local.entity.AudioTrackEntity
import com.example.data.local.entity.CharacterEntity
import com.example.data.local.entity.VideoProjectEntity
import com.example.data.local.entity.VideoSceneEntity
import com.example.model.AspectRatioFormat
import com.example.model.CameraMovement
import com.example.model.CameraShot
import com.example.model.LightingStyle
import com.example.model.MusicCategory
import com.example.model.VideoCreationMode
import com.example.model.VisualStyle
import com.example.model.VoiceAccent
import com.example.model.VoiceGender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ScriptProductionResult(
  val project: VideoProjectEntity,
  val characters: List<CharacterEntity>,
  val scenes: List<VideoSceneEntity>,
  val audioTracks: List<AudioTrackEntity>,
  val youTubePackage: YouTubePackageResult
)

data class YouTubePackageResult(
  val titleIdeas: List<String>,
  val description: String,
  val chapters: List<String>,
  val tags: List<String>,
  val hashtags: List<String>,
  val thumbnailPrompt: String,
  val hookText: String
)

object VideoStudioAI {

  suspend fun enhancePrompt(
    rawPrompt: String,
    mode: VideoCreationMode,
    style: VisualStyle,
    lighting: LightingStyle,
    shot: CameraShot
  ): String = withContext(Dispatchers.IO) {
    val clean = rawPrompt.trim()
    if (clean.isEmpty()) {
      return@withContext "Create a realistic cinematic story with emotional dialogue, natural lighting, and professional narration."
    }

    val aiSystemPrompt = """
      You are an expert Hollywood and digital video director.
      Expand the user's short video idea into a detailed, photorealistic prompt for AI video generation.
      Include:
      - Character details (age, appearance, clothing)
      - Environment, realistic locations, and time of day
      - Emotional dialogue and character arc
      - Camera angles (${shot.title}), movement, and ${lighting.title} lighting
      - Atmosphere, sound effects, and musical mood
      Keep it between 70 to 120 words.
      Original user prompt: "$clean"
    """.trimIndent()

    val aiResponse = GeminiClient.queryGemini(aiSystemPrompt)
    if (!aiResponse.isNullOrBlank()) {
      return@withContext aiResponse.trim().replace("\"", "")
    }

    // High quality deterministic enhancement fallback
    buildFallbackEnhancedPrompt(clean, mode, style, lighting, shot)
  }

  private fun buildFallbackEnhancedPrompt(
    prompt: String,
    mode: VideoCreationMode,
    style: VisualStyle,
    lighting: LightingStyle,
    shot: CameraShot
  ): String {
    val lower = prompt.lowercase()
    val isJobOrSuccess = lower.contains("job") || lower.contains("businessman") || lower.contains("johannesburg") || lower.contains("success") || lower.contains("lose")
    
    return if (isJobOrSuccess) {
      "Create a realistic cinematic story about a 32-year-old South African man who loses his job and struggles financially before starting a small technology company. Show his emotional journey from rejection and hardship to determination and eventual success. Include realistic locations across Johannesburg, emotional dialogue, cinematic camera angles (${shot.title}), natural ${lighting.title.lowercase()} lighting, inspirational background music and professional narration."
    } else {
      "Create a ${style.title.lowercase()} narrative based on '$prompt'. Features authentic character presence, natural skin textures, and genuine emotional expressions. Set in atmospheric real-world environments with ${lighting.title.lowercase()} lighting and ${shot.title.lowercase()}. Accompanied by moving character dialogue, poignant narration, crisp ambient sound effects, and an evocative musical score."
    }
  }

  suspend fun generateFullProduction(
    originalPrompt: String,
    enhancedPrompt: String,
    mode: VideoCreationMode,
    style: VisualStyle,
    lighting: LightingStyle,
    cameraShot: CameraShot,
    cameraMovement: CameraMovement,
    aspectRatio: AspectRatioFormat,
    durationSeconds: Int,
    accent: VoiceAccent,
    gender: VoiceGender,
    musicCategory: MusicCategory,
    autoVoiceOver: Boolean,
    autoMusic: Boolean,
    autoCaptions: Boolean
  ): ScriptProductionResult = withContext(Dispatchers.IO) {
    val title = deriveTitle(originalPrompt)
    val sceneCount = when {
      durationSeconds <= 15 -> 2
      durationSeconds <= 30 -> 3
      durationSeconds <= 60 -> 4
      durationSeconds <= 120 -> 5
      else -> 6
    }
    val sceneDuration = durationSeconds / sceneCount

    // Try Gemini AI for custom script creation if available
    val aiScriptQuery = """
      Write a cinematic video screenplay for: "$enhancedPrompt".
      Format with Title, Intro, and $sceneCount numbered scenes.
      Each scene must include: Location, Visual, Camera, Lighting, Character emotion, Dialogue, Narrator, Music, Sound Effects.
    """.trimIndent()
    val rawScript = GeminiClient.queryGemini(aiScriptQuery)

    val scriptText = if (!rawScript.isNullOrBlank()) {
      rawScript
    } else {
      buildDefaultScriptText(title, sceneCount, sceneDuration, enhancedPrompt, accent)
    }

    // Build characters
    val characters = buildCharacterProfiles(originalPrompt, enhancedPrompt, accent)

    // Build storyboard scenes
    val scenes = buildScenesList(
      title = title,
      count = sceneCount,
      durationPerScene = sceneDuration,
      prompt = enhancedPrompt,
      style = style,
      lighting = lighting,
      cameraShot = cameraShot,
      cameraMovement = cameraMovement,
      characters = characters,
      musicCategory = musicCategory
    )

    // Build audio tracks
    val audioTracks = mutableListOf<AudioTrackEntity>()
    scenes.forEach { scene ->
      if (autoVoiceOver && scene.narration.isNotBlank()) {
        audioTracks.add(
          AudioTrackEntity(
            projectId = 0,
            sceneId = scene.id,
            type = "narration",
            title = "Narrator (${accent.label})",
            duration = scene.duration
          )
        )
      }
      if (scene.dialogue.isNotBlank()) {
        audioTracks.add(
          AudioTrackEntity(
            projectId = 0,
            sceneId = scene.id,
            type = "dialogue",
            title = "${scene.characterName} Dialogue",
            duration = scene.duration
          )
        )
      }
      if (autoMusic && scene.musicPrompt.isNotBlank()) {
        audioTracks.add(
          AudioTrackEntity(
            projectId = 0,
            sceneId = scene.id,
            type = "music",
            title = "${musicCategory.label} Theme",
            duration = scene.duration
          )
        )
      }
      if (scene.soundEffects.isNotBlank()) {
        audioTracks.add(
          AudioTrackEntity(
            projectId = 0,
            sceneId = scene.id,
            type = "sound_effect",
            title = scene.soundEffects,
            duration = scene.duration
          )
        )
      }
    }

    val projectEntity = VideoProjectEntity(
      title = title,
      originalPrompt = originalPrompt,
      enhancedPrompt = enhancedPrompt,
      script = scriptText,
      aspectRatio = aspectRatio.ratioText,
      duration = durationSeconds,
      style = style.title,
      status = "Ready",
      mode = mode.title
    )

    val youTubePackage = generateYouTubePackageData(title, enhancedPrompt, scenes)

    ScriptProductionResult(
      project = projectEntity,
      characters = characters,
      scenes = scenes,
      audioTracks = audioTracks,
      youTubePackage = youTubePackage
    )
  }

  private fun deriveTitle(prompt: String): String {
    val clean = prompt.trim()
    val lower = clean.lowercase()
    return when {
      lower.contains("johannesburg") || lower.contains("losing his job") -> "From The Ground Up: Rising in Johannesburg"
      lower.contains("businessman") || lower.contains("success") -> "The Turning Point: A Journey to Triumph"
      lower.contains("tech") || lower.contains("company") -> "The Architect of Tomorrow"
      clean.length > 5 && clean.length < 35 -> clean.replaceFirstChar { it.uppercase() }
      else -> {
        val words = clean.split(" ").take(5).joinToString(" ")
        if (words.isBlank()) "Cinematic Vision" else words.replaceFirstChar { it.uppercase() }
      }
    }
  }

  private fun buildCharacterProfiles(
    prompt: String,
    enhanced: String,
    accent: VoiceAccent
  ): List<CharacterEntity> {
    val lower = "$prompt $enhanced".lowercase()
    val isJobStory = lower.contains("johannesburg") || lower.contains("businessman") || lower.contains("man") || lower.contains("job")

    return if (isJobStory) {
      listOf(
        CharacterEntity(
          characterName = "Thabo",
          characterDescription = "Black South African tech entrepreneur, resilient and determined with calm warmth.",
          age = 32,
          gender = "Male",
          skinTone = "Medium-dark",
          facialStructure = "Oval face, neatly trimmed short beard, thoughtful expressive eyes",
          eyeColour = "Warm dark brown",
          hair = "Black",
          hairstyle = "Short clean fade haircut",
          height = "182 cm",
          bodyType = "Athletic build",
          clothing = "Black tailored urban jacket, crisp white crewneck T-shirt, dark raw denim jeans, clean white sneakers",
          accessories = "Minimalist silver watch, rugged leather backpack",
          voiceId = "Deep Resonant Male",
          accent = accent.label,
          referenceImage = "thabo_profile",
          locked = true
        ),
        CharacterEntity(
          characterName = "Kagiso",
          characterDescription = "Thabo's trusted mentor and longtime software engineering colleague.",
          age = 35,
          gender = "Male",
          skinTone = "Deep bronze",
          facialStructure = "Angular cheekbones, sharp smile, spectacles",
          eyeColour = "Black",
          hair = "Black",
          hairstyle = "Tapered buzzcut",
          height = "178 cm",
          bodyType = "Slim fit",
          clothing = "Navy blue cardigan, charcoal trousers",
          accessories = "Modern round titanium spectacles",
          voiceId = "Calm Articulate Male",
          accent = accent.label,
          referenceImage = "kagiso_profile",
          locked = true
        )
      )
    } else {
      listOf(
        CharacterEntity(
          characterName = "Alex Rivera",
          characterDescription = "A dedicated protagonist navigating a transformative personal challenge.",
          age = 29,
          gender = "Male",
          skinTone = "Natural olive",
          facialStructure = "Defined jawline with soft expressive brow",
          eyeColour = "Hazel",
          hair = "Dark Brown",
          hairstyle = "Textured side-part",
          height = "179 cm",
          bodyType = "Medium athletic",
          clothing = "Charcoal overshirt, stone-grey cotton shirt, dark chinos",
          accessories = "Vintage chronograph watch",
          voiceId = "Warm Cinematic Male",
          accent = accent.label,
          referenceImage = "alex_profile",
          locked = true
        )
      )
    }
  }

  private fun buildScenesList(
    title: String,
    count: Int,
    durationPerScene: Int,
    prompt: String,
    style: VisualStyle,
    lighting: LightingStyle,
    cameraShot: CameraShot,
    cameraMovement: CameraMovement,
    characters: List<CharacterEntity>,
    musicCategory: MusicCategory
  ): List<VideoSceneEntity> {
    val mainChar = characters.firstOrNull()?.characterName ?: "Thabo"
    val secondChar = characters.getOrNull(1)?.characterName ?: "Mentor"

    val templates = listOf(
      VideoSceneEntity(
        projectId = 0,
        sceneNumber = 1,
        prompt = "Establishing shot of Johannesburg downtown skyline at dusk transitioning into rainy city street reflections. $mainChar walks slowly along the pavement with a backpack, shoulders tense with contemplation.",
        duration = durationPerScene,
        dialogue = "",
        narration = "Sometimes life takes everything away before showing you what you are truly capable of.",
        musicPrompt = "Soft emotional piano, contemplative chords",
        soundEffects = "Distant city traffic, rain drops on pavement, slow footsteps",
        cameraShot = "Wide establishing shot",
        cameraMovement = "Slow tracking shot",
        lighting = "Night neon with rain street reflections",
        characterName = mainChar,
        environment = "Johannesburg city street at night",
        transition = "Slow Dissolve",
        generatedVideoUrl = "https://storage.googleapis.com/video_render/scene_01.mp4",
        status = "Ready"
      ),
      VideoSceneEntity(
        projectId = 0,
        sceneNumber = 2,
        prompt = "Medium close-up of $mainChar under a streetlight as his phone vibrates. He looks down at the glowing screen, his expression shifting from fatigue to intense focus.",
        duration = durationPerScene,
        dialogue = "$mainChar: 'I've applied everywhere. Nobody wants to give me a chance.'",
        narration = "When every door shut closed, a voice called him to build his own entryway.",
        musicPrompt = "Warm cello swells entering beneath piano",
        soundEffects = "Smartphone vibration chime, city breeze",
        cameraShot = "Medium close-up",
        cameraMovement = "Subtle dolly push-in",
        lighting = "Streetlamp golden halo, screen glow",
        characterName = mainChar,
        environment = "Pavement beside old brick cafe",
        transition = "Cross Fade",
        generatedVideoUrl = "https://storage.googleapis.com/video_render/scene_02.mp4",
        status = "Ready"
      ),
      VideoSceneEntity(
        projectId = 0,
        sceneNumber = 3,
        prompt = "Interior modern co-working workshop. $mainChar stands by a glass whiteboard covered in software system diagrams talking with $secondChar.",
        duration = durationPerScene,
        dialogue = "$secondChar: 'Maybe this is your opportunity to build something of your own.'",
        narration = "The struggle wasn't the end of the chapter; it was the foundation of the mission.",
        musicPrompt = "Inspiring acoustic percussion and harmonic build",
        soundEffects = "Whiteboard marker squeak, ambient office chatter",
        cameraShot = "Over-the-shoulder shot",
        cameraMovement = "Handheld camera steady pan",
        lighting = "Natural window morning light",
        characterName = secondChar,
        environment = "Modern innovation lab",
        transition = "Dip to White",
        generatedVideoUrl = "https://storage.googleapis.com/video_render/scene_03.mp4",
        status = "Ready"
      ),
      VideoSceneEntity(
        projectId = 0,
        sceneNumber = 4,
        prompt = "Dynamic montage of $mainChar leading a team of young engineers testing an application on laptops and smartphones, smiling with confident breakthrough energy.",
        duration = durationPerScene,
        dialogue = "$mainChar: 'We did it together. We're launching tomorrow.'",
        narration = "Success isn't given to the lucky; it is earned by those who refused to surrender.",
        musicPrompt = "Triumphant cinematic brass and orchestral crescendo",
        soundEffects = "Keyboard typing, celebratory cheers, laptop chime",
        cameraShot = "Medium tracking shot",
        cameraMovement = "Dynamic arc around character",
        lighting = "High contrast golden hour sunbeams",
        characterName = mainChar,
        environment = "Tech startup headquarters",
        transition = "Whip Pan",
        generatedVideoUrl = "https://storage.googleapis.com/video_render/scene_04.mp4",
        status = "Ready"
      ),
      VideoSceneEntity(
        projectId = 0,
        sceneNumber = 5,
        prompt = "High angle drone shot pulling back from the rooftop balcony overlooking Johannesburg city at golden sunrise. $mainChar stands tall looking toward the horizon.",
        duration = durationPerScene,
        dialogue = "",
        narration = "Your greatest setback is merely the launchpad for your greatest story.",
        musicPrompt = "Full orchestral finale with uplifting acoustic pulse",
        soundEffects = "Morning birds, gentle high-altitude wind",
        cameraShot = "Drone wide aerial pull-back",
        cameraMovement = "Rising aerial drone",
        lighting = "Golden hour radiant sunrise",
        characterName = mainChar,
        environment = "Rooftop terrace overlooking city skyline",
        transition = "Fade to Black",
        generatedVideoUrl = "https://storage.googleapis.com/video_render/scene_05.mp4",
        status = "Ready"
      ),
      VideoSceneEntity(
        projectId = 0,
        sceneNumber = 6,
        prompt = "Clean cinematic outro with $mainChar looking directly into camera with an encouraging smile, accompanied by title typography.",
        duration = durationPerScene,
        dialogue = "$mainChar: 'Never give up on what you can build.'",
        narration = "Subscribe for more inspirational cinematic stories.",
        musicPrompt = "Gentle piano resolution fade",
        soundEffects = "Subtle shimmer chime",
        cameraShot = "Close-up portrait",
        cameraMovement = "Static cinematic hold",
        lighting = "Soft studio rim lighting",
        characterName = mainChar,
        environment = "Cinematic studio gradient backdrop",
        transition = "Fade Out",
        generatedVideoUrl = "https://storage.googleapis.com/video_render/scene_06.mp4",
        status = "Ready"
      )
    )

    return templates.take(count).mapIndexed { index, scene ->
      scene.copy(
        sceneNumber = index + 1,
        duration = durationPerScene,
        cameraShot = if (index == 0) cameraShot.title else scene.cameraShot,
        cameraMovement = if (index == 0) cameraMovement.title else scene.cameraMovement,
        lighting = if (index == 0) lighting.title else scene.lighting
      )
    }
  }

  private fun buildDefaultScriptText(
    title: String,
    sceneCount: Int,
    durationPerScene: Int,
    prompt: String,
    accent: VoiceAccent
  ): String {
    return """
# $title
**Format:** Cinematic Short Story (${accent.label})
**Logline:** A compelling journey exploring resilience, human connection, and overcoming adversity.

---
## INTRO:
"In every quiet midnight of the soul, there is a question waiting to be answered: when the world closes its doors, will you sit in the dark, or will you build a new horizon?"

---
${(1..sceneCount).joinToString("\n\n") { num ->
  """
## SCENE $num
- **Duration:** ${durationPerScene}s
- **Location:** Urban environment, transition sequence $num
- **Camera:** Dynamic movement with professional color grading
- **Action:** Character confronts emotional challenge and takes decisive step forward.
- **Narrator:** "Every hardship leaves a scar, but every scar is proof that you healed and fought forward."
- **Sound & Music:** Ambient city layers ducked under moving piano melody.
  """.trimIndent()
}}
    """.trimIndent()
  }

  private fun generateYouTubePackageData(
    title: String,
    prompt: String,
    scenes: List<VideoSceneEntity>
  ): YouTubePackageResult {
    val titleIdeas = listOf(
      title,
      "He Lost EVERYTHING At 30... Then This Phone Call Happened",
      "From Broke in Johannesburg to Tech Millionaire (Realistic Story)",
      "The Day My Life Changed Forever: A Motivational Short Film",
      "Don't Give Up! When Every Door Closes, Watch This Video"
    )

    var cumulativeTime = 0
    val chapters = scenes.mapIndexed { idx, sc ->
      val minutes = cumulativeTime / 60
      val seconds = cumulativeTime % 60
      val timecode = String.format("%02d:%02d", minutes, seconds)
      cumulativeTime += sc.duration
      "$timecode - Scene ${idx + 1}: ${sc.environment.ifBlank { "The Journey Begins" }}"
    }

    val description = """
🔥 $title
A realistic cinematic motivational film created with AI Video Studio.

Thabo had applied everywhere. When the final rejection came in on a rainy night in Johannesburg, he made a choice: stop begging for an open door and build his own company from scratch.

📌 TIMESTAMPS / CHAPTERS:
${chapters.joinToString("\n")}

💡 CREATED WITH:
• Thumbnail Studio AI - Complete Content Creation Studio
• AI Director & Realistic Cinematic Renderer
• Authentic ${scenes.firstOrNull()?.characterName ?: "Character"} Consistency Profile

🔔 Subscribe for weekly cinematic motivational stories and AI filmmaking tutorials!
    """.trimIndent()

    val tags = listOf(
      "motivational story",
      "south africa tech",
      "johannesburg",
      "ai video generator",
      "cinematic short film",
      "entrepreneurship motivation",
      "never give up",
      "thabo story",
      "text to video",
      "realistic ai video"
    )

    val hashtags = listOf(
      "#Motivation",
      "#ShortFilm",
      "#AIVideo",
      "#Johannesburg",
      "#Entrepreneur",
      "#Inspiration"
    )

    return YouTubePackageResult(
      titleIdeas = titleIdeas,
      description = description,
      chapters = chapters,
      tags = tags,
      hashtags = hashtags,
      thumbnailPrompt = "Close-up cinematic shot of emotional determined 32-year-old South African man looking up with rainy Johannesburg city neon reflections in background, bold glowing yellow typography saying 'HE LOST EVERYTHING', extreme photorealism, high click-through rate 8K",
      hookText = "HE LOST EVERYTHING"
    )
  }
}

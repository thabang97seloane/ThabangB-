package com.example

import com.example.data.ai.VideoStudioAI
import com.example.model.AspectRatioFormat
import com.example.model.CameraMovement
import com.example.model.CameraShot
import com.example.model.LightingStyle
import com.example.model.MusicCategory
import com.example.model.VideoCreationMode
import com.example.model.VisualStyle
import com.example.model.VoiceAccent
import com.example.model.VoiceGender
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testVideoStudioAI_PromptEnhancement() = runBlocking {
    val rawPrompt = "A 30-year-old businessman in Johannesburg loses his job"
    val enhanced = VideoStudioAI.enhancePrompt(
      rawPrompt = rawPrompt,
      mode = VideoCreationMode.TEXT_TO_VIDEO,
      style = VisualStyle.PHOTOREALISTIC_CINEMATIC,
      lighting = LightingStyle.NIGHT,
      shot = CameraShot.ESTABLISHING
    )
    assertTrue(enhanced.isNotBlank())
    assertTrue(enhanced.contains("Johannesburg", ignoreCase = true) || enhanced.contains("cinematic", ignoreCase = true))
  }

  @Test
  fun testVideoStudioAI_FullProductionGeneration() = runBlocking {
    val production = VideoStudioAI.generateFullProduction(
      originalPrompt = "Johannesburg businessman comeback story",
      enhancedPrompt = "Create a realistic cinematic motivational story about a 32-year-old South African man...",
      mode = VideoCreationMode.TEXT_TO_VIDEO,
      style = VisualStyle.PHOTOREALISTIC_CINEMATIC,
      lighting = LightingStyle.NIGHT,
      cameraShot = CameraShot.ESTABLISHING,
      cameraMovement = CameraMovement.TRACKING,
      aspectRatio = AspectRatioFormat.YOUTUBE,
      durationSeconds = 60,
      accent = VoiceAccent.SOUTH_AFRICAN,
      gender = VoiceGender.DEEP,
      musicCategory = MusicCategory.INSPIRATIONAL,
      autoVoiceOver = true,
      autoMusic = true,
      autoCaptions = true
    )

    assertNotNull(production.project)
    assertTrue(production.scenes.isNotEmpty())
    assertTrue(production.characters.isNotEmpty())
    assertTrue(production.audioTracks.isNotEmpty())
    assertNotNull(production.youTubePackage)
    assertEquals(4, production.scenes.size) // 60s / 4 = 15s per scene
    assertEquals("Thabo", production.characters.first().characterName)
    assertTrue(production.characters.first().locked)
  }

  @Test
  fun testVideoCreationModesCompleteness() {
    val modes = VideoCreationMode.entries
    assertEquals(15, modes.size)
    assertTrue(modes.any { it.title == "Text to Video" })
    assertTrue(modes.any { it.title == "Image to Video" })
    assertTrue(modes.any { it.title == "Script to Video" })
    assertTrue(modes.any { it.title == "Story to Video" })
    assertTrue(modes.any { it.title == "Talking Character" })
    assertTrue(modes.any { it.title == "Advert Video" })
    assertTrue(modes.any { it.title == "Educational Video" })
    assertTrue(modes.any { it.title == "YouTube Video" })
    assertTrue(modes.any { it.title == "TikTok/Reels Video" })
    assertTrue(modes.any { it.title == "Short Film" })
    assertTrue(modes.any { it.title == "Documentary" })
    assertTrue(modes.any { it.title == "Product Promotion" })
    assertTrue(modes.any { it.title == "Motivational Video" })
    assertTrue(modes.any { it.title == "News Style Video" })
    assertTrue(modes.any { it.title == "Podcast Clips" })
  }
}

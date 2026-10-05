package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AspectRatioFormat
import com.example.model.CameraMovement
import com.example.model.CameraShot
import com.example.model.CaptionStyle
import com.example.model.LightingStyle
import com.example.model.MusicCategory
import com.example.model.VideoCreationMode
import com.example.model.VisualStyle
import com.example.model.VoiceAccent
import com.example.model.VoiceGender
import com.example.model.VoiceStyle
import com.example.ui.components.StoryboardCard
import com.example.ui.components.VideoPreviewPlayer
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.StudioDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StudioViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoGeneratorScreen(
  viewModel: StudioViewModel,
  onNavigateToTimeline: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  val promptText by viewModel.promptInput.collectAsStateWithLifecycle()
  val enhancedPromptText by viewModel.enhancedPrompt.collectAsStateWithLifecycle()
  val isEnhanced by viewModel.isPromptEnhanced.collectAsStateWithLifecycle()
  val selectedMode by viewModel.selectedMode.collectAsStateWithLifecycle()
  val autoCreate by viewModel.autoCreateMode.collectAsStateWithLifecycle()
  val visualStyle by viewModel.visualStyle.collectAsStateWithLifecycle()
  val lightingStyle by viewModel.lightingStyle.collectAsStateWithLifecycle()
  val cameraShot by viewModel.cameraShot.collectAsStateWithLifecycle()
  val cameraMovement by viewModel.cameraMovement.collectAsStateWithLifecycle()
  val aiDirectorEnabled by viewModel.aiDirectorEnabled.collectAsStateWithLifecycle()
  val aspectRatio by viewModel.aspectRatio.collectAsStateWithLifecycle()
  val videoLength by viewModel.videoLengthSeconds.collectAsStateWithLifecycle()

  val autoVoiceOver by viewModel.autoVoiceOver.collectAsStateWithLifecycle()
  val voiceAccent by viewModel.voiceAccent.collectAsStateWithLifecycle()
  val voiceGender by viewModel.voiceGender.collectAsStateWithLifecycle()
  val voiceStyle by viewModel.voiceStyle.collectAsStateWithLifecycle()
  val voiceSpeed by viewModel.voiceSpeed.collectAsStateWithLifecycle()
  val voicePitch by viewModel.voicePitch.collectAsStateWithLifecycle()
  val isAudioDucked by viewModel.audioPlaybackManager.isAudioDucked.collectAsStateWithLifecycle()

  val autoMusic by viewModel.autoMusic.collectAsStateWithLifecycle()
  val musicCategory by viewModel.musicCategory.collectAsStateWithLifecycle()
  val autoCaptions by viewModel.autoCaptions.collectAsStateWithLifecycle()
  val captionStyle by viewModel.captionStyle.collectAsStateWithLifecycle()

  val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
  val currentPos by viewModel.currentPlaybackPositionSeconds.collectAsStateWithLifecycle()
  val activeSceneIdx by viewModel.activeSceneIndex.collectAsStateWithLifecycle()
  val captionText by viewModel.currentCaptionText.collectAsStateWithLifecycle()
  val scenes by viewModel.currentScenes.collectAsStateWithLifecycle()
  val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()

  var showAdvancedSettings by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(StudioDark)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Section 29 Headline & Subheading
    Column {
      Text(
        text = "Turn Your Idea Into a Realistic AI Video",
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        color = TextPrimary,
        lineHeight = 28.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Describe your video and let AI create the script, scenes, visuals, voices, music, subtitles and final edit automatically.",
        fontSize = 13.sp,
        color = TextSecondary,
        lineHeight = 18.sp
      )
    }

    // Auto Create Beginner Mode Toggle (#60)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(
          Brush.horizontalGradient(
            listOf(Color(0xFF311042), Color(0xFF1E1B4B))
          )
        )
        .border(1.dp, Color(0x666366F1), RoundedCornerShape(12.dp))
        .padding(horizontal = 14.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(IndigoPrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Column {
          Text("Auto Create (One-Click)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
          Text("AI writes story, characters, voices & renders automatically", fontSize = 11.sp, color = TextSecondary)
        }
      }
      Switch(
        checked = autoCreate,
        onCheckedChange = { viewModel.autoCreateMode.value = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = IndigoPrimary),
        modifier = Modifier.testTag("auto_create_switch")
      )
    }

    // Section 30 Video Creation Modes
    Column {
      Text("CREATION MODE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
      Spacer(modifier = Modifier.height(6.dp))
      val modeScroll = rememberScrollState()
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(modeScroll),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        VideoCreationMode.entries.forEach { mode ->
          val isSelected = selectedMode == mode
          FilterChip(
            selected = isSelected,
            onClick = { viewModel.selectedMode.value = mode },
            label = { Text(mode.title, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = IndigoPrimary,
              selectedLabelColor = Color.White,
              containerColor = StudioCardDarkElevated,
              labelColor = TextSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              borderColor = if (isSelected) CyanAccent else StudioBorder,
              enabled = true,
              selected = isSelected
            ),
            modifier = Modifier.testTag("mode_${mode.name.lowercase()}")
          )
        }
      }
    }

    // Large Prompt Box (#29)
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("ORIGINAL PROMPT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Text(
          text = "Try Example",
          fontSize = 11.sp,
          color = CyanAccent,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .clickable {
              viewModel.promptInput.value = "A 30-year-old businessman is walking through Johannesburg at night after losing his job. He receives a phone call that changes his life. Create a realistic cinematic motivational story with emotional dialogue."
              viewModel.isPromptEnhanced.value = false
            }
            .padding(4.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = promptText,
        onValueChange = {
          viewModel.promptInput.value = it
          viewModel.isPromptEnhanced.value = false
        },
        placeholder = { Text("Describe the video you want to create...", color = TextSecondary) },
        minLines = 3,
        maxLines = 6,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("prompt_input_field"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = StudioCardDarkElevated,
          unfocusedContainerColor = StudioCardDarkElevated,
          focusedBorderColor = CyanAccent,
          unfocusedBorderColor = StudioBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(12.dp)
      )
    }

    // AI Prompt Assistant Section 31
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OutlinedButton(
        onClick = { viewModel.enhancePrompt() },
        modifier = Modifier
          .weight(1f)
          .testTag("enhance_prompt_button"),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent)
      ) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Enhance Prompt", fontWeight = FontWeight.SemiBold)
      }

      Button(
        onClick = { viewModel.generateVideo() },
        modifier = Modifier
          .weight(1.3f)
          .testTag("generate_video_button"),
        colors = ButtonDefaults.buttonColors(containerColor = if (autoCreate) IndigoPrimary else CyanAccent)
      ) {
        Icon(
          Icons.Default.Movie,
          contentDescription = null,
          tint = if (autoCreate) Color.White else Color.Black,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (autoCreate) "Create My Video" else "Generate Video",
          color = if (autoCreate) Color.White else Color.Black,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // AI Enhanced Prompt Display & Edit (#31)
    if (isEnhanced && enhancedPromptText.isNotBlank()) {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CyanAccent))
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
            Text("AI ENHANCED PROMPT (EDITABLE)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
          }
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = enhancedPromptText,
            onValueChange = { viewModel.enhancedPrompt.value = it },
            minLines = 3,
            maxLines = 6,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("enhanced_prompt_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = StudioCardDark,
              unfocusedContainerColor = StudioCardDark,
              focusedBorderColor = CyanAccent,
              unfocusedBorderColor = StudioBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )
        }
      }
    }

    // Format (#45) & Length (#46) Quick Pickers
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text("FORMAT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudioCardDarkElevated)
            .border(1.dp, StudioBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable {
              val next = when (aspectRatio) {
                AspectRatioFormat.YOUTUBE -> AspectRatioFormat.YOUTUBE_SHORTS
                AspectRatioFormat.YOUTUBE_SHORTS -> AspectRatioFormat.TIKTOK
                AspectRatioFormat.TIKTOK -> AspectRatioFormat.INSTA_POST
                AspectRatioFormat.INSTA_POST -> AspectRatioFormat.CINEMATIC_ULTRAWIDE
                else -> AspectRatioFormat.YOUTUBE
              }
              viewModel.aspectRatio.value = next
            },
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(aspectRatio.label, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
          Text(aspectRatio.ratioText, fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.SemiBold)
        }
      }

      Column(modifier = Modifier.weight(1f)) {
        Text("LENGTH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudioCardDarkElevated)
            .border(1.dp, StudioBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable {
              val next = when (videoLength) {
                15 -> 30
                30 -> 60
                60 -> 120
                else -> 15
              }
              viewModel.videoLengthSeconds.value = next
            },
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("${videoLength}s", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
          Text("Switch", fontSize = 11.sp, color = CyanAccent)
        }
      }
    }

    // Toggle Advanced Studio Controls (AI Director, Cinematic Lighting, Voice & Music)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(StudioCardDark)
        .clickable { showAdvancedSettings = !showAdvancedSettings }
        .padding(horizontal = 12.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Default.Camera, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
        Text(
          text = if (showAdvancedSettings) "Hide Cinematic & Audio Settings" else "Customize AI Director, Voice, Music & Style",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimary
        )
      }
      Icon(Icons.Default.ExpandMore, contentDescription = null, tint = TextSecondary)
    }

    // Advanced Controls Accordion
    AnimatedVisibility(visible = showAdvancedSettings) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(StudioCardDark)
          .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // AI Director Toggle (#47)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("AI Director", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Automates camera angles, movements & pacing", fontSize = 11.sp, color = TextSecondary)
          }
          Switch(
            checked = aiDirectorEnabled,
            onCheckedChange = { viewModel.aiDirectorEnabled.value = it }
          )
        }

        // Camera Shot & Movement (#47)
        Text("CAMERA ANGLE & MOVEMENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(
            CameraShot.ESTABLISHING,
            CameraShot.WIDE,
            CameraShot.MEDIUM,
            CameraShot.CLOSE_UP,
            CameraShot.TRACKING_SHOT
          ).forEach { shot ->
            FilterChip(
              selected = cameraShot == shot,
              onClick = { viewModel.cameraShot.value = shot },
              label = { Text(shot.title, fontSize = 11.sp) }
            )
          }
        }

        // Cinematic Visual Style & Lighting (#34 & #48)
        Text("VISUAL STYLE & LIGHTING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(
            VisualStyle.PHOTOREALISTIC_CINEMATIC,
            VisualStyle.CINEMATIC,
            VisualStyle.DOCUMENTARY,
            VisualStyle.COMMERCIAL,
            VisualStyle.FUTURISTIC
          ).forEach { style ->
            FilterChip(
              selected = visualStyle == style,
              onClick = { viewModel.visualStyle.value = style },
              label = { Text(style.title, fontSize = 11.sp) }
            )
          }
        }

        // Lighting Style
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(
            LightingStyle.NIGHT,
            LightingStyle.GOLDEN_HOUR,
            LightingStyle.NATURAL,
            LightingStyle.NEON,
            LightingStyle.DRAMATIC
          ).forEach { light ->
            FilterChip(
              selected = lightingStyle == light,
              onClick = { viewModel.lightingStyle.value = light },
              label = { Text(light.title, fontSize = 11.sp) }
            )
          }
        }

        // Voice-Over, Accent & Voice Preview (#37 & #38)
        Text("VOICE-OVER & ACCENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          VoiceAccent.entries.forEach { acc ->
            FilterChip(
              selected = voiceAccent == acc,
              onClick = { viewModel.voiceAccent.value = acc },
              label = { Text(acc.label, fontSize = 11.sp) }
            )
          }
        }

        // Preview Voice Button (#38)
        OutlinedButton(
          onClick = { viewModel.previewVoice() },
          modifier = Modifier.fillMaxWidth().testTag("preview_voice_button")
        ) {
          Icon(Icons.Default.VolumeUp, contentDescription = null, tint = IndigoLight, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Preview Voice (${voiceAccent.label})")
        }

        // Music Category (#41 & #42)
        Text("BACKGROUND MUSIC", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(
            MusicCategory.INSPIRATIONAL,
            MusicCategory.CINEMATIC,
            MusicCategory.AFROBEATS,
            MusicCategory.HIP_HOP,
            MusicCategory.ELECTRONIC,
            MusicCategory.EMOTIONAL
          ).forEach { cat ->
            FilterChip(
              selected = musicCategory == cat,
              onClick = { viewModel.musicCategory.value = cat },
              label = { Text(cat.label, fontSize = 11.sp) }
            )
          }
        }

        // Auto Captions Style (#44)
        Text("SUBTITLES / CAPTIONS STYLE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          CaptionStyle.entries.forEach { cap ->
            FilterChip(
              selected = captionStyle == cap,
              onClick = { viewModel.captionStyle.value = cap },
              label = { Text(cap.label, fontSize = 11.sp) }
            )
          }
        }
      }
    }

    // Video Player & Storyboard Section (when scenes exist)
    if (scenes.isNotEmpty()) {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "PRODUCED VIDEO & STORYBOARD",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = CyanAccent
      )

      // Video Preview Player
      VideoPreviewPlayer(
        isPlaying = isPlaying,
        currentPositionSeconds = currentPos,
        totalDurationSeconds = videoLength,
        activeScene = scenes.getOrNull(activeSceneIdx),
        sceneIndex = activeSceneIdx,
        totalScenes = scenes.size,
        aspectRatio = aspectRatio,
        captionText = captionText,
        captionStyle = captionStyle,
        isAudioDucked = isAudioDucked,
        onTogglePlay = { viewModel.togglePlayback() },
        onSeek = { viewModel.seekTo(it) }
      )

      // Action row for timeline navigation & add scene
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Storyboard Scenes (${scenes.size})",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = { viewModel.addScene() },
            modifier = Modifier.testTag("add_scene_button")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Scene", fontSize = 12.sp)
          }

          Button(
            onClick = onNavigateToTimeline,
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier.testTag("open_timeline_button")
          ) {
            Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Timeline Editor", fontSize = 12.sp)
          }
        }
      }

      // Storyboard Scene Cards
      var accumulatedSeconds = 0
      scenes.forEach { scene ->
        val startSec = accumulatedSeconds
        val endSec = accumulatedSeconds + scene.duration
        accumulatedSeconds += scene.duration

        StoryboardCard(
          scene = scene,
          startTimeSeconds = startSec,
          endTimeSeconds = endSec,
          onEdit = { /* Scene inline prompt editing */ },
          onRegenerate = {
            viewModel.updateScene(scene.copy(prompt = "${scene.prompt} (Refined cinematic take)"))
          },
          onDelete = { viewModel.deleteScene(scene) },
          onDuplicate = { viewModel.duplicateScene(scene) }
        )
      }
    }
  }
}

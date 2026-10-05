package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.model.AspectRatioFormat
import com.example.model.VideoCreationMode
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
fun AIVideoDashboardScreen(
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
  val aspectRatio by viewModel.aspectRatio.collectAsStateWithLifecycle()
  val videoLength by viewModel.videoLengthSeconds.collectAsStateWithLifecycle()

  val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
  val currentPos by viewModel.currentPlaybackPositionSeconds.collectAsStateWithLifecycle()
  val activeSceneIdx by viewModel.activeSceneIndex.collectAsStateWithLifecycle()
  val captionText by viewModel.currentCaptionText.collectAsStateWithLifecycle()
  val captionStyle by viewModel.captionStyle.collectAsStateWithLifecycle()
  val isAudioDucked by viewModel.audioPlaybackManager.isAudioDucked.collectAsStateWithLifecycle()
  val scenes by viewModel.currentScenes.collectAsStateWithLifecycle()
  val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()

  var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    if (uri != null) {
      selectedImageUri = uri
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(StudioDark)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 29. Dashboard Headline & Subheading
    Column {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(CyanAccent)
        )
        Text(
          text = "AI VIDEO GENERATOR",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = CyanAccent,
          letterSpacing = 1.sp
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "Turn Your Idea Into a Realistic AI Video",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        color = TextPrimary,
        lineHeight = 30.sp
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "Describe your video and let AI create the script, scenes, visuals, voices, music, subtitles and final edit automatically.",
        fontSize = 13.sp,
        color = TextSecondary,
        lineHeight = 19.sp
      )
    }

    // Engine Model Badges (Veo 3.1 Fast, Lyria 3 Music, Gemini 3.1 Flash Image)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(StudioCardDark)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(Icons.Default.Movie, contentDescription = null, tint = IndigoLight, modifier = Modifier.size(15.dp))
        Text("Veo 3.1 Video Engine", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
      }
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(Icons.Default.MusicNote, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(15.dp))
        Text("Lyria Music", fontSize = 11.sp, color = TextSecondary)
      }
      Text("16:9 / 9:16", fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.SemiBold)
    }

    // 30. Selection List for Different Video Creation Modes
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "VIDEO CREATION MODES",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = TextSecondary,
          letterSpacing = 0.5.sp
        )
        Text(
          text = "${VideoCreationMode.entries.size} Modes",
          fontSize = 11.sp,
          color = CyanAccent,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Horizontal Mode Selection List with visual cards
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.testTag("video_creation_modes_list")
      ) {
        items(VideoCreationMode.entries) { mode ->
          val isSelected = selectedMode == mode
          ModeCard(
            mode = mode,
            isSelected = isSelected,
            onClick = {
              viewModel.selectedMode.value = mode
              // Auto-adjust default aspect ratio for vertical platforms
              if (mode == VideoCreationMode.TIKTOK_REELS) {
                viewModel.aspectRatio.value = AspectRatioFormat.TIKTOK
              } else if (mode == VideoCreationMode.YOUTUBE_VIDEO) {
                viewModel.aspectRatio.value = AspectRatioFormat.YOUTUBE
              }
            }
          )
        }
      }
    }

    // Image-to-Video Photo Picker (If Image to Video mode is active)
    if (selectedMode == VideoCreationMode.IMAGE_TO_VIDEO) {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CyanAccent)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = CyanAccent)
              Text("Animate Photo with Veo", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
            }

            OutlinedButton(
              onClick = {
                photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              },
              modifier = Modifier.testTag("pick_photo_button")
            ) {
              Text(if (selectedImageUri == null) "Select Photo" else "Change")
            }
          }

          if (selectedImageUri != null) {
            Spacer(modifier = Modifier.height(10.dp))
            AsyncImage(
              model = selectedImageUri,
              contentDescription = "Selected photo to animate",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp))
            )
          } else {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Upload a photo to generate realistic video motions using Veo video generations (16:9 or 9:16).",
              fontSize = 12.sp,
              color = TextSecondary
            )
          }
        }
      }
    }

    // Large Input Text Box with placeholder 'Describe the video you want to create...' (#29)
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("VIDEO DESCRIPTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)

        Text(
          text = "Try Example Story",
          fontSize = 11.sp,
          color = CyanAccent,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .clickable {
              viewModel.promptInput.value = "A 30-year-old businessman is walking through Johannesburg at night after losing his job. He receives a phone call that changes his life. Create a realistic cinematic motivational story with emotional dialogue."
              viewModel.isPromptEnhanced.value = false
            }
            .padding(4.dp)
            .testTag("try_example_prompt_button")
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = promptText,
        onValueChange = {
          viewModel.promptInput.value = it
          viewModel.isPromptEnhanced.value = false
        },
        placeholder = {
          Text(
            text = "Describe the video you want to create...",
            color = TextSecondary.copy(alpha = 0.8f),
            fontSize = 14.sp
          )
        },
        minLines = 4,
        maxLines = 7,
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
        shape = RoundedCornerShape(14.dp)
      )
    }

    // 31. AI Prompt Assistant (Enhance Prompt)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OutlinedButton(
        onClick = { viewModel.enhancePrompt() },
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("enhance_prompt_button"),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Enhance Prompt", fontWeight = FontWeight.SemiBold)
      }

      // 'Generate Video' Button (#29)
      Button(
        onClick = { viewModel.generateVideo() },
        enabled = !isGenerating,
        modifier = Modifier
          .weight(1.3f)
          .height(48.dp)
          .testTag("generate_video_button"),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (autoCreate) IndigoPrimary else CyanAccent
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(
          Icons.Default.Movie,
          contentDescription = null,
          tint = if (autoCreate) Color.White else Color.Black,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isGenerating) "Generating..." else "Generate Video",
          color = if (autoCreate) Color.White else Color.Black,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      }
    }

    // AI Enhanced Prompt Card Display (#31)
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

    // Quick Format & Length Controls
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Aspect ratio
      Column(modifier = Modifier.weight(1f)) {
        Text("ASPECT RATIO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
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

      // Video Length
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
          Text("Change", fontSize = 11.sp, color = CyanAccent)
        }
      }
    }

    // Video Output Player & Storyboard (when video is generated)
    if (scenes.isNotEmpty()) {
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "GENERATED VIDEO PREVIEW",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = CyanAccent
        )
        Text(
          text = "${scenes.size} Scenes • ${videoLength}s",
          fontSize = 12.sp,
          color = TextSecondary
        )
      }

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

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Storyboard Scenes",
          fontSize = 15.sp,
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
            Text("Timeline", fontSize = 12.sp)
          }
        }
      }

      var accumulatedSeconds = 0
      scenes.forEach { scene ->
        val startSec = accumulatedSeconds
        val endSec = accumulatedSeconds + scene.duration
        accumulatedSeconds += scene.duration

        StoryboardCard(
          scene = scene,
          startTimeSeconds = startSec,
          endTimeSeconds = endSec,
          onEdit = {},
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

@Composable
private fun ModeCard(
  mode: VideoCreationMode,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val icon: ImageVector = when (mode) {
    VideoCreationMode.TEXT_TO_VIDEO -> Icons.Default.TextFields
    VideoCreationMode.SCRIPT_TO_VIDEO -> Icons.Default.Description
    VideoCreationMode.STORY_TO_VIDEO -> Icons.Default.AutoStories
    VideoCreationMode.IMAGE_TO_VIDEO -> Icons.Default.Image
    VideoCreationMode.TALKING_CHARACTER -> Icons.Default.RecordVoiceOver
    VideoCreationMode.ADVERT_VIDEO -> Icons.Default.Campaign
    VideoCreationMode.EDUCATIONAL_VIDEO -> Icons.Default.School
    VideoCreationMode.YOUTUBE_VIDEO -> Icons.Default.SmartDisplay
    VideoCreationMode.TIKTOK_REELS -> Icons.Default.Videocam
    VideoCreationMode.SHORT_FILM -> Icons.Default.Movie
    VideoCreationMode.DOCUMENTARY -> Icons.Default.HistoryEdu
    VideoCreationMode.PRODUCT_PROMO -> Icons.Default.ShoppingBag
    VideoCreationMode.MOTIVATIONAL -> Icons.Default.Psychology
    VideoCreationMode.NEWS_STYLE -> Icons.Default.Newspaper
    VideoCreationMode.PODCAST_CLIPS -> Icons.Default.Mic
  }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) IndigoPrimary else StudioCardDarkElevated
    ),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) CyanAccent else StudioBorder)
    ),
    modifier = Modifier
      .width(135.dp)
      .height(100.dp)
      .clickable { onClick() }
      .testTag("mode_card_${mode.name.lowercase()}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color.White.copy(alpha = 0.2f) else StudioDark),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = mode.title,
            tint = if (isSelected) Color.White else CyanAccent,
            modifier = Modifier.size(16.dp)
          )
        }

        if (isSelected) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Selected",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Text(
        text = mode.title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) Color.White else TextPrimary,
        maxLines = 2,
        lineHeight = 15.sp
      )
    }
  }
}

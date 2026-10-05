package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CaptionStyle
import com.example.model.MusicCategory
import com.example.ui.components.TimelineView
import com.example.ui.components.VideoPreviewPlayer
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.StudioDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun TimelineEditorScreen(
  viewModel: StudioViewModel,
  onBack: () -> Unit,
  onOpenExport: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  val scenes by viewModel.currentScenes.collectAsStateWithLifecycle()
  val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()
  val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
  val currentPos by viewModel.currentPlaybackPositionSeconds.collectAsStateWithLifecycle()
  val videoLength by viewModel.videoLengthSeconds.collectAsStateWithLifecycle()
  val activeSceneIdx by viewModel.activeSceneIndex.collectAsStateWithLifecycle()
  val captionText by viewModel.currentCaptionText.collectAsStateWithLifecycle()
  val captionStyle by viewModel.captionStyle.collectAsStateWithLifecycle()
  val isAudioDucked by viewModel.audioPlaybackManager.isAudioDucked.collectAsStateWithLifecycle()
  val aspectRatio by viewModel.aspectRatio.collectAsStateWithLifecycle()

  val musicVol by viewModel.musicVolume.collectAsStateWithLifecycle()
  val duckingEnabled by viewModel.enableAudioDucking.collectAsStateWithLifecycle()
  val musicCat by viewModel.musicCategory.collectAsStateWithLifecycle()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(StudioDark)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Screen header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(onClick = onBack) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
        }
        Column {
          Text(
            text = "Video Timeline Editor",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "${scenes.size} Scenes • ${videoLength}s total",
            fontSize = 12.sp,
            color = CyanAccent
          )
        }
      }

      Button(
        onClick = onOpenExport,
        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
        modifier = Modifier.testTag("timeline_export_button")
      ) {
        Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Export", color = Color.Black, fontWeight = FontWeight.Bold)
      }
    }

    // Live Video Player
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

    // Multi-track Timeline View (#50)
    TimelineView(
      scenes = scenes,
      currentPositionSeconds = currentPos,
      totalDurationSeconds = videoLength,
      onSeek = { viewModel.seekTo(it) },
      onExtendScene = { sc ->
        viewModel.updateScene(sc.copy(duration = sc.duration + 4))
        viewModel.videoLengthSeconds.value += 4
      },
      onSplitScene = { sc ->
        val half = (sc.duration / 2).coerceAtLeast(3)
        viewModel.updateScene(sc.copy(duration = half))
        viewModel.duplicateScene(sc.copy(duration = half, prompt = "${sc.prompt} (Part 2)"))
      },
      onDeleteScene = { sc -> viewModel.deleteScene(sc) }
    )

    // Audio Ducking & Music Controls (#42)
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
      border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudioBorder))
    ) {
      Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.MusicNote, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
            Text("Automatic Audio Ducking", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
          }
          Switch(
            checked = duckingEnabled,
            onCheckedChange = { viewModel.enableAudioDucking.value = it }
          )
        }

        Text(
          text = "Reduces background music volume by 75% while narration and character dialogue are speaking.",
          fontSize = 11.sp,
          color = TextSecondary
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Music Volume", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(100.dp))
          Slider(
            value = musicVol,
            onValueChange = { viewModel.musicVolume.value = it },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = IndigoPrimary),
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Captions Customizer (#44)
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
      border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudioBorder))
    ) {
      Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Icon(Icons.Default.Subtitles, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
          Text("Auto Captions Style & Animation", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          CaptionStyle.entries.take(4).forEach { style ->
            FilterChip(
              selected = captionStyle == style,
              onClick = { viewModel.captionStyle.value = style },
              label = { Text(style.label, fontSize = 11.sp) }
            )
          }
        }
      }
    }
  }
}

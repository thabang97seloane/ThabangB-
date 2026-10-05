package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.VideoSceneEntity
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TrackCaptions
import com.example.ui.theme.TrackDialogue
import com.example.ui.theme.TrackMusic
import com.example.ui.theme.TrackSfx
import com.example.ui.theme.TrackVideo
import com.example.ui.theme.TrackVoice

@Composable
fun TimelineView(
  scenes: List<VideoSceneEntity>,
  currentPositionSeconds: Float,
  totalDurationSeconds: Int,
  onSeek: (Float) -> Unit,
  onExtendScene: (VideoSceneEntity) -> Unit,
  onSplitScene: (VideoSceneEntity) -> Unit,
  onDeleteScene: (VideoSceneEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedSceneForAction by remember { mutableStateOf<VideoSceneEntity?>(null) }
  val scrollState = rememberScrollState()
  val pixelsPerSecond = 18.dp

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(StudioCardDark)
      .border(1.dp, StudioBorder, RoundedCornerShape(14.dp))
      .padding(12.dp)
      .testTag("timeline_editor_view")
  ) {
    // Timeline Header with editing action buttons (#50)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(
          imageVector = Icons.Default.Videocam,
          contentDescription = "Timeline",
          tint = CyanAccent,
          modifier = Modifier.size(18.dp)
        )
        Text(
          text = "Multi-Track Timeline Editor",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        AssistChip(
          onClick = {
            selectedSceneForAction?.let { onSplitScene(it) }
          },
          label = { Text("Split", fontSize = 11.sp) },
          leadingIcon = {
            Icon(Icons.Default.CallSplit, contentDescription = "Split", modifier = Modifier.size(13.dp))
          },
          colors = AssistChipDefaults.assistChipColors(containerColor = StudioCardDarkElevated),
          border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        )
        AssistChip(
          onClick = {
            selectedSceneForAction?.let { onExtendScene(it) }
          },
          label = { Text("Extend +4s", fontSize = 11.sp) },
          colors = AssistChipDefaults.assistChipColors(containerColor = StudioCardDarkElevated),
          border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Multi-track view
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(scrollState)
    ) {
      // Left track headers
      Column(
        modifier = Modifier.width(90.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        TrackHeader(label = "Video", icon = Icons.Default.Videocam, color = TrackVideo)
        TrackHeader(label = "Voice-over", icon = Icons.Default.RecordVoiceOver, color = TrackVoice)
        TrackHeader(label = "Dialogue", icon = Icons.Default.GraphicEq, color = TrackDialogue)
        TrackHeader(label = "Music", icon = Icons.Default.MusicNote, color = TrackMusic)
        TrackHeader(label = "Sound FX", icon = Icons.Default.GraphicEq, color = TrackSfx)
        TrackHeader(label = "Captions", icon = Icons.Default.Subtitles, color = TrackCaptions)
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Right scrollable track blocks
      Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Track 1: Video Scene clips
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
          scenes.forEach { scene ->
            val clipWidth = (scene.duration * 18).dp
            val isSelected = selectedSceneForAction?.id == scene.id
            Box(
              modifier = Modifier
                .width(clipWidth)
                .height(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) Color(0xFF60A5FA) else TrackVideo)
                .clickable { selectedSceneForAction = scene },
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                text = "Scene ${scene.sceneNumber} (${scene.duration}s)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
            }
          }
        }

        // Track 2: Voice-Over clips
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
          scenes.forEach { scene ->
            val clipWidth = (scene.duration * 18).dp
            Box(
              modifier = Modifier
                .width(clipWidth)
                .height(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (scene.narration.isNotBlank()) TrackVoice else Color(0x33475569)),
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                text = if (scene.narration.isNotBlank()) "Narration" else "-",
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
            }
          }
        }

        // Track 3: Character Dialogue
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
          scenes.forEach { scene ->
            val clipWidth = (scene.duration * 18).dp
            Box(
              modifier = Modifier
                .width(clipWidth)
                .height(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (scene.dialogue.isNotBlank()) TrackDialogue else Color(0x33475569)),
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                text = if (scene.dialogue.isNotBlank()) "${scene.characterName} Dialogue" else "-",
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
            }
          }
        }

        // Track 4: Music (Continuous with Ducking markers)
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
          scenes.forEach { scene ->
            val clipWidth = (scene.duration * 18).dp
            Box(
              modifier = Modifier
                .width(clipWidth)
                .height(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(TrackMusic),
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                text = "BGM • Ducked",
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
            }
          }
        }

        // Track 5: Sound Effects
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
          scenes.forEach { scene ->
            val clipWidth = (scene.duration * 18).dp
            Box(
              modifier = Modifier
                .width(clipWidth)
                .height(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (scene.soundEffects.isNotBlank()) TrackSfx else Color(0x33475569)),
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                text = if (scene.soundEffects.isNotBlank()) scene.soundEffects.take(14) else "-",
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
            }
          }
        }

        // Track 6: Subtitles / Captions
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
          scenes.forEach { scene ->
            val clipWidth = (scene.duration * 18).dp
            Box(
              modifier = Modifier
                .width(clipWidth)
                .height(30.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(TrackCaptions),
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                text = "Auto Captions",
                fontSize = 10.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun TrackHeader(
  label: String,
  icon: ImageVector,
  color: Color
) {
  Row(
    modifier = Modifier
      .width(90.dp)
      .height(30.dp)
      .clip(RoundedCornerShape(6.dp))
      .background(StudioCardDarkElevated)
      .padding(horizontal = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = color,
      modifier = Modifier.size(13.dp)
    )
    Text(
      text = label,
      fontSize = 10.sp,
      fontWeight = FontWeight.Medium,
      color = TextSecondary,
      maxLines = 1
    )
  }
}

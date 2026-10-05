package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.VideoSceneEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun StoryboardCard(
  scene: VideoSceneEntity,
  startTimeSeconds: Int,
  endTimeSeconds: Int,
  onEdit: () -> Unit,
  onRegenerate: () -> Unit,
  onDelete: () -> Unit,
  onDuplicate: () -> Unit,
  modifier: Modifier = Modifier
) {
  val startFormatted = String.format(Locale.US, "%02d:%02d", startTimeSeconds / 60, startTimeSeconds % 60)
  val endFormatted = String.format(Locale.US, "%02d:%02d", endTimeSeconds / 60, endTimeSeconds % 60)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("storyboard_card_${scene.sceneNumber}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudioBorder))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Scene header with time range
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(IndigoPrimary)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "Scene %02d".format(Locale.US, scene.sceneNumber),
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color.White
            )
          }

          Text(
            text = "$startFormatted – $endFormatted (${scene.duration}s)",
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = CyanAccent,
            fontWeight = FontWeight.SemiBold
          )
        }

        // Camera Shot & Movement Badge
        Text(
          text = "${scene.cameraShot} • ${scene.cameraMovement}",
          fontSize = 11.sp,
          color = TextSecondary,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Visual Prompt Description
      Text(
        text = scene.prompt,
        fontSize = 13.sp,
        color = TextPrimary,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Narration & Dialogue rows
      if (scene.narration.isNotBlank()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x336366F1))
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.RecordVoiceOver,
            contentDescription = "Narration",
            tint = IndigoLight,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "\"${scene.narration}\"",
            fontSize = 12.sp,
            color = Color(0xFFE0E7FF),
            fontWeight = FontWeight.Medium
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
      }

      if (scene.dialogue.isNotBlank()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x33F43F5E))
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = "Dialogue",
            tint = RoseAccent,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = scene.dialogue,
            fontSize = 12.sp,
            color = Color(0xFFFFE4E6),
            fontWeight = FontWeight.SemiBold
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
      }

      // Music & SFX row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (scene.musicPrompt.isNotBlank()) {
          AssistChip(
            onClick = {},
            label = { Text(scene.musicPrompt, fontSize = 10.sp, maxLines = 1) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = AmberAccent,
                modifier = Modifier.size(14.dp)
              )
            },
            colors = AssistChipDefaults.assistChipColors(containerColor = Color(0x22F59E0B)),
            border = null,
            modifier = Modifier.weight(1f, fill = false)
          )
        }
        if (scene.soundEffects.isNotBlank()) {
          AssistChip(
            onClick = {},
            label = { Text(scene.soundEffects, fontSize = 10.sp, maxLines = 1) },
            colors = AssistChipDefaults.assistChipColors(containerColor = Color(0x2206B6D4)),
            border = null,
            modifier = Modifier.weight(1f, fill = false)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Action Buttons: Edit, Regenerate, Delete, Duplicate
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onEdit,
          modifier = Modifier.size(36.dp).testTag("edit_scene_${scene.sceneNumber}")
        ) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Scene",
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
          )
        }
        IconButton(
          onClick = onRegenerate,
          modifier = Modifier.size(36.dp).testTag("regenerate_scene_${scene.sceneNumber}")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Regenerate Scene",
            tint = CyanAccent,
            modifier = Modifier.size(16.dp)
          )
        }
        IconButton(
          onClick = onDuplicate,
          modifier = Modifier.size(36.dp).testTag("duplicate_scene_${scene.sceneNumber}")
        ) {
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Duplicate Scene",
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
          )
        }
        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(36.dp).testTag("delete_scene_${scene.sceneNumber}")
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Scene",
            tint = RoseAccent.copy(alpha = 0.8f),
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

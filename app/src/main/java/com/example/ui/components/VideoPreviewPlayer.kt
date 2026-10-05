package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.VideoSceneEntity
import com.example.model.AspectRatioFormat
import com.example.model.CaptionStyle
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun VideoPreviewPlayer(
  isPlaying: Boolean,
  currentPositionSeconds: Float,
  totalDurationSeconds: Int,
  activeScene: VideoSceneEntity?,
  sceneIndex: Int,
  totalScenes: Int,
  aspectRatio: AspectRatioFormat,
  captionText: String,
  captionStyle: CaptionStyle,
  isAudioDucked: Boolean,
  onTogglePlay: () -> Unit,
  onSeek: (Float) -> Unit,
  modifier: Modifier = Modifier
) {
  val ratioFloat = when (aspectRatio) {
    AspectRatioFormat.YOUTUBE -> 16f / 9f
    AspectRatioFormat.YOUTUBE_SHORTS,
    AspectRatioFormat.TIKTOK,
    AspectRatioFormat.INSTAGRAM_REELS -> 9f / 16f
    AspectRatioFormat.INSTA_POST -> 1f
    AspectRatioFormat.CINEMATIC_ULTRAWIDE -> 21f / 9f
  }

  val infiniteTransition = rememberInfiniteTransition(label = "player_motion")
  val scanAnim by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scan_offset"
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(Color.Black)
      .border(1.dp, StudioBorder, RoundedCornerShape(16.dp))
  ) {
    // Top player info bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xCC0B0F19))
        .padding(horizontal = 14.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (isPlaying) Color(0xFF10B981) else RoseAccent)
        )
        Text(
          text = if (totalScenes > 0) "Scene ${sceneIndex + 1} of $totalScenes • ${activeScene?.cameraShot ?: "Cinematic"}" else "AI Video Preview",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = TextPrimary
        )
      }

      if (isAudioDucked) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x3306B6D4))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = "Audio Ducking",
            tint = CyanAccent,
            modifier = Modifier.size(12.dp)
          )
          Text(
            text = "Audio Ducking",
            fontSize = 10.sp,
            color = CyanAccent,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // Video Canvas Frame
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(230.dp)
        .background(Color(0xFF030712))
        .clickable { onTogglePlay() },
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .aspectRatio(ratioFloat, matchHeightConstraintsFirst = true)
          .clip(RoundedCornerShape(4.dp))
      ) {
        // Simulated photorealistic generative visual scene with lighting reflections
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          val bgGrad = Brush.radialGradient(
            colors = listOf(
              Color(0xFF1E293B),
              Color(0xFF0F172A),
              Color(0xFF020617)
            ),
            center = Offset(w * (0.3f + scanAnim * 0.4f), h * 0.35f),
            radius = w * 0.8f
          )
          drawRect(brush = bgGrad)

          // Horizon & city reflections
          val cityGrad = Brush.verticalGradient(
            colors = listOf(Color(0x00000000), Color(0x66F59E0B), Color(0x3306B6D4), Color(0xFF000000)),
            startY = h * 0.5f,
            endY = h
          )
          drawRect(brush = cityGrad, topLeft = Offset(0f, h * 0.55f))

          // Street reflection wet pavement lines
          for (i in 0..12) {
            val startX = w * (i / 12f)
            val endX = w * 0.5f + (startX - w * 0.5f) * 1.8f
            drawLine(
              color = if (i % 2 == 0) Color(0x22F59E0B) else Color(0x2238BDF8),
              start = Offset(startX, h * 0.65f),
              end = Offset(endX, h),
              strokeWidth = 2f
            )
          }

          // Character silhouette representing realistic consistency
          val charCenterX = w * (0.5f + (scanAnim - 0.5f) * 0.15f)
          val charCenterY = h * 0.58f

          // Head & Shoulders silhouette
          drawCircle(
            color = Color(0xCC090D16),
            radius = h * 0.09f,
            center = Offset(charCenterX, charCenterY - h * 0.16f)
          )

          val torso = Path().apply {
            moveTo(charCenterX - h * 0.18f, charCenterY + h * 0.25f)
            lineTo(charCenterX - h * 0.12f, charCenterY - h * 0.08f)
            lineTo(charCenterX + h * 0.12f, charCenterY - h * 0.08f)
            lineTo(charCenterX + h * 0.18f, charCenterY + h * 0.25f)
            close()
          }
          drawPath(torso, color = Color(0xDD090D16))

          // Cinematic rim light outline
          drawCircle(
            color = Color(0x446366F1),
            radius = h * 0.092f,
            center = Offset(charCenterX, charCenterY - h * 0.16f),
            style = Stroke(width = 2.5f)
          )
        }

        // Overlay Environment & Camera Movement tag
        Row(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(8.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x99000000))
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "${activeScene?.cameraMovement ?: "Tracking"} • ${activeScene?.lighting ?: "Night Neon"}",
            fontSize = 11.sp,
            color = Color(0xFFE2E8F0),
            fontWeight = FontWeight.SemiBold
          )
        }

        // Live Captions / Subtitles Overlay (#44)
        if (captionText.isNotBlank()) {
          Box(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(horizontal = 16.dp, vertical = 12.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(captionStyle.defaultBg))
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(
              text = captionText,
              color = Color(captionStyle.defaultColor),
              fontSize = when (captionStyle) {
                CaptionStyle.TIKTOK, CaptionStyle.BOLD -> 15.sp
                CaptionStyle.MINIMAL -> 12.sp
                else -> 14.sp
              },
              fontWeight = when (captionStyle) {
                CaptionStyle.BOLD, CaptionStyle.TIKTOK -> FontWeight.ExtraBold
                CaptionStyle.PROFESSIONAL -> FontWeight.SemiBold
                else -> FontWeight.Bold
              },
              textAlign = TextAlign.Center,
              modifier = Modifier.testTag("video_player_caption_text")
            )
          }
        }

        // Play/Pause Center Indicator when paused
        if (!isPlaying) {
          Box(
            modifier = Modifier
              .align(Alignment.Center)
              .size(54.dp)
              .clip(CircleShape)
              .background(Color(0xAA000000))
              .border(1.5.dp, IndigoPrimary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = "Play",
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }
        }
      }
    }

    // Playback Controller Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF0F172A))
        .padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onTogglePlay,
        modifier = Modifier
          .size(40.dp)
          .testTag("play_pause_button")
      ) {
        Icon(
          imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
          contentDescription = if (isPlaying) "Pause" else "Play",
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }

      val minutes = (currentPositionSeconds.toInt()) / 60
      val seconds = (currentPositionSeconds.toInt()) % 60
      val totalMinutes = totalDurationSeconds / 60
      val totalSecs = totalDurationSeconds % 60
      val timeText = String.format(Locale.US, "%02d:%02d / %02d:%02d", minutes, seconds, totalMinutes, totalSecs)

      Text(
        text = timeText,
        fontSize = 12.sp,
        fontFamily = FontFamily.Monospace,
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = 6.dp)
      )

      Slider(
        value = currentPositionSeconds,
        onValueChange = { onSeek(it) },
        valueRange = 0f..totalDurationSeconds.toFloat().coerceAtLeast(1f),
        colors = SliderDefaults.colors(
          thumbColor = CyanAccent,
          activeTrackColor = IndigoPrimary,
          inactiveTrackColor = StudioBorder
        ),
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 6.dp)
          .testTag("video_timeline_slider")
      )
    }
  }
}

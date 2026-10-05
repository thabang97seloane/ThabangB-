package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.StudioDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun ThumbnailStudioScreen(
  viewModel: StudioViewModel,
  onOpenYouTubePackage: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  val title by viewModel.thumbTitle.collectAsStateWithLifecycle()
  val subtitle by viewModel.thumbSubtitle.collectAsStateWithLifecycle()
  val badgeText by viewModel.thumbBadge.collectAsStateWithLifecycle()
  val layout by viewModel.thumbLayout.collectAsStateWithLifecycle()
  val ctrScore by viewModel.thumbScore.collectAsStateWithLifecycle()

  val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()
  val characters by viewModel.currentCharacters.collectAsStateWithLifecycle()
  val mainCharName = characters.firstOrNull()?.characterName ?: "Thabo"

  val templates = listOf(
    "Split Screen Hero",
    "Extreme Close-Up",
    "Dramatic Glow Frame",
    "Before & After Story",
    "Tech Minimalist"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(StudioDark)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "AI YouTube Thumbnail Studio",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "Generate high-CTR thumbnails matched to your video story",
          fontSize = 12.sp,
          color = TextSecondary
        )
      }

      Button(
        onClick = onOpenYouTubePackage,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
        modifier = Modifier.testTag("open_yt_package_button")
      ) {
        Icon(Icons.Default.SmartDisplay, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("YT Package")
      }
    }

    // 16:9 YouTube Thumbnail Canvas
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.Black),
      border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudioBorder)),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("youtube_thumbnail_canvas")
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
      ) {
        // Generative art canvas simulation
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          val bg = Brush.horizontalGradient(
            colors = listOf(
              Color(0xFF0F172A),
              Color(0xFF1E1B4B),
              Color(0xFF881337)
            )
          )
          drawRect(brush = bg)

          // Dramatic spotlight
          val spot = Brush.radialGradient(
            colors = listOf(Color(0x99F59E0B), Color(0x00000000)),
            center = Offset(w * 0.75f, h * 0.45f),
            radius = w * 0.45f
          )
          drawRect(brush = spot)

          // Character cutout silhouette on the right side
          val charX = w * 0.72f
          val charY = h * 0.52f
          drawCircle(
            color = Color(0xEE090D16),
            radius = h * 0.28f,
            center = Offset(charX, charY - h * 0.22f)
          )

          val torso = Path().apply {
            moveTo(charX - h * 0.35f, h)
            lineTo(charX - h * 0.25f, charY + h * 0.05f)
            lineTo(charX + h * 0.25f, charY + h * 0.05f)
            lineTo(charX + h * 0.35f, h)
            close()
          }
          drawPath(torso, color = Color(0xEE090D16))
        }

        // Left Side Bold High-CTR Text Overlays
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // Top Yellow/Red Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFFEF4444))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = badgeText,
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }

          // Main Big Hook Text
          Column(modifier = Modifier.width(220.dp)) {
            Box(
              modifier = Modifier
                .background(Color(0xFFFFE600))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = title,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 20.sp
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
              modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "$mainCharName's True Story",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // Bottom timecode badge
          Text(
            text = "4K • 1080p Ultra HD",
            color = Color(0xFFE2E8F0),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // AI CTR Predictor Score (#55)
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
      border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudioBorder))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(Color(0x3310B981)),
            contentAlignment = Alignment.Center
          ) {
            Text("$ctrScore", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color(0xFF10B981))
          }
          Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Text("AI CTR Score: Excellent", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
              Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
            }
            Text(
              text = "High facial emotion contrast and bold yellow keywords maximize click rate",
              fontSize = 11.sp,
              color = TextSecondary
            )
          }
        }
      }
    }

    // Layout Templates Picker
    Text("VIRAL THUMBNAIL TEMPLATES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
    val tmplScroll = rememberScrollState()
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(tmplScroll),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      templates.forEach { tmpl ->
        val isSelected = layout == tmpl
        FilterChip(
          selected = isSelected,
          onClick = { viewModel.thumbLayout.value = tmpl },
          label = { Text(tmpl, fontSize = 12.sp) }
        )
      }
    }

    // Text Customizer Fields
    OutlinedTextField(
      value = title,
      onValueChange = { viewModel.thumbTitle.value = it },
      label = { Text("Primary Hook Text (Big Impact)") },
      modifier = Modifier.fillMaxWidth().testTag("thumb_title_field")
    )

    OutlinedTextField(
      value = badgeText,
      onValueChange = { viewModel.thumbBadge.value = it },
      label = { Text("Alert Badge (Top Tag)") },
      modifier = Modifier.fillMaxWidth().testTag("thumb_badge_field")
    )

    // Action Buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OutlinedButton(
        onClick = {
          viewModel.thumbTitle.value = if (activeProject != null) "HE WON AGAINST ALL ODDS" else "THE TURNING POINT"
          viewModel.thumbScore.value = 96
          Toast.makeText(context, "AI re-analyzed video moments and regenerated thumbnail!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.weight(1f).testTag("regenerate_thumb_button")
      ) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("AI Regenerate", fontSize = 12.sp)
      }

      Button(
        onClick = {
          Toast.makeText(context, "Thumbnail saved to gallery in 1920x1080 Full HD!", Toast.LENGTH_SHORT).show()
        },
        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
        modifier = Modifier.weight(1f).testTag("download_thumb_button")
      ) {
        Icon(Icons.Default.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Download", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }
    }
  }
}

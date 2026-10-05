package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark

@Composable
fun TopStudioAppBar(
  currentTitle: String,
  onOpenYouTubePackage: () -> Unit,
  onExportClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(StudioCardDark)
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.linearGradient(listOf(IndigoPrimary, CyanAccent, RoseAccent))
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = "Studio Logo",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Thumbnail Studio",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White
          )
          Text(
            text = " AI",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            color = CyanAccent
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        AssistChip(
          onClick = onOpenYouTubePackage,
          label = { Text("YT Package", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.SmartDisplay,
              contentDescription = "YouTube Package",
              modifier = Modifier.size(16.dp),
              tint = RoseAccent
            )
          },
          colors = AssistChipDefaults.assistChipColors(
            containerColor = Color(0x33EF4444),
            labelColor = Color.White
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x66EF4444)),
          modifier = Modifier.testTag("open_youtube_package_chip")
        )

        IconButton(
          onClick = onExportClick,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(StudioBorder)
            .testTag("top_bar_export_button")
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Export Video",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

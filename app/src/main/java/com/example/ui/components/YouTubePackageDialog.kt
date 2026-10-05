package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.ai.YouTubePackageResult
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun YouTubePackageDialog(
  packageResult: YouTubePackageResult?,
  onDismiss: () -> Unit,
  onOpenThumbnailStudio: () -> Unit
) {
  if (packageResult == null) return
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = StudioCardDark,
      border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("youtube_package_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(scrollState)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
              imageVector = Icons.Default.SmartDisplay,
              contentDescription = "YouTube",
              tint = RoseAccent,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "AI YouTube Package",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          IconButton(
            onClick = {
              val fullText = "${packageResult.titleIdeas.joinToString("\n")}\n\n${packageResult.description}\n\nTags:\n${packageResult.tags.joinToString(", ")}"
              val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              cm.setPrimaryClip(ClipData.newPlainText("YouTube Package", fullText))
              Toast.makeText(context, "Full package copied to clipboard!", Toast.LENGTH_SHORT).show()
            }
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy all",
              tint = CyanAccent,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // High CTR Title Ideas
        Text(
          text = "HIGH-CTR TITLE OPTIONS:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = CyanAccent
        )
        Spacer(modifier = Modifier.height(6.dp))
        packageResult.titleIdeas.forEachIndexed { i, title ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(StudioCardDarkElevated)
              .padding(10.dp)
          ) {
            Text(
              text = "${i + 1}. $title",
              fontSize = 13.sp,
              color = TextPrimary,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chapters / Timestamps
        Text(
          text = "CHAPTERS & TIMESTAMPS:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = CyanAccent
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudioCardDarkElevated)
            .padding(10.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            packageResult.chapters.forEach { chapter ->
              Text(
                text = chapter,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tags & Hashtags
        Text(
          text = "TAGS & KEYWORDS:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = CyanAccent
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = packageResult.tags.joinToString(", "),
          fontSize = 12.sp,
          color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text("Close")
          }
          Button(
            onClick = {
              onDismiss()
              onOpenThumbnailStudio()
            },
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
            modifier = Modifier.weight(1.3f)
          ) {
            Text("Open Thumbnail Studio")
          }
        }
      }
    }
  }
}

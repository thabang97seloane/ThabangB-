package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ExportQuality
import com.example.model.ExportResolution
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ExportBottomSheet(
  isVisible: Boolean,
  onDismiss: () -> Unit,
  onExportConfirmed: (format: String, res: ExportResolution, quality: ExportQuality) -> Unit
) {
  if (!isVisible) return

  var selectedFormat by remember { mutableStateOf("MP4") }
  var selectedResolution by remember { mutableStateOf(ExportResolution.FHD_1080P) }
  var selectedQuality by remember { mutableStateOf(ExportQuality.HIGH) }
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = StudioCardDark,
      border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("export_video_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.FileDownload,
            contentDescription = "Export",
            tint = CyanAccent,
            modifier = Modifier.size(24.dp)
          )
          Text(
            text = "Export & Download Video",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Format selection: MP4 / WebM (#53)
        Text("EXPORT FORMAT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("MP4", "WebM").forEach { fmt ->
            val isSelected = selectedFormat == fmt
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) IndigoPrimary else StudioCardDarkElevated)
                .border(1.dp, if (isSelected) CyanAccent else StudioBorder, RoundedCornerShape(8.dp))
                .clickable { selectedFormat = fmt }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = fmt,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Resolution: 720p, 1080p, 4K (#53)
        Text("RESOLUTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ExportResolution.entries.forEach { res ->
            val isSelected = selectedResolution == res
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) IndigoPrimary else StudioCardDarkElevated)
                .border(1.dp, if (isSelected) CyanAccent else StudioBorder, RoundedCornerShape(8.dp))
                .clickable { selectedResolution = res }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = res.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quality: Standard, High, Maximum
        Text("ENCODING QUALITY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ExportQuality.entries.forEach { q ->
            val isSelected = selectedQuality == q
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) IndigoPrimary else StudioCardDarkElevated)
                .border(1.dp, if (isSelected) CyanAccent else StudioBorder, RoundedCornerShape(8.dp))
                .clickable { selectedQuality = q }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = q.label.split(" ").first(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text("Cancel")
          }
          Button(
            onClick = {
              onExportConfirmed(selectedFormat, selectedResolution, selectedQuality)
              Toast.makeText(context, "Rendering $selectedFormat (${selectedResolution.label}). Download ready!", Toast.LENGTH_LONG).show()
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
            modifier = Modifier.weight(1.3f).testTag("confirm_export_button")
          ) {
            Icon(Icons.Default.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text("Download", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

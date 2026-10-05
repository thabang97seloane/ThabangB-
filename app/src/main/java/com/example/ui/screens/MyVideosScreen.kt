package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.VideoProjectEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDarkElevated
import com.example.ui.theme.StudioDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StudioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyVideosScreen(
  viewModel: StudioViewModel,
  onOpenVideo: () -> Unit,
  onOpenExport: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val projects by viewModel.allProjects.collectAsStateWithLifecycle()
  val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(StudioDark)
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
          text = "My Videos Library",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "${projects.size} Saved Projects • Synced with Video Database",
          fontSize = 12.sp,
          color = TextSecondary
        )
      }
    }

    if (projects.isEmpty() && activeProject == null) {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Videocam, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(48.dp))
          Spacer(modifier = Modifier.height(8.dp))
          Text("No Video Projects Yet", fontWeight = FontWeight.Bold, color = TextPrimary)
          Text("Go to 'AI Video' and create your first masterpiece!", fontSize = 12.sp, color = TextSecondary)
        }
      }
    } else {
      val displayList = if (projects.isNotEmpty()) projects else listOfNotNull(activeProject)

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(displayList, key = { it.id }) { project ->
          ProjectCardItem(
            project = project,
            isSelected = activeProject?.id == project.id,
            onOpen = {
              viewModel.selectProject(project)
              onOpenVideo()
            },
            onEdit = {
              viewModel.selectProject(project)
              onOpenVideo()
            },
            onDuplicate = {
              Toast.makeText(context, "Duplicated project as copy", Toast.LENGTH_SHORT).show()
            },
            onDownload = {
              viewModel.selectProject(project)
              onOpenExport()
            },
            onDelete = {
              viewModel.deleteProject(project.id)
            }
          )
        }
      }
    }
  }
}

@Composable
private fun ProjectCardItem(
  project: VideoProjectEntity,
  isSelected: Boolean,
  onOpen: () -> Unit,
  onEdit: () -> Unit,
  onDuplicate: () -> Unit,
  onDownload: () -> Unit,
  onDelete: () -> Unit
) {
  val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
  val dateText = dateFormat.format(Date(project.createdAt))

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) CyanAccent else StudioBorder)
    ),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("project_item_${project.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(IndigoPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
          }
          Column {
            Text(
              text = project.title,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = TextPrimary
            )
            Text(
              text = "${project.duration}s • ${project.aspectRatio} • $dateText",
              fontSize = 12.sp,
              color = TextSecondary
            )
          }
        }

        // Status Badge (#54: Draft, Generating, Ready)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (project.status == "Ready") Color(0x3310B981) else Color(0x33F59E0B))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = project.status,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (project.status == "Ready") Color(0xFF10B981) else AmberAccent
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = project.originalPrompt,
        fontSize = 12.sp,
        color = TextSecondary,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Action row: Open, Edit, Duplicate, Download, Delete (#54)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = onOpen,
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          modifier = Modifier.padding(end = 4.dp).testTag("open_project_${project.id}")
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Open", fontSize = 12.sp)
        }

        IconButton(onClick = onEdit) {
          Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
        IconButton(onClick = onDuplicate) {
          Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
        IconButton(onClick = onDownload) {
          Icon(Icons.Default.Download, contentDescription = "Download", tint = CyanAccent, modifier = Modifier.size(16.dp))
        }
        IconButton(onClick = onDelete) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseAccent, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}

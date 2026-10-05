package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ExportBottomSheet
import com.example.ui.components.GenerationProgressDialog
import com.example.ui.components.TopStudioAppBar
import com.example.ui.components.YouTubePackageDialog
import com.example.ui.screens.AIVideoDashboardScreen
import com.example.ui.screens.CharacterStudioScreen
import com.example.ui.screens.MyVideosScreen
import com.example.ui.screens.ThumbnailStudioScreen
import com.example.ui.screens.TimelineEditorScreen
import com.example.ui.screens.VideoGeneratorScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VideoStudioTheme
import com.example.ui.viewmodel.StudioViewModel

enum class StudioTab(val label: String, val icon: ImageVector, val tag: String) {
  AI_VIDEO("AI Video", Icons.Default.Movie, "nav_ai_video"),
  TIMELINE("Timeline", Icons.Default.Videocam, "nav_timeline"),
  CHARACTERS("Characters", Icons.Default.Face, "nav_characters"),
  THUMBNAILS("Thumbnails", Icons.Default.SmartDisplay, "nav_thumbnails"),
  MY_VIDEOS("My Videos", Icons.Default.VideoLibrary, "nav_my_videos")
}

class MainActivity : ComponentActivity() {
  private val viewModel: StudioViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      VideoStudioTheme {
        var currentTab by remember { mutableStateOf(StudioTab.AI_VIDEO) }
        var showExportDialog by remember { mutableStateOf(false) }
        var showYouTubePackageDialog by remember { mutableStateOf(false) }

        val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()
        val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
        val genStep by viewModel.currentGenStep.collectAsStateWithLifecycle()
        val genProgress by viewModel.genProgress.collectAsStateWithLifecycle()
        val completedSteps by viewModel.completedSteps.collectAsStateWithLifecycle()
        val ytPackage by viewModel.youTubePackage.collectAsStateWithLifecycle()

        // Hardware Back button handling
        if (currentTab != StudioTab.AI_VIDEO) {
          BackHandler {
            currentTab = StudioTab.AI_VIDEO
          }
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            TopStudioAppBar(
              currentTitle = activeProject?.title ?: "Thumbnail Studio AI",
              onOpenYouTubePackage = { showYouTubePackageDialog = true },
              onExportClick = { showExportDialog = true }
            )
          },
          bottomBar = {
            NavigationBar(
              containerColor = StudioCardDark,
              tonalElevation = 8.dp
            ) {
              StudioTab.entries.forEach { tab ->
                val isSelected = currentTab == tab
                NavigationBarItem(
                  selected = isSelected,
                  onClick = { currentTab = tab },
                  icon = {
                    Icon(
                      imageVector = tab.icon,
                      contentDescription = tab.label,
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  label = {
                    Text(
                      text = tab.label,
                      fontSize = 11.sp,
                      color = if (isSelected) CyanAccent else TextSecondary
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CyanAccent,
                    unselectedIconColor = TextSecondary,
                    indicatorColor = Color(0x336366F1)
                  ),
                  modifier = Modifier.testTag(tab.tag)
                )
              }
            }
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            when (currentTab) {
              StudioTab.AI_VIDEO -> {
                AIVideoDashboardScreen(
                  viewModel = viewModel,
                  onNavigateToTimeline = { currentTab = StudioTab.TIMELINE }
                )
              }
              StudioTab.TIMELINE -> {
                TimelineEditorScreen(
                  viewModel = viewModel,
                  onBack = { currentTab = StudioTab.AI_VIDEO },
                  onOpenExport = { showExportDialog = true }
                )
              }
              StudioTab.CHARACTERS -> {
                CharacterStudioScreen(viewModel = viewModel)
              }
              StudioTab.THUMBNAILS -> {
                ThumbnailStudioScreen(
                  viewModel = viewModel,
                  onOpenYouTubePackage = { showYouTubePackageDialog = true }
                )
              }
              StudioTab.MY_VIDEOS -> {
                MyVideosScreen(
                  viewModel = viewModel,
                  onOpenVideo = { currentTab = StudioTab.AI_VIDEO },
                  onOpenExport = { showExportDialog = true }
                )
              }
            }

            // Async Video Job Progress Dialog (#58)
            GenerationProgressDialog(
              isVisible = isGenerating,
              currentStep = genStep,
              progress = genProgress,
              completedSteps = completedSteps
            )

            // YouTube Package Dialog (#55)
            YouTubePackageDialog(
              packageResult = ytPackage,
              onDismiss = { showYouTubePackageDialog = false },
              onOpenThumbnailStudio = { currentTab = StudioTab.THUMBNAILS }
            )

            // Video Export Dialog (#53)
            ExportBottomSheet(
              isVisible = showExportDialog,
              onDismiss = { showExportDialog = false },
              onExportConfirmed = { format, res, quality ->
                // Handled in export bottom sheet with toast notification
              }
            )
          }
        }
      }
    }
  }
}

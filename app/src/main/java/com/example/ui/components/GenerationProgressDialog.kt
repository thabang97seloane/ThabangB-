package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GenerationStep
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GenerationProgressDialog(
  isVisible: Boolean,
  currentStep: GenerationStep,
  progress: Float,
  completedSteps: Set<GenerationStep>
) {
  if (!isVisible) return

  val allDisplaySteps = listOf(
    GenerationStep.PREPARING_SCRIPT,
    GenerationStep.CREATING_CHARACTERS,
    GenerationStep.CREATING_STORYBOARD,
    GenerationStep.GENERATING_SCENES,
    GenerationStep.GENERATING_VOICE,
    GenerationStep.ADDING_MUSIC,
    GenerationStep.ADDING_SFX,
    GenerationStep.ADDING_CAPTIONS,
    GenerationStep.FINAL_RENDER
  )

  Dialog(onDismissRequest = { /* Modal during generation */ }) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = StudioCardDark,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("generation_progress_dialog")
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(36.dp),
            color = CyanAccent,
            trackColor = StudioBorder
          )
          Column {
            Text(
              text = "Creating Your Video",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "${(progress * 100).toInt()}% • Multi-stage AI Pipeline",
              fontSize = 13.sp,
              color = CyanAccent
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = IndigoPrimary,
          trackColor = StudioBorder
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          allDisplaySteps.forEach { step ->
            val isDone = completedSteps.contains(step)
            val isCurrent = currentStep == step

            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              if (isDone) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Completed",
                  tint = Color(0xFF10B981),
                  modifier = Modifier.size(20.dp)
                )
              } else if (isCurrent) {
                CircularProgressIndicator(
                  modifier = Modifier.size(18.dp),
                  strokeWidth = 2.dp,
                  color = CyanAccent
                )
              } else {
                Icon(
                  imageVector = Icons.Default.RadioButtonUnchecked,
                  contentDescription = "Waiting",
                  tint = TextSecondary.copy(alpha = 0.4f),
                  modifier = Modifier.size(18.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Text(
                text = step.title,
                fontSize = 14.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = when {
                  isDone -> TextPrimary
                  isCurrent -> CyanAccent
                  else -> TextSecondary.copy(alpha = 0.6f)
                }
              )

              Spacer(modifier = Modifier.weight(1f))

              Text(
                text = when {
                  isDone -> "✓"
                  isCurrent -> "Generating..."
                  else -> "Waiting"
                },
                fontSize = 12.sp,
                color = when {
                  isDone -> Color(0xFF10B981)
                  isCurrent -> CyanAccent
                  else -> TextSecondary.copy(alpha = 0.5f)
                }
              )
            }
          }
        }
      }
    }
  }
}

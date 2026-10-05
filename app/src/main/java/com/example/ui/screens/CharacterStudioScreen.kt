package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.CharacterEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoLight
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
fun CharacterStudioScreen(
  viewModel: StudioViewModel,
  modifier: Modifier = Modifier
) {
  val characters by viewModel.currentCharacters.collectAsStateWithLifecycle()
  val savedCharacters by viewModel.savedCharacters.collectAsStateWithLifecycle()
  val lockGlobal by viewModel.lockCharacterAppearance.collectAsStateWithLifecycle()

  var selectedAngleTab by remember { mutableIntStateOf(0) }
  val angleTabs = listOf("Portrait", "Front View", "Side View", "Full-Body")
  var showNewCharacterDialog by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

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
          text = "Character Consistency Studio",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "Preserve facial structure, clothing & identity across scenes",
          fontSize = 12.sp,
          color = TextSecondary
        )
      }

      Button(
        onClick = { showNewCharacterDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        modifier = Modifier.testTag("create_character_button")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Create")
      }
    }

    // Global Lock Appearance Option (#35)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(StudioCardDarkElevated)
        .border(1.dp, if (lockGlobal) CyanAccent else StudioBorder, RoundedCornerShape(12.dp))
        .padding(horizontal = 14.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(if (lockGlobal) CyanAccent else StudioBorder),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (lockGlobal) Icons.Default.Lock else Icons.Default.LockOpen,
            contentDescription = null,
            tint = if (lockGlobal) Color.Black else TextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }
        Column {
          Text("Lock Character Appearance", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Text("Prevents clothing and face morphing between scenes", fontSize = 11.sp, color = TextSecondary)
        }
      }

      Switch(
        checked = lockGlobal,
        onCheckedChange = { viewModel.lockCharacterAppearance.value = it },
        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = IndigoPrimary),
        modifier = Modifier.testTag("lock_character_appearance_switch")
      )
    }

    // Reference Angles Bar (#36: Front view, Side view, Portrait, Full-body)
    Text("REFERENCE ANGLE PREVIEWS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
    TabRow(
      selectedTabIndex = selectedAngleTab,
      containerColor = StudioCardDarkElevated,
      contentColor = CyanAccent,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedAngleTab]),
          color = CyanAccent
        )
      },
      modifier = Modifier.clip(RoundedCornerShape(10.dp))
    ) {
      angleTabs.forEachIndexed { index, tabTitle ->
        Tab(
          selected = selectedAngleTab == index,
          onClick = { selectedAngleTab = index },
          text = { Text(tabTitle, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
        )
      }
    }

    // Active Characters list
    val allChars = if (characters.isNotEmpty()) characters else savedCharacters
    if (allChars.isEmpty()) {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(48.dp))
          Spacer(modifier = Modifier.height(8.dp))
          Text("No Character Profiles Yet", fontWeight = FontWeight.Bold, color = TextPrimary)
          Text("Generate a video or tap 'Create' to define a recurring character.", fontSize = 12.sp, color = TextSecondary)
        }
      }
    } else {
      allChars.forEach { character ->
        CharacterProfileCard(
          character = character,
          selectedAngle = angleTabs[selectedAngleTab],
          onToggleLock = { viewModel.toggleCharacterLock(character) },
          onPreviewVoice = {
            viewModel.previewVoice(
              sampleText = "${character.characterName} here. Let's make something exceptional."
            )
          }
        )
      }
    }
  }
}

@Composable
fun CharacterProfileCard(
  character: CharacterEntity,
  selectedAngle: String,
  onToggleLock: () -> Unit,
  onPreviewVoice: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = StudioCardDarkElevated),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StudioBorder)),
    modifier = Modifier.fillMaxWidth().testTag("character_card_${character.characterName}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(IndigoPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Face, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
          }
          Column {
            Text(
              text = character.characterName,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "${character.age} y/o • ${character.gender} • ${character.accent}",
              fontSize = 12.sp,
              color = CyanAccent
            )
          }
        }

        IconButton(onClick = onToggleLock) {
          Icon(
            imageVector = if (character.locked) Icons.Default.Lock else Icons.Default.LockOpen,
            contentDescription = "Lock Character Appearance",
            tint = if (character.locked) CyanAccent else TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Angle view indicator
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0x3306B6D4))
          .padding(8.dp)
      ) {
        Text(
          text = "Visual Framing: $selectedAngle • Consistency Model Locked",
          fontSize = 11.sp,
          color = CyanAccent,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Trait details
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        TraitRow("Skin Tone", character.skinTone)
        TraitRow("Facial Structure", character.facialStructure)
        TraitRow("Hair & Style", "${character.hair}, ${character.hairstyle}")
        TraitRow("Clothing", character.clothing)
        TraitRow("Accessories", character.accessories)
        TraitRow("Assigned Voice", "${character.voiceId} (${character.accent})")
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Voice preview button (#38)
      OutlinedButton(
        onClick = onPreviewVoice,
        modifier = Modifier.fillMaxWidth().testTag("preview_char_voice_${character.characterName}")
      ) {
        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = IndigoLight, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Preview Character Voice", fontSize = 12.sp)
      }
    }
  }
}

@Composable
private fun TraitRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, fontSize = 11.sp, color = TextSecondary)
    Text(text = value, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
  }
}

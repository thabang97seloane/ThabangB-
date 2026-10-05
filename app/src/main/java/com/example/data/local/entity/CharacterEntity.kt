package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  @ColumnInfo(name = "user_id")
  val userId: String = "creator_user",
  @ColumnInfo(name = "project_id")
  val projectId: Long = 0,
  @ColumnInfo(name = "character_name")
  val characterName: String,
  @ColumnInfo(name = "character_description")
  val characterDescription: String,
  val age: Int = 30,
  val gender: String = "Male",
  @ColumnInfo(name = "skin_tone")
  val skinTone: String = "Medium-dark",
  @ColumnInfo(name = "facial_structure")
  val facialStructure: String = "Oval face with short beard",
  @ColumnInfo(name = "eye_colour")
  val eyeColour: String = "Dark brown",
  val hair: String = "Black",
  val hairstyle: String = "Short fade haircut",
  val height: String = "180 cm",
  @ColumnInfo(name = "body_type")
  val bodyType: String = "Athletic",
  val clothing: String = "Black jacket, white T-shirt, dark jeans",
  val accessories: String = "Leather wristband",
  @ColumnInfo(name = "voice_id")
  val voiceId: String = "Deep Male",
  val accent: String = "South African English",
  @ColumnInfo(name = "reference_image")
  val referenceImage: String = "",
  val locked: Boolean = true
)

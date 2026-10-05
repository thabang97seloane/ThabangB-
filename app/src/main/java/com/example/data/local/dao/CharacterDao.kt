package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
  @Query("SELECT * FROM characters ORDER BY id DESC")
  fun getAllCharacters(): Flow<List<CharacterEntity>>

  @Query("SELECT * FROM characters WHERE project_id = :projectId OR project_id = 0")
  fun getCharactersForProject(projectId: Long): Flow<List<CharacterEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCharacter(character: CharacterEntity): Long

  @Update
  suspend fun updateCharacter(character: CharacterEntity)

  @Query("DELETE FROM characters WHERE id = :id")
  suspend fun deleteCharacterById(id: Long)
}

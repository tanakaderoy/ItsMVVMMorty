package com.tanaka.mazivanhanga.itsmvvmmorty.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterCacheEntity: CharacterCacheEntity): Long

    @Query(value = "SELECT * FROM CHARACTERS")
    suspend fun getCharacters(): List<CharacterCacheEntity>

    @Query(value = "SELECT * FROM CHARACTERS WHERE name LIKE :name")
    suspend fun getFilteredCharacters(name: String): List<CharacterCacheEntity>

    @Query(value = "SELECT * FROM CHARACTERS WHERE id = :id")
    suspend fun getCharacter(id: Int): CharacterCacheEntity
}
package com.tanaka.mazivanhanga.itsmvvmmorty.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanaka.mazivanhanga.itsmvvmmorty.util.CustomTypeConverters


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */

@Database(entities = [CharacterCacheEntity::class], version = 1)
@TypeConverters(CustomTypeConverters::class)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        val DATA_BASE_NAME: String = "character_db"
    }
}
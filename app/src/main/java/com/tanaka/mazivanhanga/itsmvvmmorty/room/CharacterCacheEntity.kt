package com.tanaka.mazivanhanga.itsmvvmmorty.room

import androidx.room.*
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Location


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
@Entity(tableName = "characters")
data class CharacterCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "species")
    val species: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "gender")
    val gender: String,

    @ColumnInfo(name = "origin")
    val origin: Location,

    @ColumnInfo(name = "location")
    val location: Location,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "episode")   
    val episode: List<String>,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "created")
    val created: String
) {
}
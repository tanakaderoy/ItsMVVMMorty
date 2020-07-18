package com.tanaka.mazivanhanga.itsmvvmmorty.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Location
import java.util.*


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class CustomTypeConverters {
    var gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType =
            object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return gson.toJson(location)
    }

    @TypeConverter
    fun toLocation(value: String?): Location? {
        return gson.fromJson(value, Location::class.java)
    }
}
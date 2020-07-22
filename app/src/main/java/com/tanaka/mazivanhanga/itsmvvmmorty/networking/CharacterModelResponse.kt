package com.tanaka.mazivanhanga.itsmvvmmorty.networking

import com.tanaka.mazivanhanga.itsmvvmmorty.model.Location

data class CharacterModelResponse(
    val info: Info,
    val results: List<Result>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String? = null
)

data class Result(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Location,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)


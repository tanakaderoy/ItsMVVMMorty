package com.tanaka.mazivanhanga.itsmvvmmorty.model

data class CharacterModelResponse (
    val info: Info,
    val results: List<Result>
)

data class Info (
    val count: Long,
    val pages: Long,
    val next: String,
    val prev: Any? = null
)

data class Result (
    val id: Long,
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

data class Location (
    val name: String,
    val url: String
)
package com.tanaka.mazivanhanga.itsmvvmmorty.networking

import io.reactivex.Observable
import retrofit2.http.GET


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
interface ApiService {
    @GET("/character")
    suspend fun getCharacters(): CharacterModelResponse
}
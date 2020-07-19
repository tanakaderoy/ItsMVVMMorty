package com.tanaka.mazivanhanga.itsmvvmmorty.networking

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
interface ApiService {
    @GET("character/")
    suspend fun getCharacters(@Query("page") page: Int): CharacterModelResponse

    @GET("character/")
    fun getCharacterObservable(@Query("page") page:Int): Single<Response<CharacterModelResponse>>
}
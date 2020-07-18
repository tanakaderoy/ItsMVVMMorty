package com.tanaka.mazivanhanga.itsmvvmmorty.repository

import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import com.tanaka.mazivanhanga.itsmvvmmorty.networking.ApiService
import com.tanaka.mazivanhanga.itsmvvmmorty.networking.NetworkMapper
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CacheMapper
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CharacterDao
import com.tanaka.mazivanhanga.itsmvvmmorty.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class MainRepository constructor(
    private val characterDao: CharacterDao,
    private val apiService: ApiService,
    private val characterCacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {
    suspend fun getCharacter(page: Int): Flow<DataState<List<Character>>> = flow {
        emit(DataState.Loading)
        try {
            val networkCharacterResponse = apiService.getCharacters(page)
            val characters = networkMapper.mapFromEntity(networkCharacterResponse)
            characters.forEach {
                characterDao.insert(characterCacheMapper.mapToEntity(it))
            }
            val cachedCharacters = characterDao.getCharacters()
            emit(DataState.Success(characterCacheMapper.mapFromEntitiesList(cachedCharacters)))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}

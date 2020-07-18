package com.tanaka.mazivanhanga.itsmvvmmorty.di

import com.tanaka.mazivanhanga.itsmvvmmorty.networking.ApiService
import com.tanaka.mazivanhanga.itsmvvmmorty.networking.NetworkMapper
import com.tanaka.mazivanhanga.itsmvvmmorty.repository.MainRepository
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CacheMapper
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        characterDao: CharacterDao,
        apiService: ApiService,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): MainRepository {
        return MainRepository(
            characterDao = characterDao,
            apiService = apiService,
            characterCacheMapper = cacheMapper,
            networkMapper = networkMapper
        )
    }
}
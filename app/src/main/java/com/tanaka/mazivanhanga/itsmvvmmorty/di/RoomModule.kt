package com.tanaka.mazivanhanga.itsmvvmmorty.di

import android.content.Context
import androidx.room.Room
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CharacterDao
import com.tanaka.mazivanhanga.itsmvvmmorty.room.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideCharacterDb(@ApplicationContext context: Context): CharacterDatabase {
        return Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            CharacterDatabase.DATA_BASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideCharacterDao(characterDatabase: CharacterDatabase): CharacterDao {
        return characterDatabase.characterDao()
    }

}
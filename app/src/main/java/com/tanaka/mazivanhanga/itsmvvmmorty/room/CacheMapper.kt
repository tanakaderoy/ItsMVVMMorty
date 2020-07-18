package com.tanaka.mazivanhanga.itsmvvmmorty.room

import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import com.tanaka.mazivanhanga.itsmvvmmorty.util.EntityMapper
import javax.inject.Inject


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class CacheMapper
@Inject
constructor(): EntityMapper<CharacterCacheEntity, Character>{
    override fun mapFromEntity(entity: CharacterCacheEntity): Character {
        return Character(
            id = entity.id,
            name = entity.name,
            status = entity.status,
            species = entity.species,
            gender = entity.gender,
            type = entity.type,
            location = entity.location,
            origin = entity.origin,
            image = entity.image,
            episode = entity.episode,
            url = entity.url,
            created = entity.created
        )
    }

    override fun mapToEntity(domainModel: Character): CharacterCacheEntity {
        return CharacterCacheEntity(
            id = domainModel.id,
            name = domainModel.name,
            status = domainModel.status,
            species = domainModel.species,
            gender = domainModel.gender,
            type = domainModel.type,
            location = domainModel.location,
            origin = domainModel.origin,
            image = domainModel.image,
            episode = domainModel.episode,
            url = domainModel.url,
            created = domainModel.created
        )
    }

    fun mapFromEntitiesList(entities: List<CharacterCacheEntity>): List<Character>{
        return entities.map { mapFromEntity(it) }
    }
}
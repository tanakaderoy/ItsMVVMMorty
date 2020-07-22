package com.tanaka.mazivanhanga.itsmvvmmorty.networking

import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character
import com.tanaka.mazivanhanga.itsmvvmmorty.util.EntityMapper
import javax.inject.Inject


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class NetworkMapper
@Inject
constructor() : EntityMapper<CharacterModelResponse, List<Character>> {
    override fun mapFromEntity(entity: CharacterModelResponse): List<Character> {
        return entity.results.map {
            Character(
                id = it.id,
                name = it.name,
                status = it.status,
                species = it.species,
                gender = it.gender,
                type = it.type,
                location = it.location,
                origin = it.origin,
                image = it.image,
                episode = it.episode,
                url = it.url,
                created = it.created
            )
        }
    }

    override fun mapToEntity(domainModel: List<Character>): CharacterModelResponse {
        //Probably not going to be used
        return domainModel.map {
            CharacterModelResponse(
                info = Info(0, 0, "", null),
                results = listOf(
                    Result(
                        id = it.id,
                        name = it.name,
                        status = it.status,
                        species = it.species,
                        gender = it.gender,
                        type = it.type,
                        location = it.location,
                        origin = it.origin,
                        image = it.image,
                        episode = it.episode,
                        url = it.url,
                        created = it.created
                    )
                )
            )
        }[0]
    }

}
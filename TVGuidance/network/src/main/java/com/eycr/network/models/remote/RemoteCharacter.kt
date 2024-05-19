package com.eycr.network.models.remote

import com.eycr.network.models.domain.ShowCharacter
import com.eycr.network.models.domain.CharacterGender
import com.eycr.network.models.domain.CharacterStatus
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
) {
    @Serializable
    data class Location(
        val name: String,
        val url: String,
    )
    @Serializable
    data class Origin(
        val name: String,
        val url: String
    )
}

fun RemoteCharacter.toDomainCharacter(): ShowCharacter {
    val characterGender = when(gender.lowercase()) {
        "female" -> CharacterGender.Female
        "male" -> CharacterGender.Male
        "genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }
    val characterStatus = when (status.lowercase()) {
        "alive" -> CharacterStatus.Alive
        "dead" -> CharacterStatus.Dead
        else -> CharacterStatus.Unknown
    }
    return ShowCharacter(
        created = created,
        episodeIds = episode.map { it.substring(it.lastIndexOf("/") + 1).toInt() },
        gender = characterGender,
        id = id,
        imageUrl = image,
        location = ShowCharacter.Location(name = location.name, url = location.url),
        name = name,
        origin = ShowCharacter.Origin(name = origin.name, url = origin.url),
        species = species,
        status = characterStatus,
        type = type
    )
}
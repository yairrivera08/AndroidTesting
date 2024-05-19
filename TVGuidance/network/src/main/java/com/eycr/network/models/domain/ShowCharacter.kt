package com.eycr.network.models.domain

data class ShowCharacter(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val origin: Origin,
    val location: Location,
    val imageUrl: String,
    val episodeIds: List<Int>,
    val created: String
) {
    data class Location(
        val name: String,
        val url: String,
    )
    data class Origin(
        val name: String,
        val url: String
    )
}
package com.example.pr5_pokemon_panarin

data class PokemonResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlot>
)

data class TypeSlot(val type: TypeName)

data class TypeName(val name: String)

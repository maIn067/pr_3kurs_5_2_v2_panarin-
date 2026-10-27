package com.example.pr5_pokemon_panarin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val owner: String,
    val name: String,
    val height: Int,
    val weight: Int,
    val type: String,
    val note: String
)

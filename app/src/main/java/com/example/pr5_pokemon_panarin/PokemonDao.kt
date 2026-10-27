package com.example.pr5_pokemon_panarin

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemons WHERE owner = :owner")
    fun getByOwner(owner: String): List<PokemonEntity>

    @Insert
    fun insert(pokemon: PokemonEntity)

    @Update
    fun update(pokemon: PokemonEntity)

    @Delete
    fun delete(pokemon: PokemonEntity)
}

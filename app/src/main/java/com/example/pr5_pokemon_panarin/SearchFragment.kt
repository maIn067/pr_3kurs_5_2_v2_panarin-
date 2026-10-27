package com.example.pr5_pokemon_panarin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class SearchFragment : Fragment() {

    private lateinit var textInfo: AppCompatTextView
    private var pokemon: PokemonResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val editQuery: AppCompatEditText = view.findViewById(R.id.edit_query)
        val btnSearch: AppCompatButton = view.findViewById(R.id.btn_search)
        val btnAdd: AppCompatButton = view.findViewById(R.id.btn_add)
        val btnCollection: AppCompatButton = view.findViewById(R.id.btn_collection)
        textInfo = view.findViewById(R.id.text_info)

        val prefs = requireActivity().getSharedPreferences("pokemon_app", Context.MODE_PRIVATE)
        val gson = Gson()
        val user = gson.fromJson(prefs.getString("user", ""), User::class.java)

        val lastJson = prefs.getString("last_pokemon", null)
        if (lastJson != null) {
            pokemon = gson.fromJson(lastJson, PokemonResponse::class.java)
            showPokemon()
        }

        btnSearch.setOnClickListener {
            val query = editQuery.text.toString().trim().lowercase()
            if (query.isEmpty()) {
                Toast.makeText(requireContext(), "Введите имя или номер", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val url = "https://pokeapi.co/api/v2/pokemon/$query"
            val queue = Volley.newRequestQueue(requireContext())
            val request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    pokemon = gson.fromJson(response, PokemonResponse::class.java)
                    prefs.edit().putString("last_pokemon", gson.toJson(pokemon)).apply()
                    Log.d("MyLog", "Найден покемон: ${pokemon!!.name}")
                    showPokemon()
                },
                { error ->
                    Log.d("MyLog", "Volley error: $error")
                    Toast.makeText(requireContext(), "Покемон не найден", Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(request)
        }

        btnAdd.setOnClickListener {
            if (pokemon == null) {
                Toast.makeText(requireContext(), "Сначала найдите покемона", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val p = pokemon!!
            val entity = PokemonEntity(
                owner = user.login,
                name = p.name,
                height = p.height,
                weight = p.weight,
                type = p.types[0].type.name,
                note = ""
            )
            AppDatabase.getDatabase(requireContext()).pokemonDao().insert(entity)
            Toast.makeText(requireContext(), "Добавлено в коллекцию", Toast.LENGTH_SHORT).show()
        }

        btnCollection.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CollectionFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun showPokemon() {
        val p = pokemon!!
        textInfo.text = "Номер: ${p.id}\n" +
                "Имя: ${p.name}\n" +
                "Рост: ${p.height}\n" +
                "Вес: ${p.weight}\n" +
                "Тип: ${p.types[0].type.name}"
    }
}

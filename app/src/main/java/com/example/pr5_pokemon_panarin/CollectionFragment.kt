package com.example.pr5_pokemon_panarin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class CollectionFragment : Fragment() {

    private lateinit var listPokemons: ListView
    private lateinit var dao: PokemonDao
    private lateinit var login: String
    private var pokemons: List<PokemonEntity> = listOf()
    private var selected: PokemonEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        val textSelected: AppCompatTextView = view.findViewById(R.id.text_selected)
        val editNote: AppCompatEditText = view.findViewById(R.id.edit_note)
        val btnEdit: AppCompatButton = view.findViewById(R.id.btn_edit)
        val btnDelete: AppCompatButton = view.findViewById(R.id.btn_delete)
        val btnBack: AppCompatButton = view.findViewById(R.id.btn_back)
        listPokemons = view.findViewById(R.id.list_pokemons)

        val prefs = requireActivity().getSharedPreferences("pokemon_app", Context.MODE_PRIVATE)
        login = Gson().fromJson(prefs.getString("user", ""), User::class.java).login
        dao = AppDatabase.getDatabase(requireContext()).pokemonDao()

        updateList()

        listPokemons.setOnItemClickListener { _, _, position, _ ->
            selected = pokemons[position]
            textSelected.text = "Выбран: ${selected!!.name}"
            editNote.setText(selected!!.note)
        }

        btnEdit.setOnClickListener {
            if (selected == null) {
                Toast.makeText(requireContext(), "Выберите покемона", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dao.update(selected!!.copy(note = editNote.text.toString()))
            selected = null
            textSelected.text = ""
            editNote.setText("")
            updateList()
        }

        btnDelete.setOnClickListener {
            if (selected == null) {
                Toast.makeText(requireContext(), "Выберите покемона", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dao.delete(selected!!)
            selected = null
            textSelected.text = ""
            editNote.setText("")
            updateList()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun updateList() {
        pokemons = dao.getByOwner(login)
        val items = mutableListOf<String>()
        for (p in pokemons) {
            items.add("${p.name}, тип: ${p.type}, заметка: ${p.note}")
        }
        listPokemons.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
    }
}

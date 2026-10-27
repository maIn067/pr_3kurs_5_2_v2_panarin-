package com.example.pr5_pokemon_panarin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val editLogin: AppCompatEditText = view.findViewById(R.id.edit_login)
        val editPassword: AppCompatEditText = view.findViewById(R.id.edit_password)
        val btnEnter: AppCompatButton = view.findViewById(R.id.btn_enter)

        val prefs = requireActivity().getSharedPreferences("pokemon_app", Context.MODE_PRIVATE)
        val gson = Gson()

        btnEnter.setOnClickListener {
            val login = editLogin.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userJson = prefs.getString("user", null)
            if (userJson != null) {
                val oldUser = gson.fromJson(userJson, User::class.java)
                if (oldUser.login == login && oldUser.password != password) {
                    Toast.makeText(requireContext(), "Неверный пароль", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val user = User(login, password)
            prefs.edit().putString("user", gson.toJson(user)).apply()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .commit()
        }

        return view
    }
}

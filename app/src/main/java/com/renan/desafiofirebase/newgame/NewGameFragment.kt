package com.renan.desafiofirebase.newgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.renan.desafiofirebase.R

class NewGameFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameGame = view.findViewById<TextInputEditText>(R.id.tietNameGame)
        val dateGame = view.findViewById<TextInputEditText>(R.id.tietCreatedAt)
        val description = view.findViewById<TextInputEditText>(R.id.tietDescription)
        val btnSaveGame = view.findViewById<MaterialButton>(R.id.btnSaveGame)
        val navControler = findNavController()

        btnSaveGame.setOnClickListener {
            navControler.navigate(
                R.id.action_newGameFragment_to_homeFragment, bundleOf(
                    "nameGame" to nameGame.text.toString(),
                    "dateGame" to dateGame.text.toString(),
                    "description" to description.text.toString()
                )
            )
        }

    }
}
package com.renan.desafiofirebase.home

import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.renan.desafiofirebase.R
import kotlin.math.log


class HomeFragment : Fragment() {

    private lateinit var _view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var listGames: MutableList<Games> = mutableListOf(
            Games(
                R.drawable.splash,
                "Zelda novo",
                "2021",
                "Jogo louco"

            ),
            Games(
                R.drawable.splash,
                "Zelda 2",
                "2025",
                "Jogo louco em 2025"

            )
        )

        _view = view

        val recyclerViewGames = _view.findViewById<RecyclerView>(R.id.recyclerListGames)
        recyclerViewGames.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(view.context, 2)
            adapter = GameListAdapter(listGames, view.context)
        }
        addNewGame()

        val nameGame = arguments?.getString("nameGame")
        val dateGame = arguments?.getString("dateGame")
        val description = arguments?.getString("description")

        if (nameGame != null) {

            listGames.add(
                Games(
                    R.drawable.splash,
                    "$nameGame",
                    "$dateGame",
                    "$description"
                )
            )
            recyclerViewGames.adapter?.run {
                notifyDataSetChanged()

            }

        }
        Log.d("RENANVALOR", "TESTE = $listGames")


    }

    private fun addNewGame() {
        val btnAddGame = _view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        val navControler = findNavController()
        btnAddGame.setOnClickListener {
            navControler.navigate(R.id.action_homeFragment_to_newGameFragment)
        }
    }

    private fun listGameAdd(
        listGames: MutableList<Games>,
        nameGame: String,
        dateGame: String,
        description: String,
    ) {
        listGames.add(
            Games(
                R.drawable.splash,
                nameGame,
                dateGame,
                description
            )
        )
    }
}
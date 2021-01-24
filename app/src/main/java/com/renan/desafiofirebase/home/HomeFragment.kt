package com.renan.desafiofirebase.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.renan.desafiofirebase.R


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

        _view = view

        val recyclerViewGames = _view.findViewById<RecyclerView>(R.id.recyclerListGames)
        recyclerViewGames.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = GameListAdapter(listGames(), context)
        }
        addNewGame()
    }

    private fun addNewGame() {
        val btnAddGame = _view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        val navControler = findNavController()
        btnAddGame.setOnClickListener {
            navControler.navigate(R.id.action_homeFragment_to_newGameFragment)
        }
    }

    private fun listGames(): List<Games> {
        return listOf(
            Games(
                R.drawable.ic_launcher_background,
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
    }
}
package com.renan.desafiofirebase.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.renan.desafiofirebase.R
import com.renan.desafiofirebase.home.model.GamesModel
import com.renan.desafiofirebase.home.adapter.GameListAdapter
import com.renan.desafiofirebase.home.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var _view: View

    private var listGames = mutableListOf<GamesModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gameListAdapter: GameListAdapter
    private lateinit var navController: NavController

    private val _homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

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
        navController = findNavController()

        recyclerView()

    }

    private fun recyclerView() {
        recyclerView = _view.findViewById(R.id.recyclerListGames)
        val manager = GridLayoutManager(_view.context, 2)

        gameListAdapter = GameListAdapter(listGames) {
            navigateToGameDetails(it)
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = gameListAdapter
        }
    }

    private fun navigateToGame(gameModel: GamesModel) {
        val bundle = bundleOf(
            GAME_ID to gameModel.id,
            GAME_IMAGE_URL to gameModel.imageUrl,
            GAME_TITLE to gameModel.title,
            GAME_CREATED_AT to gameModel.createdAt,
            GAME_DESCRIPTION to gameModel.description
        )
        _navController.navigate(R.id.action_homeFragment_to_gameFragment, bundle)
    }

}
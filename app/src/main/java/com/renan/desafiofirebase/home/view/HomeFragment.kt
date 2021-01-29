package com.renan.desafiofirebase.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var _view: View

    private var listGames = mutableListOf<GamesModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gameListAdapter: GameListAdapter
    private lateinit var navController: NavController

    private val homeViewModel: HomeViewModel by lazy {
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

        homeViewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(_view.context, it, Toast.LENGTH_SHORT).show()
        })

        homeViewModel.stateList.observe(viewLifecycleOwner, {
            addAllgames(it as MutableList<GamesModel>)
        })

        homeViewModel.stateQueryList.observe(viewLifecycleOwner, {
            addAllgames(it as MutableList<GamesModel>)
        })

        homeViewModel.getListGames()

        recyclerView()
        searchEvent()
        addNewGame()

    }

    private fun recyclerView() {
        recyclerView = _view.findViewById(R.id.recyclerListGames)
        val manager = GridLayoutManager(_view.context, 2)

        gameListAdapter = GameListAdapter(listGames) {
            navigateToGame(it)
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = gameListAdapter
        }
    }

    private fun navigateToGame(gameModel: GamesModel) {
        val bundle = bundleOf(
            GAME_ID to gameModel.gameId,
            GAME_IMAGE to gameModel.image,
            GAME_NAME to gameModel.name,
            GAME_DATE to gameModel.dateLaunch,
            GAME_DESCRIPTION to gameModel.description
        )
        navController.navigate(R.id.action_homeFragment_to_detailsGameFragment, bundle)
    }

    private fun addAllgames(list: MutableList<GamesModel>) {
        listGames.clear()
        listGames.addAll(list)
        gameListAdapter.notifyDataSetChanged()
    }

    private fun searchEvent() {
        var job: Job? = null
        val searchBar = _view.findViewById<SearchView>(R.id.searchViewHome)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    if (query.isNotEmpty()) {
                        homeViewModel.queryFirebaseService(query.toLowerCase(Locale.ROOT))
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("TAG", "Query changed")
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    if (newText.isEmpty()) {
                        homeViewModel.getListGames()
                    } else {
                        homeViewModel.queryFirebaseService(newText.toLowerCase(Locale.ROOT))
                    }
                }
                return false
            }
        })
        searchBar.setOnCloseListener {
            homeViewModel.getListGames()
            true
        }
    }
    private fun addNewGame() {
        val addBtn = _view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        addBtn.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_newGameFragment)
        }
    }

    companion object {
        const val GAME_ID: String = "id"
        const val GAME_NAME: String = "title"
        const val GAME_DESCRIPTION: String = "description"
        const val GAME_DATE: String = "createdat"
        const val GAME_IMAGE: String = "IMAGEURL"
    }

}
package com.renan.desafiofirebase.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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


class HomeFragment : Fragment() {

    private lateinit var _view: View

    var listGames = mutableListOf<GamesModel>()
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var userDatabaseRef: DatabaseReference
    private lateinit var userStorageRef: StorageReference


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
        initSetup()
        val recyclerViewGames = _view.findViewById<RecyclerView>(R.id.recyclerListGames)
        recyclerViewGames.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(view.context, 2)
            adapter = GameListAdapter(listGames){
                val bundle = bundleOf("GAME" to it)
            }
        }


        val gameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              //  val games = snapshot.value as MutableList<*>

                recyclerViewGames.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", error.toException())
            }
        }
        userDatabaseRef.addValueEventListener(gameListener)

        val nameGame = arguments?.getString("nameGame")
        val dateGame = arguments?.getString("dateGame")
        val description = arguments?.getString("description")


//        if(nameGame != null){
//            val id: String = myRef.push().key.toString()
//            val gameItem = GamesModel(
////                R.drawable.splash,
//                "teste",
//                nameGame.toString(),
//                dateGame.toString(),
//                description.toString()
//            )
//            var a = myRef.child(id).setValue(gameItem)
//            listGames.add(gameItem)
//            recyclerViewGames.adapter?.notifyDataSetChanged()
//        }
        btnAddNewGame()
    }

    private fun initSetup() {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        storageRef = storage.getReference("uploads")
        databaseRef = database.getReference("users")
        userDatabaseRef = databaseRef.child(user.uid)
        userStorageRef = storageRef.child(user.uid)
    }

    private fun btnAddNewGame() {
        val btnAddGame = _view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        val navControler = findNavController()
        btnAddGame.setOnClickListener {
            navControler.navigate(R.id.action_homeFragment_to_newGameFragment)
        }
    }

//    private fun listGameAdd(
//        listGames: MutableList<GamesModel>,
//        nameGame: String,
//        dateGame: String,
//        description: String,
//    ) {
//        listGames.add(
//            GamesModel(
//                R.drawable.splash,
//                nameGame,
//                dateGame,
//                description
//            )
//        )
//    }
}
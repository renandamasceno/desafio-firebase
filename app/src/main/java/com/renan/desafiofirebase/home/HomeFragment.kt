package com.renan.desafiofirebase.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.renan.desafiofirebase.R
import org.json.JSONObject


class HomeFragment : Fragment() {

    private lateinit var _view: View

    var listGames: ArrayList<GamesModel> = ArrayList()
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("recyclerview")

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
            layoutManager = GridLayoutManager(view.context, 2)
            adapter = GameListAdapter(listGames, view.context, myRef)
        }
        btnAddNewGame()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val gamesHash = snapshot.value as HashMap<*,*>

                    for ((k,v) in gamesHash){
                        val json = JSONObject(v.toString())
                        val result = Klaxon()
                            .parse<GamesModel>(json.toString())
                        listGames.add(result!!)
                    }
                    recyclerViewGames.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val nameGame = arguments?.getString("nameGame")
        val dateGame = arguments?.getString("dateGame")
        val description = arguments?.getString("description")

        if(nameGame != null){
            val id: String = myRef.push().key.toString()
            val gameItem = GamesModel(
//                R.drawable.splash,
                nameGame.toString(),
                dateGame.toString(),
                description.toString()
            )
            val a = myRef.child(id).setValue(gameItem)
            listGames.add(gameItem)
            recyclerViewGames.adapter?.notifyDataSetChanged()
        }


    }

    private fun btnAddNewGame() {
        val btnAddGame = _view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        val navControler = findNavController()
        btnAddGame.setOnClickListener {
            navControler.navigate(R.id.action_homeFragment_to_newGameFragment)
        }
    }

    private fun listGameAdd(
        listGames: MutableList<GamesModel>,
        nameGame: String,
        dateGame: String,
        description: String,
    ) {
        listGames.add(
            GamesModel(
//                R.drawable.splash,
                nameGame,
                dateGame,
                description
            )
        )
    }
}
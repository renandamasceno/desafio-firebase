package com.renan.desafiofirebase.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.renan.desafiofirebase.home.model.GamesModel
import com.renan.desafiofirebase.utils.ProjectUtils.getUserId

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var stateQueryList = MutableLiveData<List<GamesModel>>()
    var loading = MutableLiveData<Boolean>()
    var stateList = MutableLiveData<List<GamesModel>>()
    var error = MutableLiveData<String>()

    fun getListGames() {
        loading.value = true
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(
            getUserId(getApplication()).toString()
        )
        Log.d("TAG", "get games")
        reference.orderByKey().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val games = mutableListOf<GamesModel>()
                loading.value = false
                if (snapshot.hasChildren()) {
                    snapshot.children.forEach { snapshotChildren ->
                        val game = snapshotChildren.getValue(GamesModel::class.java)
                        game?.let { games.add(it) }
                    }
                }
                Log.d("TAG", "Home: on Data change")
                stateQueryList.value = games
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "Home: cancelled")
            }
        })
    }

    fun queryFirebaseService(searchText: String) {
        Log.d("TAG", searchText)
        loading.value = true
        val databaseReference =
            FirebaseDatabase.getInstance().getReference(getUserId(getApplication()).toString())

        val query: Query =
            databaseReference.orderByChild("title").startAt(searchText).endAt("${searchText}\uf8ff")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("TAG", "Query onDataChange")
                loading.value = false

                if (dataSnapshot.exists()) {
                    Log.d("TAG", "Query dataSnapshot exists")
                    // dataSnapshot is the "issue" node with all children with id 0
                    val game = mutableListOf<GamesModel>()
                    for (data in dataSnapshot.children) {
                        // do something with the individual "issues"
                        val gameData = data.getValue(GamesModel::class.java)
                        gameData?.let { game.add(it) }
                    }
                    stateQueryList.value = game
                } else {
                    Log.d("TAG", "Query data no data exists")
                    stateQueryList.value = mutableListOf()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loading.value = false
                Log.d("TAG", "onCancelled")
            }
        })
    }
}
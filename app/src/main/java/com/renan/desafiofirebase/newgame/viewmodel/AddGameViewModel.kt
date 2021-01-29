package com.renan.desafiofirebase.newgame.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.renan.desafiofirebase.home.model.GamesModel
import com.renan.desafiofirebase.utils.ProjectUtils
import com.renan.desafiofirebase.utils.ProjectUtils.getUserId

class AddGameViewModel(application: Application) : AndroidViewModel(application) {
    var loading = MutableLiveData<Boolean>()
    var error = MutableLiveData<String>()
    var stateGameRegistered = MutableLiveData<Boolean>()
    var stateGameUpdated = MutableLiveData<Boolean>()
    var stateImage = MutableLiveData<String>()

    fun createGame(
        gameModel: GamesModel
    ) {
        loading.value = true
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(
            getUserId(getApplication()).toString()
        )

        reference.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var gameAdded = false

                if (snapshot.hasChildren()) {
                    for (data in snapshot.children) {
                        val firebaseResult = data.getValue(GamesModel::class.java)
                        if (firebaseResult?.gameId == gameModel.gameId) {
                            gameAdded = true
                        }
                    }
                }
                if (gameAdded) {
                    updateGameFirebase(reference, gameModel)
                } else {
                    saveGameFirebase(reference, gameModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateGameFirebase(reference: DatabaseReference, gameModel: GamesModel) {
        Log.d("TAG", "update no firebase")
        reference.child(gameModel.gameId).setValue(gameModel)

        reference.child(gameModel.gameId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                loading.value = false
                Log.d("TAG", "jogo atualizado")
                stateGameUpdated.value = true
            }

            override fun onCancelled(error: DatabaseError) {
                loading.value = false
                Log.d("TAG", "jogo não atualizado")
                errorMessage("Couldn't update the game")
            }
        })
    }

    private fun saveGameFirebase(reference: DatabaseReference, gameModel: GamesModel) {
        Log.d("TAG", "adicionando no firebase")
        val key = reference.push().key

        key?.let {
            gameModel.gameId = key
            reference.child(it).setValue(gameModel)

            reference.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    loading.value = false
                    Log.d("TAG", "jogo adicionado")
                    stateGameRegistered.value = true
                }

                override fun onCancelled(error: DatabaseError) {
                    loading.value = false
                    Log.d("TAG", "jogo não adicionado")
                    errorMessage("Couldn't add the game")
                }
            })
        }
    }

    fun errorMessage(s: String) {
        error.value = s
    }

    fun updateGamePhoto(
        view: View,
        imageUri: Uri?
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        loading.value = true

        if (imageUri != null && user != null) {
            val database = FirebaseStorage.getInstance()
            val storageReference = database.reference.child(
                "GAME_IMAGE_" +
                        System.currentTimeMillis() +
                        "." +
                        ProjectUtils.getFileExtension(view, imageUri)
            )

            storageReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { taskSnapshot ->
                        loading.value = false
                        Log.d("TAG", taskSnapshot.toString())
                        stateImage.value = taskSnapshot.toString()

                    }.addOnFailureListener {
                        loading.value = false
                        errorMessage("Error storing image")
                        stateImage.value = ""
                    }
                }
        } else {
            loading.value = false
            stateImage.value = ""
        }
    }
}
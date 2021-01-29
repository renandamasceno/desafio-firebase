package com.renan.desafiofirebase.newgame.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.renan.desafiofirebase.R
import com.renan.desafiofirebase.home.model.GamesModel
import de.hdodenhof.circleimageview.CircleImageView

class NewGameFragment : Fragment() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val storage = FirebaseStorage.getInstance()
    val userRef = storage.getReference("upload")
    val database = FirebaseDatabase.getInstance()
    val databaseRef = database.getReference("users")
    private lateinit var imageFileReference: String
    private var imageURI: Uri? = null

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
        val imgCreateGame = view.findViewById<CircleImageView>(R.id.imgCreateGameBtn)
        val navControler = findNavController()
        imageFileReference = ""

        btnSaveGame.setOnClickListener {
            navControler.navigate(
                R.id.action_newGameFragment_to_homeFragment, bundleOf(
                    "nameGame" to nameGame.text.toString(),
                    "dateGame" to dateGame.text.toString(),
                    "description" to description.text.toString(),
                    "ref" to enviarArquivo(userRef),
                    "enviarGame" to enviarGame(
                        databaseRef,
                        nameGame.text.toString(),
                        dateGame.text.toString(),
                        description.text.toString(),
                        imageFileReference
                    )
                )
            )
        }

        imgCreateGame.setOnClickListener {
            findFiles()
        }

    }

    private fun findFiles() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CONTENT_REQUEST_CODE)
    }

    fun enviarArquivo(storageReference: StorageReference) {
        if (imageURI != null) {
            imageURI?.run {

                val extension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(activity?.contentResolver?.getType(imageURI!!))

                val fileReference =
                    storageReference.child(user!!.uid).child("${System.currentTimeMillis()}.${extension}")

                fileReference.putFile(this)
                    .addOnSuccessListener {
                        imageFileReference = fileReference.toString()
                    }
                    .addOnFailureListener {
                        // TODO: 26/01/21
                    }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CONTENT_REQUEST_CODE && resultCode == RESULT_OK){
            imageURI = data?.data
            view?.findViewById<CircleImageView>(R.id.imgCreateGameBtn)?.setImageURI(imageURI)
        }

    }

    fun enviarGame(
        databaseRef: DatabaseReference,
        name: String,
        date: String,
        description: String,
        imageRef: String
    ) {
        //val newGame = GamesModel(name, date, description, imageRef)
       // databaseRef.child(user!!.uid).child(name).setValue(newGame)

    }

    companion object {
        const val CONTENT_REQUEST_CODE = 1
    }
}
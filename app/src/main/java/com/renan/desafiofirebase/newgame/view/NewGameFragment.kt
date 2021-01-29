package com.renan.desafiofirebase.newgame.view

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
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
import com.renan.desafiofirebase.newgame.viewmodel.AddGameViewModel
import com.renan.desafiofirebase.utils.Constants.PICK_IMAGE_REQUEST_CODE
import com.renan.desafiofirebase.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.renan.desafiofirebase.utils.ProjectUtils.hideKeyboard
import com.renan.desafiofirebase.utils.ProjectUtils.validateText
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class NewGameFragment : Fragment() {

    private lateinit var _view: View
    private val addNewGameViewModel: AddGameViewModel by lazy {
        ViewModelProvider(this).get(AddGameViewModel::class.java)
    }
    private lateinit var navController: NavController
    private var selectedImageUri: Uri? = null
    private var gameImageUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        navController = findNavController()

        addNewGameViewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(_view.context, it, Toast.LENGTH_SHORT).show()
        })

        addNewGameViewModel.stateGameRegistered.observe(viewLifecycleOwner, {
            navigateHome(it)
        })

        addNewGameViewModel.stateGameUpdated.observe(viewLifecycleOwner, {
            Toast.makeText(
                _view.context,
                "Game adicionado co suceso",
                Toast.LENGTH_SHORT
            ).show()
            navigateHome(it)
        })

        addNewGameViewModel.stateImage.observe(viewLifecycleOwner, { state ->
            state?.let {
                if (it != "") {
                    gameImageUrl = it
                }
                addGameHandler()
            }
        })
        argumentsListener()
        imageListener()
        saveGame()

    }

    private fun addGameHandler() {
        val nameView = _view.findViewById<TextInputEditText>(R.id.tietNameGame)
        val createdAtView = _view.findViewById<TextInputEditText>(R.id.tietCreatedAt)
        val descriptionView = _view.findViewById<TextInputEditText>(R.id.tietDescription)

        val gameId = if (arguments?.getString(GAME_ID) != null) {
            arguments?.getString(GAME_ID)
        } else {
            ""
        }
        Log.d("TAG", "gameImageUrl = $gameImageUrl")
        val game = GamesModel(
            gameId!!,
            gameImageUrl!!,
            nameView.text.toString().toLowerCase(Locale.ROOT),
            createdAtView.text.toString(),
            descriptionView.text.toString()
        )
        addNewGameViewModel.createGame(game)
    }

    private fun argumentsListener() {
        val imageUrl = arguments?.getString(GAME_IMAGE)
        val name = arguments?.getString(GAME_NAME)
        val createdAt = arguments?.getInt(GAME_DATE)
        val description = arguments?.getString(GAME_DESCRIPTION)

        if (validateText(name, createdAt.toString(), description)) {
            val imageView = _view.findViewById<ImageView>(R.id.imgCreateGameBtn)
            val titleView = _view.findViewById<TextInputEditText>(R.id.tietNameGame)
            val createdAtView = _view.findViewById<TextInputEditText>(R.id.tietCreatedAt)
            val descriptionView = _view.findViewById<TextInputEditText>(R.id.tietDescription)

            titleView.setText(name!!.capitalize(Locale.ROOT))
            createdAtView.setText(createdAt.toString())
            descriptionView.setText(description!!.capitalize(Locale.ROOT))
            if (!imageUrl.isNullOrEmpty()) {
                gameImageUrl = imageUrl
                Picasso.get().load(imageUrl).into(imageView)
            }
        }
    }

    private fun imageListener() {
        val imageView = _view.findViewById<ImageView>(R.id.imgCreateGameBtn)
        imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    _view.context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showImagePicker()
            } else {
                checkReadExternalStoragePermission(
                    _view,
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
    }

    private fun showImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun checkReadExternalStoragePermission(view: View, requestCode: Int) {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        val result = ContextCompat.checkSelfPermission(view.context, permission)

        if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(permission)

            requestPermissions(
                listPermissionsNeeded.toTypedArray(),
                requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE
            && grantResults.contains(PackageManager.PERMISSION_GRANTED)
        ) {
            showImagePicker()
        } else {
            Toast.makeText(
                _view.context,
                "Permissão negada",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            selectedImageUri = data.data

            val profileImageView = _view.findViewById<ImageView>(R.id.imgCreateGameBtn)
            Picasso.get().load(selectedImageUri).fit().centerCrop().into(profileImageView)
        }
    }

    private fun navigateHome(isUpdated: Boolean) {
        if (isUpdated) {
            hideKeyboard(_view)
            Handler(Looper.getMainLooper()).postDelayed({
                navController.navigate(R.id.action_newGameFragment_to_homeFragment)
            }, 1500)
        }
    }

    private fun saveGame() {
        val saveGameBtn = _view.findViewById<MaterialButton>(R.id.btnSaveGame)
        val nameView = _view.findViewById<TextInputEditText>(R.id.tietNameGame)
        val createdAtView = _view.findViewById<TextInputEditText>(R.id.tietCreatedAt)
        val descriptionView = _view.findViewById<TextInputEditText>(R.id.tietDescription)

        saveGameBtn.setOnClickListener {
            hideKeyboard(_view)
            if (validateText(
                    nameView.text.toString(),
                    createdAtView.text.toString(),
                    descriptionView.text.toString()
                )
            ) {
                addNewGameViewModel.updateGamePhoto(
                    _view, selectedImageUri
                )
            } else {
                Toast.makeText(
                    _view.context,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
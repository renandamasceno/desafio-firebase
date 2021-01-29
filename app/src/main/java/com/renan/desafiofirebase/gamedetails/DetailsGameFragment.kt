package com.renan.desafiofirebase.gamedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.renan.desafiofirebase.R


class DetailsGameFragment : Fragment() {
    private lateinit var _view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        val gameId = arguments?.getString(GAME_ID)
        val image= arguments?.getString(GAME_IMAGE_URL)
        val name = arguments?.getString(GAME_TITLE)
        val dateLaunch = arguments?.getInt(GAME_CREATED_AT)
        val description = arguments?.getString(GAME_DESCRIPTION)

        val imageCoverView = _view.findViewById<ImageView>(R.id.imageGame)
        val titleView = _view.findViewById<TextView>(R.id.nameGame)
        val title2View = _view.findViewById<TextView>(R.id.name2game)
        val createdAtView = _view.findViewById<TextView>(R.id.dateGame)
        val descriptionView = _view.findViewById<TextView>(R.id.DescriptionGame)
    }


}
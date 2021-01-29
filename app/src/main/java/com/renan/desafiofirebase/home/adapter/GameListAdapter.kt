package com.renan.desafiofirebase.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.renan.desafiofirebase.R
import com.renan.desafiofirebase.home.model.GamesModel
import com.squareup.picasso.Picasso
import java.util.*

class GameListAdapter(
    private var dataSet: MutableList<GamesModel>,
    private val listener: (GamesModel) -> Unit
) : RecyclerView.Adapter<GameListAdapter.GameListViewHolder>() {
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {

        view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_home, parent, false)
        return GameListViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        val item = dataSet[position]

        val image = item.image
        val name = item.name
        val dateLaunch = item.dateLaunch

        holder.bind(image,name,dateLaunch)
        holder.itemView.setOnClickListener { listener(item) }
    }

    override fun getItemCount() = dataSet.size

    class GameListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameImage = itemView.findViewById<ImageView>(R.id.imgItemList)
        private val gameName = itemView.findViewById<TextView>(R.id.txtNameItemList)
        private val gameDate = itemView.findViewById<TextView>(R.id.txtDateItemList)

        fun bind(image: String, name: String, dateLaunch: String) {


            gameName.text = name.capitalize(Locale.ROOT)
            gameDate.text = dateLaunch
            if (image.isNotEmpty()) {
                Picasso.get().load(image).into(gameImage)
            } else {
                Picasso.get().load(R.drawable.placeholder).into(gameImage)
            }
        }

    }

}
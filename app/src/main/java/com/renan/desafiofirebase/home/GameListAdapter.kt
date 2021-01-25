package com.renan.desafiofirebase.home

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.renan.desafiofirebase.R
import com.squareup.picasso.Picasso

class GameListAdapter(
    private var dataSet: MutableList<Games>,
    private var context: Context
) : RecyclerView.Adapter<GameListAdapter.GameListViewHolder>() {
    lateinit var view: View

    class GameListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameImage = itemView.findViewById<ImageView>(R.id.imgItemList)
        val gameName = itemView.findViewById<TextView>(R.id.txtNameItemList)
        val gameDate = itemView.findViewById<TextView>(R.id.txtDateItemList)

        fun bind(GameModel: Games) {

            Picasso.get()
                .load(R.drawable.splash)
                .into(gameImage)

            gameName.text = GameModel.name
            gameDate.text = GameModel.dateLaunch
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {

        view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_home, parent, false)
        return GameListViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    override fun getItemCount() = dataSet.size

}
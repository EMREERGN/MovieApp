package com.emreergun.movieapp.activities.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emreergun.movieapp.R
import com.emreergun.movieapp.models.MovieModel

interface CardClickListener{
    fun onClicked(movie:MovieModel)
}

class MoiveViewHolder(view:View):RecyclerView.ViewHolder(view){
    val context=view.context
    val image=view.findViewById<ImageView>(R.id.imageView)
    val name=view.findViewById<TextView>(R.id.movieNameTxt)
    val playBtn=view.findViewById<ImageView>(R.id.playBtn)
    val descTxt=view.findViewById<TextView>(R.id.trailerDescTxt)

    val container:CardView=view.findViewById(R.id.cardContainer)

}


class MovieAdapter(
    val movieList:ArrayList<MovieModel>,
    val listener: CardClickListener
):RecyclerView.Adapter<MoiveViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoiveViewHolder {

        val root=LayoutInflater.from(parent.context).inflate(R.layout.movie_card_item,parent,false)

        return MoiveViewHolder(root)

    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MoiveViewHolder, position: Int) {

        val movie=movieList[position]

        if (movie.trailerLink==null){
            holder.playBtn.visibility=View.GONE
        }


        Glide.with(holder.context)
            .load(movie.imageUrl)
            .into(holder.image)

        holder.name.text=movie.name

        holder.playBtn.setOnClickListener {
            listener.onClicked(movie)
        }

        holder.container.setOnClickListener {
            when(holder.descTxt.visibility){
                View.GONE->{
                    holder.descTxt.visibility=View.VISIBLE
                }
                View.VISIBLE->{
                    holder.descTxt.visibility=View.GONE
                }
            }
        }




    }
}
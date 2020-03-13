package com.emreergun.movieapp.activities.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emreergun.movieapp.R
import com.emreergun.movieapp.TrailerBottomSheetFragment
import com.emreergun.movieapp.models.MovieModel

interface SearchedMovieListener{
    fun cardClick(movieModel: MovieModel)
}
class SearchViewHolder(view: View):RecyclerView.ViewHolder(view){
    val context=view.context
    val searchPoster=view.findViewById<ImageView>(R.id.searchPoster)
    val searchMovieName=view.findViewById<TextView>(R.id.searchMovieName)
    val releaseDate=view.findViewById<TextView>(R.id.movieReleaseDateSearched)

    val container=view.findViewById<CardView>(R.id.searchedCardContainer)

}

class SearchViewAdapter(val movieList:ArrayList<MovieModel>,val listener:SearchedMovieListener):RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val root=LayoutInflater.from(parent.context).inflate(R.layout.detail_movie_item,parent,false)


        return SearchViewHolder(root)
    }

    override fun getItemCount(): Int {

        return movieList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        val movie=movieList[position]

        Glide.with(holder.context)
            .load(movie.imageUrl)
            .into(holder.searchPoster)

        holder.searchMovieName.text=movie.name
        holder.releaseDate.text=movie.releaseDate

        holder.container.setOnClickListener {
            listener.cardClick(movie)
        }




    }

}
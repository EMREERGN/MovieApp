package com.emreergun.movieapp.helper

import android.content.Context
import com.emreergun.movieapp.models.MovieModel
import com.google.gson.Gson

class SharedPrefHelper {
    companion object{

        private val PREF_KEY="Movie_App_Pref_Tag"
        private val MOVIE_LIST="Movie_List"

        fun setMovieList(context: Context,movieList: ArrayList<MovieModel>) {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            val gson=Gson()
            val json=gson.toJson(movieList)
            with (sharedPref.edit()) {
                putString(MOVIE_LIST, json)
                commit()
            }
        }

        fun getMovieList(context: Context): String? {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            return sharedPref.getString(MOVIE_LIST,null)
        }

    }



}
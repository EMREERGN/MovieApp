package com.emreergun.movieapp.helper

import android.content.Context
import com.emreergun.movieapp.models.MovieModel
import com.google.gson.Gson

class SharedPrefHelper {
    companion object{

        private val PREF_KEY="Movie_App_Pref_Tag"
        private val MOVIE_LIST="Movie_List"
        private val TRAILER_LIST="Trailer_List"

        fun setPosterList(context: Context, movieList: ArrayList<MovieModel>?) {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            if (movieList!=null){
                val gson=Gson()
                val json=gson.toJson(movieList)
                with (sharedPref.edit()) {
                    putString(MOVIE_LIST, json)
                    commit()
                }
            }
            else{
                // Null ise
                with (sharedPref.edit()) {
                    putString(MOVIE_LIST, null)
                    commit()
                }
            }

        }

        fun getPosterList(context: Context): String? {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            return sharedPref.getString(MOVIE_LIST,null)
        }





        fun setTrailerList(context: Context,movieList: ArrayList<MovieModel>?) {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            if (movieList!=null){
                val gson=Gson()
                val json=gson.toJson(movieList)
                with (sharedPref.edit()) {
                    putString(TRAILER_LIST, json)
                    commit()
                }
            }
            else{
                // null ise
                with (sharedPref.edit()) {
                    putString(TRAILER_LIST, null)
                    commit()
                }
            }


        }

        fun getTrailerList(context: Context): String? {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            return sharedPref.getString(TRAILER_LIST,null)
        }

    }



}
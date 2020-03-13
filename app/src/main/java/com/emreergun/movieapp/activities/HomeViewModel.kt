package com.emreergun.movieapp.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.emreergun.movieapp.helper.Repository
import com.emreergun.movieapp.models.MovieModel

class HomeViewModel(application: Application):AndroidViewModel(application) {

    val posterListObservable:LiveData<ArrayList<MovieModel>> = Repository().getPosters()
    val trailerListObservable:LiveData<ArrayList<MovieModel>> = Repository().getTrailers()

}
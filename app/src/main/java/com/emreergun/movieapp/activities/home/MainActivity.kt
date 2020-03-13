package com.emreergun.movieapp.activities.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreergun.movieapp.R
import com.emreergun.movieapp.TrailerBottomSheetFragment
import com.emreergun.movieapp.helper.SharedPrefHelper
import com.emreergun.movieapp.models.MovieModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    lateinit var bottomSheet:TrailerBottomSheetFragment
    lateinit var movieAdapter: MovieAdapter

    lateinit var homeViewModel: HomeViewModel

    companion object{
        const val TAG="MainActivity"
    }

    lateinit var recyclerVeiw:RecyclerView


    private val movieContainerClickListener=object :
        CardClickListener {
        override fun onClicked(movie: MovieModel) {
            Log.i(TAG,"Clicked Movie :$movie")
            Toast.makeText(this@MainActivity,movie.name,Toast.LENGTH_SHORT).show()
            bottomSheet=TrailerBottomSheetFragment(movie)
            bottomSheet.show(supportFragmentManager,"")
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayoutManager=GridLayoutManager(this,2)
        recyclerVeiw=findViewById(R.id.recyclerViewMain)


        recyclerVeiw.apply {
            setHasFixedSize(true)
            layoutManager=gridLayoutManager
        }

        homeViewModel=ViewModelProvider(this).get(HomeViewModel::class.java)

        //getTrailer()
        getPosters()





    }

    private fun updateRecyclerView(list: ArrayList<MovieModel>) {
        movieAdapter = MovieAdapter(
            list,
            movieContainerClickListener
        )
        recyclerVeiw.adapter = movieAdapter
        recyclerVeiw.adapter?.notifyDataSetChanged()

    }

    private fun getPosters() {
        if (SharedPrefHelper.getPosterList(this)==null) {
            homeViewModel.posterListObservable.observe(this, Observer {
                updateRecyclerView(it)
                SharedPrefHelper.setPosterList(this@MainActivity, it)
            })
        }
        else{
            val gson=Gson()
            val posterList=gson.fromJson(SharedPrefHelper.getPosterList(this),object :TypeToken<ArrayList<MovieModel>>(){}.type ) as ArrayList<MovieModel>
            updateRecyclerView(posterList)
        }
    }

    private fun getTrailer() {

        if (SharedPrefHelper.getTrailerList(this)==null){
            homeViewModel.trailerListObservable.observe(this, Observer {
                updateRecyclerView(it)
                SharedPrefHelper.setTrailerList(this@MainActivity, it)
            })
        }
        // Fragmanlar kayıtlı  ise
        else{
            val gson=Gson()
            val movieList=gson.fromJson(SharedPrefHelper.getTrailerList(this),object :TypeToken<ArrayList<MovieModel>>(){}.type ) as ArrayList<MovieModel>
            updateRecyclerView(movieList)
        }




    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=MenuInflater(this)
        inflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.main_menu_trailer ->{
                Toast.makeText(this,"Trailer",Toast.LENGTH_SHORT).show()
                getTrailer()

            }
            R.id.main_menu_fav_movies ->{
                Toast.makeText(this,"Posters",Toast.LENGTH_SHORT).show()

                getPosters()
            }
        }
        return true
    }

}



package com.emreergun.movieapp.activities

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreergun.movieapp.R
import com.emreergun.movieapp.activities.MainActivity.Companion.TAG
import com.emreergun.movieapp.helper.SharedPrefHelper
import com.emreergun.movieapp.models.MovieModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    interface Listener{
        fun onfinished(movieList:ArrayList<MovieModel>)
    }

    companion object{
        const val TAG="MainActivity"
    }

    lateinit var recyclerVeiw:RecyclerView


    private val movieContainerClickListener=object :
        CardClickListener {
        override fun onClicked(movie: MovieModel) {

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


        MyBackGroundTrailerTask(object : Listener{
            override fun onfinished(movieList: ArrayList<MovieModel>) {

            }

        }).execute()




        if (SharedPrefHelper.getMovieList(this)==null){
            MyBackGroundPosterTask(object :
                Listener {
                override fun onfinished(movieList: ArrayList<MovieModel>) {
                    SharedPrefHelper.setMovieList(this@MainActivity, movieList)
                    val movieAdapter =
                        MovieAdapter(
                            movieList,
                            movieContainerClickListener
                        )
                    recyclerVeiw.adapter = movieAdapter.apply {
                        notifyDataSetChanged()
                    }
                }

            }).execute()
        }
        else{
            val gson=Gson()
            val movieList=gson.fromJson(SharedPrefHelper.getMovieList(this),object :TypeToken<ArrayList<MovieModel>>(){}.type ) as ArrayList<MovieModel>

            val movieAdapter= MovieAdapter(
                movieList,
                movieContainerClickListener
            )
            recyclerVeiw.adapter=movieAdapter.apply {
                notifyDataSetChanged()
            }
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
            }
            R.id.main_menu_fav_movies ->{
                Toast.makeText(this,"Favorite",Toast.LENGTH_SHORT).show()

            }
        }
        return true
    }

}


class  MyBackGroundPosterTask(val listnener: MainActivity.Listener) :AsyncTask<Void,Void,Void>(){
    val movieList=ArrayList<MovieModel>()
    override fun doInBackground(vararg params: Void?): Void? {
        val doc = Jsoup.connect("https://www.imdb.com/?ref_=nv_home").get()
        //Log.i(TAG,doc.title())

        val elements =doc.getElementsByClass("ipc-media ipc-media--poster ipc-image-media-ratio--poster ipc-media--baseAlt ipc-media--poster-m ipc-poster__poster-image ipc-media__img")
        elements.forEach {
            val movieName=it.getElementsByTag("img").attr("alt").toString()
            val movieImageUrl=it.getElementsByTag("img").attr("srcset").toString().split(", ")[2]
            val movieDeatail =doc.getElementsByAttribute("aria-label").attr("href").toString()
            //Log.i(TAG,movieDeatail)

            movieList.add(MovieModel(movieName,movieImageUrl,null))
        }
        return null
    }
    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        listnener.onfinished(movieList)
    }


}




class  MyBackGroundTrailerTask(val listnener: MainActivity.Listener) :AsyncTask<Void,Void,Void>(){


    val movieList=ArrayList<MovieModel>()


    override fun onPreExecute() {
        super.onPreExecute()
        Log.i(TAG,"Trailer Task start----------------------")
    }
    override fun doInBackground(vararg params: Void?): Void? {
        val doc = Jsoup.connect("https://www.imdb.com/trailers/?ref_=nv_mv_tr").get()
        Log.i(TAG,doc.title())

        val elements =doc.getElementsByClass("gridlist-item trailer-item")
        Log.i(TAG,"list size :${elements.size}")
        elements.forEach {
            val movieName=it.getElementsByClass("trailer-caption").select("a").text()

            val movieDescription=it
                .getElementsByClass("trailer-image")
                .select("a")
                .select("img").attr("class","loadlate pri_image").attr("title").toString()
            Log.i(TAG,"Movie Name : $movieName  , Image Desc : $movieDescription")




            val movieTrailerUrl="https://www.imdb.com/"+it.getElementsByClass("trailer-image").select("a").attr("href").toString()


            //MovieModel
           /* val movie=MovieModel(movieName,)
            movieList.add()*/
        }
        return null
    }
    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        Log.i(TAG,"Trailer Task finish----------------------")
    }


}

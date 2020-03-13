package com.emreergun.movieapp.helper

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emreergun.movieapp.models.MovieModel
import org.jsoup.Jsoup

class Repository {

    companion object{
        const val TAG="Repository"
    }

    interface Listener{
        fun onfinished(movieList:ArrayList<MovieModel>)
    }


    fun getPosters():LiveData<ArrayList<MovieModel>>{
        val data= MutableLiveData<ArrayList<MovieModel>>()
        MyBackGroundPosterTask(object :Listener{
            override fun onfinished(posterList: ArrayList<MovieModel>) {
                data.value=posterList
            }
        }).execute()
        return data
    }


    fun getTrailers():LiveData<ArrayList<MovieModel>>{
        val data= MutableLiveData<ArrayList<MovieModel>>()
        MyBackGroundTrailerTask(object :Listener{
            override fun onfinished(trailerList: ArrayList<MovieModel>) {
                data.value=trailerList
            }
        }).execute()
        return data
    }

    private class  MyBackGroundPosterTask(val listnener: Listener) :
        AsyncTask<Void, Void, Void>(){
        val movieList=ArrayList<MovieModel>()
        override fun doInBackground(vararg params: Void?): Void? {
            val doc = Jsoup.connect("https://www.imdb.com/?ref_=nv_home").get()
            val elements =doc.getElementsByClass("ipc-media ipc-media--poster ipc-image-media-ratio--poster ipc-media--baseAlt ipc-media--poster-m ipc-poster__poster-image ipc-media__img")
            elements.forEach {
                val movieName=it.getElementsByTag("img").attr("alt").toString()
                val movieImageUrl=it.getElementsByTag("img").attr("srcset").toString().split(", ")[2]
                //val movieDetail =doc.getElementsByAttribute("aria-label").attr("href").toString()
                //Log.i("Poster_Info","movie deatail :$movieDetail")
                movieList.add(MovieModel(movieName,movieImageUrl,null,null,null,null,null,null,null,null))
            }
            return null
        }
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            listnener.onfinished(movieList)
        }
    }

    private class  MyBackGroundTrailerTask(val listnener:Listener) :
        AsyncTask<Void, Void, Void>(){
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

                val movieName=it.
                    getElementsByClass("trailer-caption")
                    .select("a")
                    .text()

                val movieDescription=it
                    .getElementsByClass("trailer-image")
                    .select("a")
                    .select("img").attr("class","loadlate pri_image").attr("title").toString()
                Log.i(TAG,"$movieName , Desc : $movieDescription")

                val movieImageUrl=it
                    .getElementsByClass("trailer-image")
                    .select("a")
                    .select("img").attr("class","loadlate pri_image").attr("data-src-x2").toString()


                val movieTrailerUrl="https://www.imdb.com/"+it
                    .getElementsByClass("trailer-image")
                    .select("a")
                    .attr("href")
                    .toString()

                val movie=MovieModel(movieName,movieImageUrl,movieTrailerUrl,movieDescription,null,null,null,null,null,null)
                movieList.add(movie)
            }
            return null
        }
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            listnener.onfinished(movieList)
            Log.i(TAG,"Trailer Task finish----------------------")
        }


    }


}
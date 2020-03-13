package com.emreergun.movieapp.activities.search

import android.graphics.Movie
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreergun.movieapp.R
import com.emreergun.movieapp.TrailerBottomSheetFragment
import com.emreergun.movieapp.activities.home.MovieAdapter
import com.emreergun.movieapp.activities.search.Test.Companion.TAG
import com.emreergun.movieapp.models.MovieModel
import org.jsoup.Jsoup

class Test : AppCompatActivity() {

    lateinit var searchEdt:EditText
    lateinit var searchRecyclerView:RecyclerView
    lateinit var searchImage:ImageView

    lateinit var movieAdapter:SearchViewAdapter
    var movieList=ArrayList<MovieModel>()


    companion object{
        const val TAG="TEST"
    }

    interface AsyncListener{
        fun onUpdate(movieModel: MovieModel)
    }




    private val listener=object :SearchedMovieListener{
        override fun cardClick(movie: MovieModel) {
            val bottomSheet= TrailerBottomSheetFragment(movie)
            bottomSheet.show(supportFragmentManager,"")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        searchEdt=findViewById(R.id.searchEdtText)
        searchRecyclerView=findViewById(R.id.searchRecyclerView)
        searchImage=findViewById(R.id.searchImage)

        movieAdapter= SearchViewAdapter(movieList,listener)

        searchRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager=GridLayoutManager(this@Test,2)
            adapter=movieAdapter
        }

        searchImage.setOnClickListener {
            if(searchEdt.text.isNotEmpty()) // Boş değil ise
            {
                MySearchTask(searchEdt.text.toString(),object :AsyncListener{
                    override fun onUpdate(movieModel: MovieModel) {
                        val lastIndex=movieList.size
                        movieList.add(lastIndex,movieModel)
                        searchRecyclerView.adapter?.notifyItemInserted(lastIndex)
                    }

                }).execute()
            }
        }

    }
}




private class  MySearchTask(val searchString:String,val listener:Test.AsyncListener) :
    AsyncTask<Void, MovieModel, Void>() {

    val movieList=ArrayList<MovieModel>()

    override fun doInBackground(vararg params: Void?): Void? {
        val doc = Jsoup.connect("https://www.imdb.com/find?q=$searchString&s=tt&ttype=ft&ref_=fn_ft").get()
        Log.i(TAG, doc.title())

        val elements = doc.getElementsByClass("result_text")
        Log.i(TAG, "list size :${elements.size}")
        elements.forEach {

            val movieDetailLink ="https://www.imdb.com/"+ it
                .select("a")
                .attr("href")
                .toString()

            val detayDoc = Jsoup.connect(movieDetailLink).get()

            val name= detayDoc
                .getElementsByClass("title_wrapper")
                .select("h1")
                .text()
                .toString()

            val titleYear= detayDoc
                .getElementsByClass("title_wrapper")
                .select("h1")
                .select("span")
                .text()
                .toString()

            val userRating= detayDoc
                .getElementsByClass("ratingValue")
                .select("strong")
                .attr("title")
                .toString()

            val ratingValue= detayDoc
                .getElementsByClass("ratingValue")
                .select("strong")
                .select("span")
                .text()
                .toString()

            val bestRating= detayDoc
                .getElementsByClass("ratingValue")
                .select("span")
                .text()
                .toString()

            val minute= detayDoc
                .getElementsByClass("subtext")
                .select("time")
                .text()
                .toString()


            val releaseDate= detayDoc
                .getElementsByClass("subtext")
                .select("a[title=See more release dates]")
                .text()
                .toString()


            val imageUrl= detayDoc
                .getElementsByClass("poster")
                .select("a")
                .select("img")
                .attr("src")
                .toString()


            val trailerUrl="https://www.imdb.com/"+
                    detayDoc
                .getElementsByClass("slate")
                .select("a")
                .attr("href")
                .toString()


            val summary=detayDoc
                .getElementsByClass("plot_summary ")
                .select("div[class=summary_text]")
                .text()
                .toString()

            val movie=MovieModel(name,imageUrl,trailerUrl,titleYear,userRating,ratingValue,bestRating,minute,releaseDate,summary)

            movieList.add(movie)
            onProgressUpdate(movie)




        }
        return null
    }

    override fun onProgressUpdate(vararg values: MovieModel?) {
        super.onProgressUpdate(*values)
        if (values[0]!=null) {
            listener.onUpdate(values[0]!!)
        }

    }
}

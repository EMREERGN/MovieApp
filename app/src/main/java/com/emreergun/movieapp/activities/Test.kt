package com.emreergun.movieapp.activities

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.emreergun.movieapp.R
import com.emreergun.movieapp.activities.Test.Companion.TAG
import org.jsoup.Jsoup

class Test : AppCompatActivity() {

    companion object{
        const val TAG="TEST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        MySearchTask("Lord").execute()


    }
}




private class  MySearchTask(val searchString:String) :
    AsyncTask<Void, Void, Void>() {

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

            val ratingUserRate= detayDoc
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


            val poster= detayDoc
                .getElementsByClass("poster")
                .select("a")
                .select("img")
                .attr("src")
                .toString()


            val trailerLink="https://www.imdb.com/"+
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


            Log.i(TAG, "movieName : $name\nsummary : $summary ")

        }
        return null
    }
}

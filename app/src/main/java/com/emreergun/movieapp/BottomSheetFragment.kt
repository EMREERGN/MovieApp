package com.emreergun.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import com.emreergun.movieapp.models.MovieModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(val movieModel: MovieModel):BottomSheetDialogFragment() {

    private lateinit var webview:WebView
    private lateinit var movieTitle:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        movieTitle.text=movieModel.name

        webview=view.findViewById(R.id.webviewBottomSheet)

        webview.settings.javaScriptEnabled = true
        webview.settings.allowFileAccess = true;
        webview.settings.allowUniversalAccessFromFileURLs=true;
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webview.loadUrl(movieModel.trailerLink)
        webview.invalidate()



    }



}
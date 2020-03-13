package com.emreergun.movieapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.emreergun.movieapp.models.MovieModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TrailerBottomSheetFragment(val movieModel: MovieModel):BottomSheetDialogFragment() {

    private lateinit var webview:WebView
    private lateinit var movieTitle:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        movieTitle=view.findViewById(R.id.movieNameTxtBottomSheet)
        webview=view.findViewById(R.id.webviewBottomSheet)

        movieTitle.text=movieModel.name



        webview.settings.javaScriptEnabled = true
        webview.settings.allowFileAccess = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webview.settings.mediaPlaybackRequiresUserGesture =false
        }

        webview.webViewClient=WebViewClient()

        webview.getSettings().setPluginState(WebSettings.PluginState.ON)
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webview.loadUrl(movieModel.trailerLink)
        webview.invalidate()



    }



}
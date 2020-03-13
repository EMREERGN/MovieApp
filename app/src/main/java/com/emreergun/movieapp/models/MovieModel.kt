package com.emreergun.movieapp.models

data class MovieModel(
    val name:String,
    val imageUrl:String,
    val trailerLink:String?,
    val titleYear:String?,
    val userRating:String?,
    val ratingValue:String?,
    val bestRating:String?,
    val minute:String?,
    val releaseDate:String?,
    val summary:String?
)
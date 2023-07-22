package com.openpay.pruebatecnica.util

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    const val TIME_OUT = 5
    const val ACCES_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxNjQyNjIyNWJlODI5MjI5NTk1ZDJmMzljYjk4NTYxYyIsInN1YiI6IjY0YjZmNjExZDAzNmI2MDBmMGVhMmUyZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.DwUgp3Yflpq2p9gccVNxbNnbGsgMMK8GUvVUzEz4j-k"
    private const val ACCOUNT_ID = "20169169"
    //Api Methods
    const val ACCOUNT_DETAILS = "account/${ACCOUNT_ID}"
    const val RATED_TV = "account/${ACCOUNT_ID}/rated/tv"
    const val RATED_MOVIES = "account/${ACCOUNT_ID}/rated/movies"
    const val POPULAR_MOVIES = "movie/popular?page=%s"
    const val TOP_RATED_MOVIES = "movie/top_rated?page=%s"
    const val TRENDING_MOVIES = "trending/movie/day"
}
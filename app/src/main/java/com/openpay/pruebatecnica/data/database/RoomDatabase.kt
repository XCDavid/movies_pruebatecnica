package com.openpay.pruebatecnica.data.database

import androidx.room.Database
import com.openpay.pruebatecnica.data.database.dao.PopularMovieDAO
import com.openpay.pruebatecnica.data.database.dao.ProfileDAO
import com.openpay.pruebatecnica.data.database.dao.RatedMovieDAO
import com.openpay.pruebatecnica.data.database.dao.RatedTVDAO
import com.openpay.pruebatecnica.data.database.dao.TopRatedMovieDAO
import com.openpay.pruebatecnica.data.database.dao.TrendingMovieDAO
import com.openpay.pruebatecnica.data.database.dao.UserDAO
import com.openpay.pruebatecnica.data.database.entity.PopularMovie
import com.openpay.pruebatecnica.data.database.entity.Profile
import com.openpay.pruebatecnica.data.database.entity.RatedMovie
import com.openpay.pruebatecnica.data.database.entity.RatedTV
import com.openpay.pruebatecnica.data.database.entity.TopRatedMovie
import com.openpay.pruebatecnica.data.database.entity.TrendingMovie
import com.openpay.pruebatecnica.data.database.entity.Usuario

@Database(
    entities = [Usuario::class, Profile::class, RatedTV::class, RatedMovie::class, PopularMovie::class, TopRatedMovie::class, TrendingMovie::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDatabase : androidx.room.RoomDatabase() {
    abstract fun getUserDAO(): UserDAO
    abstract fun getProfileDAO(): ProfileDAO
    abstract fun getRatedTVDAO(): RatedTVDAO
    abstract fun getRatedMovieDAO(): RatedMovieDAO
    abstract fun getPopularMovieDAO(): PopularMovieDAO
    abstract fun getTopRatedMovieDAO(): TopRatedMovieDAO
    abstract fun getTrendingMovieDAO(): TrendingMovieDAO

}
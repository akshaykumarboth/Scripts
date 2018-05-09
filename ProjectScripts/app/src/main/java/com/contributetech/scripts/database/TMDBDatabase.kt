package com.contributetech.scripts.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.contributetech.scripts.database.movieDetails.MovieDetail
import com.contributetech.scripts.database.movieDetails.MovieDetailsDao
import com.contributetech.scripts.database.moviesListItemDetail.MovieListItem
import com.contributetech.scripts.database.moviesListItemDetail.MovieListItemDao
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItemDao

@Database(entities = arrayOf(MovieListItem::class, TvShowListItem::class, MovieDetail::class), version = 1, exportSchema = false)
@TypeConverters(DataTypeConverter::class)

abstract class TMDBDatabase : RoomDatabase() {
    abstract fun getMovieListItemDao(): MovieListItemDao
    abstract fun getTvListItemDao(): TvShowListItemDao
    abstract fun getMovieDetailsDao(): MovieDetailsDao
}


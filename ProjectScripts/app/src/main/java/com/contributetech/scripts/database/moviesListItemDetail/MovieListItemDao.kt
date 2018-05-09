package com.contributetech.scripts.database.moviesListItemDetail

import android.arch.persistence.room.*
import io.reactivex.Maybe


@Dao
interface MovieListItemDao {

    @Query("Select * from MovieListItem where id in (:ids)")
    fun getMoviesByIds(ids: List<Int>) : Maybe<List<MovieListItem>>

    @Query("Select * from MovieListItem")
    fun getAllMovies() : List<MovieListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMoviesList(movies:List<MovieListItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieListItem)

    @Update
    fun updateMovie(movie: MovieListItem)

    @Update
    fun updateMoviesList(movies:List<MovieListItem>)

    @Delete
    fun deleteMovie(movie: MovieListItem)

    @Delete
    fun deleteMoviesList(movies:List<MovieListItem>)
}
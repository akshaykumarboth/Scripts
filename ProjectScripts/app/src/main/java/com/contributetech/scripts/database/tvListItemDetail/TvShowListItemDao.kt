package com.contributetech.scripts.database.tvListItemDetail

import android.arch.persistence.room.*
import io.reactivex.Maybe

@Dao
interface TvShowListItemDao {

    @Query("Select * from TvShowListItem where id in (:ids)")
    fun getTvShowsById(ids: List<Int>): Maybe<List<TvShowListItem>>

    @Query("Select * from TvShowListItem")
    fun getAllTvShows(): List<TvShowListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShowList(movies: List<TvShowListItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShow(movie: TvShowListItem)

    @Update
    fun updateTvShow(movie: TvShowListItem)

    @Update
    fun updateTvShowList(movies: List<TvShowListItem>)

    @Delete
    fun deleteTvShow(movie: TvShowListItem)

    @Delete
    fun deleteTvShowList(movies: List<TvShowListItem>)
}
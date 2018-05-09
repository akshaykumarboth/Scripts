package com.contributetech.scripts.database.movieDetails

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface MovieDetailsDao {
    @Query("Select * from MovieDetail where id in (:id)")
    fun getMovieDetails(id:Int): MovieDetail
}
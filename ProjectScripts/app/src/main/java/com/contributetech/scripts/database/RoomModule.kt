package com.contributetech.scripts.database

import com.contributetech.scripts.application.ScriptsApplication
import dagger.Module
import android.arch.persistence.room.Room
import com.contributetech.scripts.database.movieDetails.MovieDetailsDao
import com.contributetech.scripts.database.moviesListItemDetail.MovieListItemDao
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItemDao
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: ScriptsApplication) {

    var mdatabase:TMDBDatabase = Room.databaseBuilder(mApplication, TMDBDatabase::class.java, "tmdb-db").fallbackToDestructiveMigration().build();

    @Singleton
    @Provides
    fun provideTMDBDb   (): TMDBDatabase {
        return mdatabase
    }

    @Singleton
    @Provides
    fun getMovieListItemDao(mdatabase: TMDBDatabase): MovieListItemDao {
        return mdatabase.getMovieListItemDao()
    }

    @Singleton
    @Provides
    fun getTvDetailsDao(mdatabase: TMDBDatabase): TvShowListItemDao {
        return mdatabase.getTvListItemDao()
    }

    @Singleton
    @Provides
    fun getMovieDetailsDao(mdatabase: TMDBDatabase): MovieDetailsDao {
        return mdatabase.getMovieDetailsDao()
    }
}
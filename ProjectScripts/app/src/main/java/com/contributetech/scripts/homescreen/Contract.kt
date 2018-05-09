package com.contributetech.scripts.homescreen

import com.contributetech.scripts.database.DBCallBacks
import com.contributetech.scripts.database.moviesListItemDetail.MovieListItem
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem
import com.contributetech.scripts.network.responseVo.ShowListResponseVO
import com.contributetech.scripts.network.responseVo.ShowsResponseVO
import com.contributetech.scripts.network.responseVo.TvShowsResponseVO

class Contract {
    class Movies {
        interface ActivityContract {
            fun fetchMovies()
            fun storeMoviesForNowPlaying(moviesList: List<MovieListItem>):ArrayList<Int>
            fun storeMoviesForUpcoming(moviesList: List<MovieListItem>):ArrayList<Int>
            fun storeMoviesForTopRated(moviesList: List<MovieListItem>):ArrayList<Int>
            fun storeMoviesForPopular(moviesList: List<MovieListItem>):ArrayList<Int>
            fun fetchMovieListFromDb(moviesIds: ArrayList<Int>, callback:DBCallBacks.Movies.MovieListTask)
            fun onMovieClick(id:Int)
        }
        interface FragmentContract {
            fun handleNowShowingResults(nowShowingResponse: ShowListResponseVO)
            fun handlePopularMoviesResults(popularMoviesResult: ShowsResponseVO)
            fun handleUpcomingMoviesResults(upComingMovies: ShowListResponseVO)
            fun handleTopRatedMoviesResults(topRatedMovies: ShowsResponseVO)
            fun subscribeToNowPlaying()
            fun subscribeToTopRatedMovies()
            fun subscribeToPopularMovies()
            fun subscribeToUpcomingMovies()
        }
    }
    class TvShows {
        interface ActivityContract {
            fun fetchTvShows()
            fun storeAiringTodayForTv(tvShowList: List<TvShowListItem>):ArrayList<Int>
            fun storeOnTheAirShowsForTv(tvShowList: List<TvShowListItem>):ArrayList<Int>
            fun storePopularShowsForTv(tvShowList: List<TvShowListItem>):ArrayList<Int>
            fun storeTopRatedShowsForTv(tvShowList: List<TvShowListItem>):ArrayList<Int>
            fun fetchTvListFromDb(tvIdsList: ArrayList<Int>, callback:DBCallBacks.Movies.TvListTask)
            fun onTvShowClick(id:Int)
        }
        interface FragmentContract {
            fun handleTvResult(tvShowsResponse: TvShowsResponseVO, listType: String)
            fun subscribeToAiringToday()
            fun subscribeToTopRatedTv()
            fun subscribeToPopularTv()
            fun subscribeToOnAirTv()
        }
    }
}
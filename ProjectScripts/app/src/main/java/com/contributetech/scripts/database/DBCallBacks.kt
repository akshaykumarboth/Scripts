package com.contributetech.scripts.database

import com.contributetech.scripts.database.moviesListItemDetail.MovieListItem
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem

class DBCallBacks {
    class Movies {
        interface MovieListTask {
            fun onSuccess(movieList: List<MovieListItem>)
            fun onFailure(error:Throwable)
        }
        interface TvListTask {
            fun onSuccess(movieList: List<TvShowListItem>)
            fun onFailure(error:Throwable)
        }
    }
}
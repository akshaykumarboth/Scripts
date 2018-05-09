package com.contributetech.scripts.homescreen

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.contributetech.scripts.R
import com.contributetech.scripts.database.DBCallBacks
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem
import com.contributetech.scripts.network.responseVo.TvShowsResponseVO


class HomeTvShowFragment: Fragment(), Contract.TvShows.FragmentContract {

    var airingTodayTvList:ArrayList<Int> = arrayListOf()
    var popularTvList:ArrayList<Int> = arrayListOf()
    var onAirTvList:ArrayList<Int> = arrayListOf()
    var topRatedTvList:ArrayList<Int> = arrayListOf()

    lateinit var vpCarousel: ViewPager
    lateinit var pagerAdapter:TvCarouselPagerAdapter
    lateinit var tvStringLabel1: TextView
    lateinit var tvStringLabel2: TextView
    lateinit var tvStringLabel3: TextView

    lateinit var rvOnAirTv: RecyclerView
    lateinit var rvPopularTv: RecyclerView
    lateinit var rvTopRatedTv: RecyclerView
    lateinit var adapterPopularTv:HorizontalTvListRecyclerAdapter
    lateinit var adapterTopRatedMovies:HorizontalTvListRecyclerAdapter
    lateinit var adapterOnAirTv:HorizontalTvListRecyclerAdapter

    lateinit var mActivityContract:Contract.TvShows.ActivityContract

    companion object {
        val ON_AIRING_TODAY:String = "ON_AIRING_TODAY"
        val ON_AIR:String = "ON_AIR"
        val POPULAR_TV:String = "POPULAR_TV"
        val TOP_RATED_TV:String = "TOP_RATED_TV"

        fun newInstance():HomeTvShowFragment {
            val fragment = HomeTvShowFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home_screen, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        val context: Context = activity as Context
        vpCarousel = view.findViewById(R.id.vp_carousel)
        pagerAdapter = TvCarouselPagerAdapter(context)
        vpCarousel.adapter = pagerAdapter
        adapterOnAirTv = HorizontalTvListRecyclerAdapter(context)
        adapterTopRatedMovies = HorizontalTvListRecyclerAdapter(context)
        adapterPopularTv = HorizontalTvListRecyclerAdapter(context)

        rvOnAirTv = view.findViewById(R.id.rv_popular_movies)
        rvTopRatedTv = view.findViewById(R.id.rv_top_rated_movies)
        rvPopularTv = view.findViewById(R.id.rv_upcoming_movies)
        rvOnAirTv.isNestedScrollingEnabled = false
        rvTopRatedTv.isNestedScrollingEnabled = false
        rvPopularTv.isNestedScrollingEnabled = false
        rvOnAirTv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTopRatedTv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPopularTv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvOnAirTv.adapter = adapterOnAirTv
        rvPopularTv.adapter = adapterPopularTv
        rvTopRatedTv.adapter = adapterTopRatedMovies

        initLabels(view)
    }

    private fun initLabels(view:View) {
        tvStringLabel1 = view.findViewById(R.id.tv_string_pop_movies)
        tvStringLabel2 = view.findViewById(R.id.tv_string_upcoiming_movies)
        tvStringLabel3 = view.findViewById(R.id.tv_string_top_rated_movies)
        tvStringLabel1.setText("On Air Shows")
        tvStringLabel2.setText("Popular Shows")
        tvStringLabel3.setText("Top Rated Shows")
    }

    override fun subscribeToAiringToday() {
        if(!(activity?.isFinishing)!!) {
            mActivityContract.fetchTvListFromDb(airingTodayTvList, object: DBCallBacks.Movies.TvListTask {
                override fun onSuccess(movieList: List<TvShowListItem>) {
                    setAiringTodayList(movieList)
                }

                override fun onFailure(error:Throwable) {
                    handleError(error)
                }

            })
        }
    }

    override fun subscribeToOnAirTv() {
        if(!(activity?.isFinishing)!!) {
            mActivityContract.fetchTvListFromDb(onAirTvList, object: DBCallBacks.Movies.TvListTask {
                override fun onSuccess(movieList: List<TvShowListItem>) {
                    setOnAirList(movieList)
                }

                override fun onFailure(error:Throwable) {
                    handleError(error)
                }

            })
        }

    }

    override fun subscribeToTopRatedTv() {
        if(!(activity?.isFinishing)!!) {
            mActivityContract.fetchTvListFromDb(topRatedTvList, object: DBCallBacks.Movies.TvListTask {
                override fun onSuccess(tvIdsList: List<TvShowListItem>) {
                    setTopRatedMovieList(tvIdsList)
                }

                override fun onFailure(error:Throwable) {
                    handleError(error)
                }

            })
        }

    }

    private fun handleError(error: Throwable) {

    }

    override fun subscribeToPopularTv() {
        if(!(activity?.isFinishing)!!) {
            mActivityContract.fetchTvListFromDb(popularTvList, object: DBCallBacks.Movies.TvListTask {
                override fun onSuccess(movieList: List<TvShowListItem>) {
                    setPopularTvList(movieList)
                }

                override fun onFailure(error:Throwable) {
                    handleError(error)
                }

            })
        }

    }

    private fun setPopularTvList(tvList :List<TvShowListItem>) {
        if(!tvList.isEmpty())
            adapterPopularTv.setData(tvList as ArrayList<TvShowListItem>)
    }

    private fun setTopRatedMovieList(tvShowList:List<TvShowListItem>) {
        if(!tvShowList.isEmpty())
            adapterTopRatedMovies.setData(tvShowList as ArrayList<TvShowListItem>)
    }

    private fun setOnAirList(tvShowList:List<TvShowListItem>) {
        if(!tvShowList.isEmpty())
            adapterOnAirTv.setData(tvShowList as ArrayList<TvShowListItem>)
    }

    private fun setAiringTodayList(tvShowList:List<TvShowListItem>) {
        if(!tvShowList.isEmpty())
            pagerAdapter.setData(tvShowList as ArrayList<TvShowListItem>)
    }

    override fun handleTvResult(tvShowsResponse: TvShowsResponseVO, type:String) {
        when(type) {
            ON_AIRING_TODAY -> {
                val tvShowsList : List<TvShowListItem> = tvShowsResponse.results;
                airingTodayTvList = mActivityContract.storeAiringTodayForTv(tvShowsList);
            }
            ON_AIR -> {
                val tvShowsList : List<TvShowListItem> = tvShowsResponse.results;
                onAirTvList = mActivityContract.storeOnTheAirShowsForTv(tvShowsList);
            }
            POPULAR_TV -> {
                val tvShowsList : List<TvShowListItem> = tvShowsResponse.results;
                popularTvList = mActivityContract.storePopularShowsForTv(tvShowsList);
            }
            TOP_RATED_TV -> {
                val tvShowsList : List<TvShowListItem> = tvShowsResponse.results;
                topRatedTvList = mActivityContract.storeTopRatedShowsForTv(tvShowsList);
            }
            else  -> {
            return
            }
        }

    }
}
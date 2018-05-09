package com.contributetech.scripts.homescreen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import com.contributetech.scripts.R
import com.contributetech.scripts.application.ScriptsApplication
import com.contributetech.scripts.database.DBCallBacks
import com.contributetech.scripts.database.moviesListItemDetail.MovieListItem
import com.contributetech.scripts.database.moviesListItemDetail.MovieListItemDao
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItemDao
import com.contributetech.scripts.movieDetail.MovieDetailsActivity
import com.contributetech.scripts.network.ParamsUtil
import com.contributetech.scripts.network.TMDBApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), Contract.Movies.ActivityContract, Contract.TvShows.ActivityContract{

    @Inject
    lateinit var api:TMDBApi

    @Inject
    lateinit var mMovieDetailsDao: MovieListItemDao

    @Inject
    lateinit var mTvDetailsDao: TvShowListItemDao


    lateinit var vpExperienceType:ViewPager
    lateinit var vpPageChangeListener:ViewPager.OnPageChangeListener
    lateinit var adapterExperienceType:PagerExperienceTypeAdapter

    var subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as ScriptsApplication).mAppComponent.inject((this))
        initView()
    }

    private fun initView() {
        var nsvHomeContainer:NestedScrollView = findViewById(R.id.nsv_home_scrool_container)
        nsvHomeContainer.isFillViewport = true

        vpExperienceType = findViewById(R.id.vp_experience_type)
        vpPageChangeListener = object:ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

                val frag:android.support.v4.app.Fragment? = adapterExperienceType.getFragment(position)
                if(vpExperienceType.currentItem == 0) {
                    if(frag != null) {
                        val movieFrag: HomeMoviesFragment = frag as HomeMoviesFragment
                        if (movieFrag.nowShowingMovieList.isEmpty()) {
                            movieFrag.mActivityContract.fetchMovies()
                        }
                    }
                }
                else {
                    if(frag != null) {
                        val movieFrag: HomeTvShowFragment = frag as HomeTvShowFragment
                        if (movieFrag.onAirTvList.isEmpty()) {
                            movieFrag.mActivityContract.fetchTvShows()
                        }
                    }
                }
            }

        };
        vpExperienceType.addOnPageChangeListener(vpPageChangeListener)
        adapterExperienceType = PagerExperienceTypeAdapter(supportFragmentManager, this as Contract.Movies.ActivityContract, this as Contract.TvShows.ActivityContract)
        vpExperienceType.adapter = adapterExperienceType
        vpExperienceType.postDelayed(Runnable {
                        vpExperienceType.setCurrentItem(0);
                        vpPageChangeListener.onPageSelected(0)
        }, 100);
    }

    override fun fetchMovies() {
        fetchNowShowingMoviesResults()
        fetchPopularMoviesResults()
        fetchUpcomingMoviesResults()
        fetchTopRatedMoviesResults()
    }

    private fun fetchNowShowingMoviesResults() {
        fetchResposeForCategory(HomeMoviesFragment.NOW_SHOWING_MOVIES)

    }

    private fun fetchPopularMoviesResults() {
        fetchResposeForCategory(HomeMoviesFragment.POPULAR_MOVIES)

    }

    private fun fetchUpcomingMoviesResults() {
        fetchResposeForCategory(HomeMoviesFragment.UPCOMING_MOVIES)

    }

    private fun fetchTopRatedMoviesResults() {
        fetchResposeForCategory(HomeMoviesFragment.TOP_RATED_MOVIES)
    }

    private fun handleError(error:Throwable) {
    }

    override fun storeMoviesForNowPlaying(moviesList: List<MovieListItem>): ArrayList<Int> {
        val contract:Contract.Movies.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.Movies.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(movie: MovieListItem in moviesList) {
            newList.add(movie.id)
        }
        val thread = Thread({
            mMovieDetailsDao.insertMoviesList(moviesList)
            runOnUiThread() {
                contract.subscribeToNowPlaying()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun storeMoviesForUpcoming(moviesList: List<MovieListItem>): ArrayList<Int> {
        val contract:Contract.Movies.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.Movies.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(movie: MovieListItem in moviesList) {
            newList.add(movie.id)
        }
        val thread = Thread({
            mMovieDetailsDao.insertMoviesList(moviesList)
            runOnUiThread() {
                contract.subscribeToUpcomingMovies()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun storeMoviesForTopRated(moviesList: List<MovieListItem>): ArrayList<Int> {
        val contract:Contract.Movies.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.Movies.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(movie: MovieListItem in moviesList) {
            newList.add(movie.id)
        }
        val thread = Thread({
            mMovieDetailsDao.insertMoviesList(moviesList as ArrayList<MovieListItem>)
            runOnUiThread() {
                contract.subscribeToTopRatedMovies()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun storeMoviesForPopular(moviesList: List<MovieListItem>): ArrayList<Int> {
        val contract:Contract.Movies.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.Movies.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(movie: MovieListItem in moviesList) {
                newList.add(movie.id)
        }
        val thread = Thread({
            mMovieDetailsDao.insertMoviesList(moviesList)
            runOnUiThread() {
                contract.subscribeToPopularMovies()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun fetchMovieListFromDb(moviesIds: ArrayList<Int>, callback:DBCallBacks.Movies.MovieListTask) {
        mMovieDetailsDao.getMoviesByIds(moviesIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it -> callback.onSuccess(it)
                }, {
                    it -> callback.onFailure(it)
                })
    }

    override fun fetchTvShows() {
        fetchAiringTodayTvShows()
        fetchOnAirTvShows()
        fetchPopularTvShows()
        fetchTopRatedTvShows()
    }

    private fun fetchAiringTodayTvShows() {
        fetchResposeForCategory(HomeTvShowFragment.ON_AIRING_TODAY)
    }

    private fun fetchOnAirTvShows() {
        fetchResposeForCategory(HomeTvShowFragment.ON_AIR)
    }

    private fun fetchPopularTvShows() {
        fetchResposeForCategory(HomeTvShowFragment.POPULAR_TV)
    }

    private fun fetchTopRatedTvShows() {
        fetchResposeForCategory(HomeTvShowFragment.TOP_RATED_TV)
    }

    private fun fetchResposeForCategory(type:String) {
        var disposable:Disposable
        when(type) {
            HomeTvShowFragment.ON_AIRING_TODAY -> {
                val contract:Contract.TvShows.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeTvShowFragment)
                disposable = api.getTvAiringToday(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handleTvResult(it, HomeTvShowFragment.ON_AIRING_TODAY)}, { error -> handleError(error) })
            }

            HomeTvShowFragment.ON_AIR -> {
                val contract:Contract.TvShows.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeTvShowFragment)
                disposable = api.getTvOnAir(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handleTvResult(it, HomeTvShowFragment.ON_AIR)}, { error -> handleError(error) })
            }

            HomeTvShowFragment.POPULAR_TV -> {
                val contract:Contract.TvShows.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeTvShowFragment)
                disposable = api.getPopularTv(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handleTvResult(it, HomeTvShowFragment.POPULAR_TV)}, { error -> handleError(error) })
            }

            HomeTvShowFragment.TOP_RATED_TV -> {
                val contract:Contract.TvShows.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeTvShowFragment)
                disposable = api.getTopRatedTv(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handleTvResult(it, HomeTvShowFragment.TOP_RATED_TV)}, { error -> handleError(error) })
            }

            HomeMoviesFragment.NOW_SHOWING_MOVIES -> {
                val contract:Contract.Movies.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeMoviesFragment)
                disposable = api.getNowPlayingMovies(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handleNowShowingResults(it)}, { error -> handleError(error) })
            }

            HomeMoviesFragment.TOP_RATED_MOVIES -> {
                val contract:Contract.Movies.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeMoviesFragment)
                disposable = api.getTopRatedMovies(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handleTopRatedMoviesResults(it)}, { error -> handleError(error) })
            }

            HomeMoviesFragment.POPULAR_MOVIES -> {
                val contract:Contract.Movies.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeMoviesFragment)
                disposable = api.getPopularMovies(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handlePopularMoviesResults(it)}, { error -> handleError(error) })
            }

            HomeMoviesFragment.UPCOMING_MOVIES -> {
                val contract:Contract.Movies.FragmentContract = (adapterExperienceType.getFragment(vpExperienceType.currentItem) as HomeMoviesFragment)
                disposable = api.getUpcomingMovies(ParamsUtil.getNowShowingParam())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({    it -> contract.handleUpcomingMoviesResults(it)}, { error -> handleError(error) })
            }
            else ->{
                return
            }
        }
        subscriptions.add(disposable)
    }

    override fun storeAiringTodayForTv(tvShowList: List<TvShowListItem>): ArrayList<Int> {
        val contract:Contract.TvShows.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.TvShows.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(tvShow: TvShowListItem in tvShowList) {
            newList.add(tvShow.id)
        }
        val thread = Thread({
            mTvDetailsDao.insertTvShowList(tvShowList)
            runOnUiThread() {
                contract.subscribeToAiringToday()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun storeOnTheAirShowsForTv(tvShowList: List<TvShowListItem>): ArrayList<Int> {
        val contract:Contract.TvShows.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.TvShows.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(movie: TvShowListItem in tvShowList) {
            newList.add(movie.id)
        }
        val thread = Thread({
            mTvDetailsDao.insertTvShowList(tvShowList)
            runOnUiThread() {
                contract.subscribeToOnAirTv()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun storePopularShowsForTv(tvShowList: List<TvShowListItem>): ArrayList<Int> {
        val contract:Contract.TvShows.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.TvShows.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(movie: TvShowListItem in tvShowList) {
            newList.add(movie.id)
        }
        val thread = Thread({
            mTvDetailsDao.insertTvShowList(tvShowList)
            runOnUiThread() {
                contract.subscribeToPopularTv()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun storeTopRatedShowsForTv(tvShowList: List<TvShowListItem>): ArrayList<Int> {
        val contract:Contract.TvShows.FragmentContract = adapterExperienceType.getFragment(vpExperienceType.currentItem) as Contract.TvShows.FragmentContract
        val newList:MutableList<Int> = ArrayList();
        for(movie: TvShowListItem in tvShowList) {
            newList.add(movie.id)
        }
        val thread = Thread({
            mTvDetailsDao.insertTvShowList(tvShowList)
            runOnUiThread() {
                contract.subscribeToTopRatedTv()
            }
        })
        thread.start()
        return newList as ArrayList<Int>;
    }

    override fun fetchTvListFromDb(tvIdsList: ArrayList<Int>, callback: DBCallBacks.Movies.TvListTask) {
        mTvDetailsDao.getTvShowsById(tvIdsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it -> callback.onSuccess(it)
                }, {
                    it -> callback.onFailure(it)
                })
    }

    override fun onMovieClick(id: Int) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("id", id);
        startActivity(intent)
    }

    override fun onTvShowClick(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}



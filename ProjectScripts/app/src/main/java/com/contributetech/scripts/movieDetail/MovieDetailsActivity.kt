package com.contributetech.scripts.movieDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.contributetech.scripts.R
import com.contributetech.scripts.application.ScriptsApplication
import com.contributetech.scripts.database.movieDetails.MovieDetail
import com.contributetech.scripts.database.movieDetails.MovieDetailsDao
import com.contributetech.scripts.network.NetworkImageUtil
import com.contributetech.scripts.network.ParamsUtil
import com.contributetech.scripts.network.TMDBApi
import com.contributetech.scripts.util.ImageUtil
import com.facebook.drawee.view.SimpleDraweeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsActivity:AppCompatActivity() {

    var movieId:Int = -1

    @Inject
    lateinit var mMoviesDao:MovieDetailsDao

    @Inject
    lateinit var mTMDBApi:TMDBApi

    lateinit var vpCarousel:ViewPager
    lateinit var imageCarouselAdapter:PagerImageCarouselAdapter
    lateinit var sdvPoster:SimpleDraweeView
    lateinit var tvTitle:TextView
    lateinit var tvRating:TextView

    private var mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        (application as ScriptsApplication).mAppComponent.inject((this))
        val intent:Intent = intent
        if(intent.hasExtra("id")) {
            movieId = intent.getIntExtra("id", -1)
        }
        initView()
        if(movieId != -1) {
            fetchMovie()
        }
    }

    private fun initView() {
        vpCarousel = findViewById(R.id.vp_movie_carousel)
        imageCarouselAdapter = PagerImageCarouselAdapter(this)
        vpCarousel.adapter = imageCarouselAdapter
        sdvPoster = findViewById(R.id.sdv_poster_image)
        tvTitle = findViewById(R.id.tv_title)
        tvRating = findViewById(R.id.tv_rating)
    }

    private fun fetchMovie() {
        val disposable = mTMDBApi.getMovieDetail(movieId, ParamsUtil.getNowShowingParam())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setData(it)
                }, {
                    handleError(it)
                })
        mDisposables.add(disposable)
    }

    private fun handleError(throwable: Throwable) {

    }

    private fun setData(it: MovieDetail) {
        setMovieDetails(it)
    }

    private fun setMovieDetails(movie: MovieDetail) {
        setPosterImage(movie.posterPath)
        if(movie.title != null) {
            tvTitle.setText(movie.title)
        }
        if(movie.voteAvg != null) {
            tvRating.setText(movie.voteAvg.toString())
        }
        getCarouselImages(movie)
    }

    private fun getCarouselImages(movie:MovieDetail) {
        val mImageList:MutableList<String> = mutableListOf()
        val url:String? = movie.backdropPath
        if(url != null) {
            mImageList.add(url)
        }
        imageCarouselAdapter.setData(mImageList as ArrayList<String>)
    }

    private fun setPosterImage(imageUrl:String?) {
        if (imageUrl != null) {
            val path: String = NetworkImageUtil.getImagePath(imageUrl, ImageUtil.LandscapeSizes.large_size)
            val uri = Uri.parse(path)
            sdvPoster.setImageURI(uri.toString())
        }
    }
}
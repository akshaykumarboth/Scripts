package com.contributetech.scripts.homescreen

import android.content.Context
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.View
import com.contributetech.scripts.database.moviesListItemDetail.MovieListItem
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import com.contributetech.scripts.R
import com.contributetech.scripts.commonListeners.IMovieClick
import com.contributetech.scripts.network.NetworkImageUtil
import com.contributetech.scripts.util.ImageUtil
import com.facebook.drawee.view.SimpleDraweeView


class MovieCarouselPagerAdapter(var context:Context) : PagerAdapter() {

    var movieList:ArrayList<MovieListItem> = ArrayList()
    var onClickListener: IMovieClick? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return movieList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.caraousel_item_view, null)

        val tvTitle = view.findViewById(R.id.tv_title) as TextView
        val fivShow = view.findViewById(R.id.iv_show) as SimpleDraweeView

        val movie: MovieListItem = movieList.get(position)
        tvTitle.setText(movie.originalTitle)
        if(movie.backdropPath != null) {
            val path: String = NetworkImageUtil.getImagePath(movie.backdropPath, ImageUtil.LandscapeSizes.large_size)
            val uri = Uri.parse(path)
            fivShow.setImageURI(uri)
        }

        view.setOnClickListener { v -> onClickListener?.onMovieClick(movie.id)}
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

    fun setData(newList:ArrayList<MovieListItem>) {
        movieList = newList;
        notifyDataSetChanged();
    }
}
package com.contributetech.scripts.movieDetail

import android.content.Context
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.contributetech.scripts.R
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem
import com.contributetech.scripts.network.NetworkImageUtil
import com.contributetech.scripts.util.ImageUtil
import com.facebook.drawee.view.SimpleDraweeView

class PagerImageCarouselAdapter(var context:Context):PagerAdapter() {

    var mMovieImagesUrls: ArrayList<String> = ArrayList()


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mMovieImagesUrls.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.image_carousel_item_view, null)
        val imageUrl: String = mMovieImagesUrls.get(position)
        val fivImage: SimpleDraweeView = view.findViewById(R.id.sdv_image)
        if (imageUrl != null) {
            val path: String = NetworkImageUtil.getImagePath(imageUrl, ImageUtil.LandscapeSizes.large_size)
            val uri = Uri.parse(path)
            fivImage.setImageURI(uri.toString())
        }

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

    fun setData(newList: ArrayList<String>) {
        mMovieImagesUrls = newList;
        notifyDataSetChanged();
    }
}
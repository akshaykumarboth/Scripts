package com.contributetech.scripts.homescreen

import android.content.Context
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.View
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import com.contributetech.scripts.R
import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem
import com.contributetech.scripts.network.NetworkImageUtil
import com.contributetech.scripts.util.ImageUtil
import com.facebook.drawee.view.SimpleDraweeView


class TvCarouselPagerAdapter(var context:Context) : PagerAdapter() {

    var tvShowList:ArrayList<TvShowListItem> = ArrayList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return tvShowList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.caraousel_item_view, null)

        val tvTitle = view.findViewById(R.id.tv_title) as TextView
        val fivShow = view.findViewById(R.id.iv_show) as SimpleDraweeView

        val tvShow: TvShowListItem = tvShowList.get(position)
        tvTitle.setText(tvShow.name)
        if(tvShow.backdropPath != null) {
            val path: String = NetworkImageUtil.getImagePath(tvShow.backdropPath, ImageUtil.LandscapeSizes.large_size)
            val uri = Uri.parse(path)
            fivShow.setImageURI(uri.toString())
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

    fun setData(newList:ArrayList<TvShowListItem>) {
        tvShowList = newList;
        notifyDataSetChanged();
    }

}
package com.contributetech.scripts.homescreen

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.ViewGroup

class PagerExperienceTypeAdapter(fragmentManager: FragmentManager, var mActivityMoviesContract: Contract.Movies.ActivityContract, var mActivityTvContract: Contract.TvShows.ActivityContract):FragmentStatePagerAdapter(fragmentManager) {

    val SIZE:Int = 2
    var firstFrag:HomeMoviesFragment? = null
    var secondFrag:HomeTvShowFragment? = null

    override fun getItem(position: Int): Fragment {
        when (position ) {
            0 -> {
                firstFrag = HomeMoviesFragment.newInstance()
                firstFrag?.mActivityContract = mActivityMoviesContract
                return firstFrag as HomeMoviesFragment
            }
            else -> {
                secondFrag = HomeTvShowFragment.newInstance()
                secondFrag?.mActivityContract = mActivityTvContract
                return secondFrag as HomeTvShowFragment
            }
        }
    }

    override fun getCount(): Int {
        return SIZE
    }

    fun getFragment(position:Int) : Fragment? {
        if (position == 0) {
            if (firstFrag != null) {
                return firstFrag as HomeMoviesFragment
            }
        }else {
            if (secondFrag != null) {
                return secondFrag as HomeTvShowFragment
            }
        }
       return null
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if(position == 0) {
            if(firstFrag == null){
                firstFrag = super.instantiateItem(container, position) as HomeMoviesFragment
            }
            firstFrag?.mActivityContract = mActivityMoviesContract
            return firstFrag as HomeMoviesFragment
        }
        else {
            if(secondFrag == null){
                secondFrag = super.instantiateItem(container, position) as HomeTvShowFragment
            }
            secondFrag?.mActivityContract = mActivityTvContract
            return secondFrag as HomeTvShowFragment
        }
    }
}
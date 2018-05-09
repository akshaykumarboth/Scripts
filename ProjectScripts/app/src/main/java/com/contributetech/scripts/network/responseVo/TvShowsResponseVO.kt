package com.contributetech.scripts.network.responseVo

import com.contributetech.scripts.database.tvListItemDetail.TvShowListItem
import com.google.gson.annotations.SerializedName

data class TvShowsResponseVO(val page:Int,
                             @SerializedName("total_results") val totalResults:Int,
                             @SerializedName("total_pages") val totalPages:Int,
                             val results:List<TvShowListItem>)
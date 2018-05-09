package com.contributetech.scripts.database.movieDetails

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.contributetech.scripts.database.DataTypeConverter
import com.contributetech.scripts.database.Genre
import com.contributetech.scripts.database.ProductionCompany
import com.google.gson.annotations.SerializedName

@Entity
class MovieDetail (
        @PrimaryKey
        var id:Int,

        var adult:Boolean,

        @ColumnInfo(name = "backdrop_path")
        var backdropPath:String?,

        var budget:Int,

        @TypeConverters(DataTypeConverter::class)
        var genres:ArrayList<Genre>,

        var homepage:String?,

        @ColumnInfo(name = "original_language")
        var originalLanguage:String?,

        @ColumnInfo(name = "original_title")
        var originalTitle:String?,

        var overview:String?,

        var popularity:Float,

        @ColumnInfo(name = "poster_path")
        var posterPath:String?,

        @ColumnInfo(name = "production_companies")
        @TypeConverters(DataTypeConverter::class)
        var productionCompanies:ArrayList<ProductionCompany>?,

        @ColumnInfo(name = "release_date")
        var releaseDate:String?,

        var revenue:Int,

        var runtime:Int,

        var status:String?,

        var tagline:String?,

        var title:String?,

        var video:Boolean,

        @ColumnInfo(name = "vote_count")
        var voteCount:Int,

        @ColumnInfo(name = "vote_average")
        @SerializedName( "vote_average")
        var voteAvg:Float
)

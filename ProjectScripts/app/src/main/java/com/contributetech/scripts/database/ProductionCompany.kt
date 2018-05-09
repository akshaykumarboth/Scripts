package com.contributetech.scripts.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class ProductionCompany (
    @PrimaryKey
    var id:Int,

    var name:String?,

    @ColumnInfo(name = "logo_path")
    var logoPath:String?,

    @ColumnInfo(name = "original_country")
    var originalCountry:String?
    )
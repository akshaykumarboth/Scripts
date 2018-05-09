package com.contributetech.scripts.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Genre(
        @PrimaryKey
        var id:Int,
        var name:String
)
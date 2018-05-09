package com.contributetech.scripts.database

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser




class DataTypeConverter {
    val gson: Gson = Gson()
    @TypeConverter
    fun stringToIntList(data: String): List<Int> {
        val idList: List<Int> = emptyList()
        if (data.length > 2) {
            val newData = data.substring(1, data.length - 1)
            val ids: List<String> = newData.split(",")
            for (id: String in ids) {
                idList.plus(id.toInt())
            }
        }
        return idList
    }

    @TypeConverter
    fun intListToString(list: List<Int>): String {
        return gson.toJson(list).toString();
    }

    @TypeConverter
    fun jsonObjToGenreList(genres: String): ArrayList<Genre> {
        val parser = JsonParser()
        val tradeElement = parser.parse(genres)
        val genresArray:JsonArray = tradeElement.getAsJsonArray()
        val genres: MutableList<Genre> = mutableListOf()
        for (i in 0..genresArray.size()) {
            val genreObj: JsonObject = genresArray.get(i) as JsonObject
            val id: Int = genreObj.get("id").asInt
            val name: String = genreObj.get("name").asString
            if (id != null && name != null) {
                val genre = Genre(id, name)
                genres.add(genre)
            }
        }
        return genres as ArrayList<Genre>
    }

    @TypeConverter
    fun GenreListToJsonObjString(genres: ArrayList<Genre>): String {
        return gson.toJson(genres).toString();
    }

    @TypeConverter
    fun jsonObjToPCList(pcArrayString: String): ArrayList<ProductionCompany> {
        val parser = JsonParser()
        val tradeElement = parser.parse(pcArrayString)
        val pcJsonArray:JsonArray = tradeElement.getAsJsonArray()
        val productionCompanies: MutableList<ProductionCompany> = mutableListOf()
        for (i in 0..pcJsonArray.size()) {
            val genreObj: JsonObject = pcJsonArray.get(i) as JsonObject
            val id: Int = genreObj.get("id").asInt
            val name: String = genreObj.get("name").asString
            val logoPath: String = genreObj.get("logo_path").asString
            val orgCountry: String = genreObj.get("original_country").asString
            if (id != null && name != null) {
                val prodCompany = ProductionCompany(id, name, logoPath, orgCountry)
                productionCompanies.add(prodCompany)
            }
        }
        return productionCompanies as ArrayList<ProductionCompany>
    }

    @TypeConverter
    fun PCListToJsonObjString(pcs: ArrayList<ProductionCompany>): String {
        return gson.toJson(pcs).toString();
    }
}


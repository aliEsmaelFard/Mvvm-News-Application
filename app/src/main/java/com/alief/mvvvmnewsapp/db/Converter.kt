package com.alief.mvvvmnewsapp.db

import androidx.room.TypeConverter
import com.alief.mvvvmnewsapp.model.Source

class Converter
{
    @TypeConverter
    fun fromSource(source: Source): String = source.name

    @TypeConverter
    fun toSource(string: String): Source = Source(string, string)
}
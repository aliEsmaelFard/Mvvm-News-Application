package com.alief.mvvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alief.mvvvmnewsapp.model.Article

@Dao
interface ArticleDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM article")
    fun getAllArticle(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)

}
package com.alief.mvvvmnewsapp.repository

import com.alief.mvvvmnewsapp.api.RetrofitInstance
import com.alief.mvvvmnewsapp.db.ArticleDataBase
import com.alief.mvvvmnewsapp.model.Article

class NewsRepository(
    val db: ArticleDataBase
) {

    suspend fun getBreakingNews(country: String, page: Int) =
        RetrofitInstance.api.getBreakingNews(country, page)

    suspend fun getSearchNews(search: String, page: Int) =
        RetrofitInstance.api.searchForNews(search, page)

    suspend fun upsert( article: Article) = db.getDao().upsert(article)

    fun getAllArticle() = db.getDao().getAllArticle()

    suspend fun delete(article: Article) = db.getDao().delete(article)
}
package com.alief.mvvvmnewsapp.model

import com.alief.mvvvmnewsapp.model.Article

data class NewsRespond(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)
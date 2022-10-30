package com.alief.mvvvmnewsapp.api

import com.alief.mvvvmnewsapp.model.NewsRespond
import com.alief.mvvvmnewsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface
{
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsRespond>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") search: String ,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsRespond>

}
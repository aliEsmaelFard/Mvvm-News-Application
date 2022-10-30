package com.alief.mvvvmnewsapp.util

sealed class Resource<T>(
    val date: T? = null,
    val message: String? = null
) {
    class Success<T>(date: T?): Resource<T>(date)
    class Error<T>(message: String, date: T? = null): Resource<T>(date, message)
    class Loading<T>: Resource<T>()
}
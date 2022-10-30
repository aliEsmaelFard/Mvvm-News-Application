package com.alief.mvvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alief.mvvvmnewsapp.R
import com.alief.mvvvmnewsapp.db.ArticleDataBase
import com.alief.mvvvmnewsapp.repository.NewsRepository
import com.alief.mvvvmnewsapp.ui.fragment.BreakingNewsFragment
import com.alief.mvvvmnewsapp.ui.fragment.SavedNewsFragment
import com.alief.mvvvmnewsapp.ui.fragment.SearchNewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity()
{
    lateinit var viewModel: NewsViewModel
    private lateinit var selectedFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_activity)
        openBreakingNewsFragment()

        val newsRepository = NewsRepository(ArticleDataBase(this))
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(newsRepository, application)
        viewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(NewsViewModel::class.java)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId)
            {
                R.id.breakingNewsFragment -> selectedFragment = BreakingNewsFragment()
                R.id.savedNewsFragment -> selectedFragment = SavedNewsFragment()
                R.id.searchNewsFragment -> selectedFragment = SearchNewsFragment()
            }
            val transaction2 = supportFragmentManager.beginTransaction()
            transaction2.replace(R.id.flFragment, selectedFragment)
            transaction2.commit()
            true
        }
    }

    fun openBreakingNewsFragment()
    {
        selectedFragment = BreakingNewsFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, selectedFragment)
        transaction.commit()
    }
}
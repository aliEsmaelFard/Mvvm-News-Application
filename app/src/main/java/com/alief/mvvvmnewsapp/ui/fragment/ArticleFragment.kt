package com.alief.mvvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.alief.mvvvmnewsapp.R
import com.alief.mvvvmnewsapp.model.Article
import com.alief.mvvvmnewsapp.ui.NewsActivity
import com.alief.mvvvmnewsapp.ui.NewsViewModel
import com.alief.mvvvmnewsapp.util.Constants.Companion.BREAKING_FRG_KEY
import com.alief.mvvvmnewsapp.util.Constants.Companion.BUNDLE_KEY
import com.alief.mvvvmnewsapp.util.Constants.Companion.FRG_KEY
import com.alief.mvvvmnewsapp.util.Constants.Companion.SAVED_FRG_KEY
import com.alief.mvvvmnewsapp.util.Constants.Companion.SEARCH_FRG_KEY
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ArticleFragment: Fragment(R.layout.fragment_article)
{
    lateinit var viewModel: NewsViewModel
    lateinit var article: Article
    lateinit var whichFragmentIs: String
    private lateinit var selectedFragment: Fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel


        val bundle = this.arguments
        bundle?.apply {
            val gson = Gson()
            article = gson.fromJson(this.getString(BUNDLE_KEY), Article::class.java)
            whichFragmentIs = this.getString(FRG_KEY).toString()
        }

        val webView: WebView = view.findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(article.url!!)

        //FAB set up
        val fab:FloatingActionButton = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            viewModel.upsertArticle(article)
            Snackbar.make(view, "Saved Successfully", Snackbar.LENGTH_SHORT).show()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here

                    when(whichFragmentIs)
                    {
                        BREAKING_FRG_KEY -> selectedFragment = BreakingNewsFragment()
                        SAVED_FRG_KEY -> selectedFragment = SavedNewsFragment()
                        SEARCH_FRG_KEY -> selectedFragment = SearchNewsFragment()
                    }
                    val transaction2 = parentFragmentManager.beginTransaction()
                    transaction2.replace(R.id.flFragment, selectedFragment)
                    transaction2.commit()

                    // if you want onBackPressed() to be called as normal afterwards

                }
            }
            )
    }

}
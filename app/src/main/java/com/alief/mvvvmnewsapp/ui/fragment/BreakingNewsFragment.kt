package com.alief.mvvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alief.mvvvmnewsapp.R
import com.alief.mvvvmnewsapp.adaptor.NewsAdaptor
import com.alief.mvvvmnewsapp.adaptor.OnItemClickListener
import com.alief.mvvvmnewsapp.model.Article
import com.alief.mvvvmnewsapp.ui.NewsActivity
import com.alief.mvvvmnewsapp.ui.NewsViewModel
import com.alief.mvvvmnewsapp.util.Constants
import com.alief.mvvvmnewsapp.util.Constants.Companion.BUNDLE_KEY
import com.alief.mvvvmnewsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.alief.mvvvmnewsapp.util.Resource
import com.google.gson.Gson

class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news), OnItemClickListener
{
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdaptor
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        recyclerView = view.findViewById(R.id.rvBreakingNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)

        setOpRecyclerView()
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { resource ->

            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.date?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        val totalPage = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPage

                        if (isLastPage) {
                            recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    resource.message?.let { message ->
                        Toast.makeText(activity, "$message", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

    fun setOpRecyclerView()
    {
        newsAdapter = NewsAdaptor(this)
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollView)
        }
    }

    fun showProgressBar()
    {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    fun hideProgressBar()
    {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollView = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)


            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstViableItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndIsNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstViableItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstViableItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndIsNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }
    }

    override fun onClicked(article: Article)
    {
        val gson = Gson()
        val jsonString = gson.toJson(article)

        val bundle = Bundle()
        bundle.putString(BUNDLE_KEY, jsonString)
        bundle.putString(Constants.FRG_KEY, Constants.BREAKING_FRG_KEY)

        val articleFragment = ArticleFragment()
        articleFragment.arguments = bundle

        val fragmentTransient = parentFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.flFragment, articleFragment)
        fragmentTransient.commit()
    }
}
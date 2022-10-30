package com.alief.mvvvmnewsapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alief.mvvvmnewsapp.R
import com.alief.mvvvmnewsapp.adaptor.NewsAdaptor
import com.alief.mvvvmnewsapp.adaptor.OnItemClickListener
import com.alief.mvvvmnewsapp.model.Article
import com.alief.mvvvmnewsapp.ui.NewsActivity
import com.alief.mvvvmnewsapp.ui.NewsViewModel
import com.alief.mvvvmnewsapp.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class SavedNewsFragment: Fragment(R.layout.fragment_saved_news), OnItemClickListener
{
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdaptor
    lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        recyclerView = view.findViewById(R.id.rvSavedNews)
        setOpRecyclerView()

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val article = newsAdapter.differ.currentList[position]

                viewModel.deleteArticle(article)
                Snackbar.make(view, "Delete Was Successful", Snackbar.LENGTH_LONG)
                    .setAction("undo") {
                        viewModel.upsertArticle(article)
                    }.show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView)
    }

    private fun setOpRecyclerView()
    {
        newsAdapter = NewsAdaptor(this)
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onClicked(article: Article)
    {
        val gson = Gson()
        val jsonString = gson.toJson(article)

        val bundle = Bundle()
        bundle.putString(Constants.BUNDLE_KEY, jsonString)
        bundle.putString(Constants.FRG_KEY, Constants.SAVED_FRG_KEY)

        val articleFragment = ArticleFragment()
        articleFragment.arguments = bundle

        val fragmentTransient = parentFragmentManager.beginTransaction()
        fragmentTransient.replace(R.id.flFragment, articleFragment)
        fragmentTransient.commit()
    }
}
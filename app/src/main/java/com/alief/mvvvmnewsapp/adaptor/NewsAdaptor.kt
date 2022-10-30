package com.alief.mvvvmnewsapp.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alief.mvvvmnewsapp.R
import com.alief.mvvvmnewsapp.model.Article
import com.bumptech.glide.Glide

class NewsAdaptor(val listener: OnItemClickListener): RecyclerView.Adapter<NewsAdaptor.ViewHolder>() {


    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_article_preview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.articleImage)
            holder.source.text = article.source?.name
            holder.title.text = article.title
            holder.published.text = article.publishedAt
            holder.description.text = article.description
        }

    }

    override fun getItemCount() = differ.currentList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val articleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val source: TextView = itemView.findViewById(R.id.tvSource)
        val published: TextView = itemView.findViewById(R.id.tvPublishedAt)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClicked(differ.currentList[bindingAdapterPosition])
        }
    }
}

interface OnItemClickListener{
    fun onClicked(article: Article)
}


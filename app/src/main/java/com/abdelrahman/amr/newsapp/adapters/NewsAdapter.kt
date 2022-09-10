package com.abdelrahman.amr.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.amr.newsapp.databinding.ItemArticlePreviewBinding
import com.abdelrahman.amr.newsapp.models.Article
import com.bumptech.glide.Glide


class NewsAdapter():RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(var previewBinding: ItemArticlePreviewBinding) :RecyclerView.ViewHolder(previewBinding.root)


    private val differCallback  = object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this@NewsAdapter,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        var article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.previewBinding.ivArticleImage)
            holder.previewBinding.tvSource.text = article.source!!.name
            holder.previewBinding.tvTitle.text = article.title
            holder.previewBinding.tvDescription.text = article.description
            holder.previewBinding.tvPublishedAt.text=article.publishedAt
            holder.previewBinding.root.setOnClickListener {

                onItemClickListener?.let {
                    it(article)
                }


            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener:((Article)->Unit)?=null
    fun setOnClickListener(listener:(Article)->Unit){
        onItemClickListener = listener
    }

}
package com.abdelrahman.amr.newsapp.repository

import com.abdelrahman.amr.newsapp.api.RetrofitInstance
import com.abdelrahman.amr.newsapp.db.ArticleDataBase
import com.abdelrahman.amr.newsapp.models.Article

class NewsRepository(
    val db :ArticleDataBase
) {

    suspend fun gerBreakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber )

    suspend fun searchNews(searchQuery:String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()


    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)



}
package com.abdelrahman.amr.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abdelrahman.amr.newsapp.models.Article
import retrofit2.http.DELETE

@Dao
interface ArticleDao {

    /*
    onConflict =OnConflictStrategy.REPLACE
    دي بنحطها علشان لو عندنا حاجة متكررة ميحصلش تعارض
    لو موجودة هيتعدل عليها لو مش موجودة هتتضاف عادي
    */

    @Insert(onConflict =OnConflictStrategy.REPLACE )
    suspend fun upsert(article: Article):Long

    @Query("SELECT * FROM articles")
     fun getAllArticles():LiveData<List<Article>>

     @Delete
     suspend fun deleteArticle(article: Article)

}
package com.abdelrahman.amr.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.abdelrahman.amr.newsapp.R
import com.abdelrahman.amr.newsapp.repository.NewsRepository
import com.abdelrahman.amr.newsapp.databinding.ActivityNewsBinding
import com.abdelrahman.amr.newsapp.db.ArticleDataBase
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel:NewsViewModel

    private lateinit var binding:ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NewsRepository(ArticleDataBase(this.applicationContext))
             val newsViewModelProviderFactory = NewsViewModelProviderFactory(application,repository)
         viewModel = ViewModelProvider(this,newsViewModelProviderFactory)[NewsViewModel::class.java]
                       val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .setupWithNavController(navController)
    }
}
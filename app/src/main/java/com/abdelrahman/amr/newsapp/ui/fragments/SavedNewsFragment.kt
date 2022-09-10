package com.abdelrahman.amr.newsapp.ui.fragments

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.amr.newsapp.R
import com.abdelrahman.amr.newsapp.adapters.NewsAdapter
import com.abdelrahman.amr.newsapp.databinding.FragmentSavedNewsBinding
import com.abdelrahman.amr.newsapp.ui.NewsActivity
import com.abdelrahman.amr.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var newsAdapter:NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSavedNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel


        viewModel.getSavedNews().observe(viewLifecycleOwner){
            newsAdapter.differ.submitList(it)
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()

        binding.rvSavedNews.apply {
            adapter  = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        newsAdapter.setOnClickListener {
            Log.d("article",it.title!!)
            val bundle = Bundle()
            bundle.putSerializable("article",it)
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment
                ,bundle
            )
        }


        val itemTouchHelper = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(binding.root,"Article was deleted successfully",Snackbar.LENGTH_SHORT).apply {
                    setAction("undo"){
                        viewModel.saveArticle(article)
                    }
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }


    }
}
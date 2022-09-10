package com.abdelrahman.amr.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.amr.newsapp.R
import com.abdelrahman.amr.newsapp.adapters.NewsAdapter
import com.abdelrahman.amr.newsapp.databinding.FragmentBreakingNewsBinding
import com.abdelrahman.amr.newsapp.ui.NewsActivity
import com.abdelrahman.amr.newsapp.ui.NewsViewModel
import com.abdelrahman.amr.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.abdelrahman.amr.newsapp.util.Resource

class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {
    private lateinit var viewModel:NewsViewModel
    private lateinit var newsAdapter:NewsAdapter
    private lateinit var binding: FragmentBreakingNewsBinding
    private val TAG = "BreakingNewsAdapter"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentBreakingNewsBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        viewModel.breakingNews.observe(viewLifecycleOwner){
             when(it){
                 is Resource.Success ->{
                     hideProgressBar()
                     it.data?.let {

                      newsAdapter.differ.submitList(it.articles.toList())
                         val totalPages = it.totalResults!! / QUERY_PAGE_SIZE +2
                         isLastPage = viewModel.breakingNewsPage == totalPages

                         if (isLastPage){
                             binding.rvBreakingNews.setPadding(0,0,0,0)
                         }


                     }
                 }

                 is Resource.Error ->{
                     hideProgressBar()
                     it.message?.let {
                         Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()

                     }

                 }

                 is Resource.Loading -> {
                     showProgressBar()

                 }

             }
        }

    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading =true

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading =false

    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    private val scrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true

            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition+visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage
                    && isAtLastItem
                    && isNotAtBeginning
                    && isTotalMoreThanVisible
                    && isScrolling
            if (shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling =false
            }




        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()

        binding.rvBreakingNews.apply {
            adapter  = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
        newsAdapter.setOnClickListener {
            Log.d("article",it.title!!)
            val bundle = Bundle()
            bundle.putSerializable("article",it)
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment
            ,bundle
            )
        }

    }
}
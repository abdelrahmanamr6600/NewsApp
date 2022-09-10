package com.abdelrahman.amr.newsapp.ui.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.abdelrahman.amr.newsapp.databinding.FragmentArticleBinding
import com.abdelrahman.amr.newsapp.ui.NewsActivity
import com.abdelrahman.amr.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment:Fragment() {
    private lateinit var binding : FragmentArticleBinding
    private val args:ArticleFragmentArgs by navArgs()
    private lateinit var viewModel: NewsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url!!)

        }

        binding.fab.setOnClickListener {
            Snackbar.make(requireContext(),binding.root,"Article was Saved Successfully",Snackbar.LENGTH_SHORT).show()
            viewModel.saveArticle(article)
        }

    }
}



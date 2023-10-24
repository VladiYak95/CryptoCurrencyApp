package com.vladiyak.cryptocurrencyapp.fragments.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vladiyak.cryptocurrencyapp.R
import com.vladiyak.cryptocurrencyapp.fragments.news.adapter.INewsRVAdapter
import com.vladiyak.cryptocurrencyapp.fragments.news.adapter.NewsRVAdapter
import com.vladiyak.cryptocurrencyapp.databinding.FragmentNewsBinding
import com.vladiyak.cryptocurrencyapp.utils.ApiResponse
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsFragment : Fragment(), INewsRVAdapter {

    private var binding: FragmentNewsBinding? = null
    private val newsAdapter = NewsRVAdapter(this)
    private lateinit var viewModel: NewsViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing navController
        navController = Navigation.findNavController(view)

        //initializing recyclerView
        setUpRecyclerView()

        //initializing viewModel
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        viewModel.getNews()

        viewModel.news.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is ApiResponse.Success -> {
                    binding?.shimmerLayoutNews?.stopShimmer()
                    binding?.shimmerLayoutNews?.visibility = View.GONE
                    binding?.rvNews?.visibility = View.VISIBLE
                    newsAdapter.submitList(response.data)
                }
                is ApiResponse.Loading -> {
                    binding?.shimmerLayoutNews?.visibility = View.VISIBLE
                    binding?.shimmerLayoutNews?.startShimmer()
                }
                is ApiResponse.Error -> {
                    binding?.shimmerLayoutNews?.stopShimmer()
                    binding?.shimmerLayoutNews?.visibility = View.GONE
                    Snackbar.make(view, "Could yes retrieve news, restart app!", Snackbar.LENGTH_SHORT).show()
                }
            }
            response.responseMessage
        })
    }

    private fun setUpRecyclerView() {
        binding?.rvNews?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onNewsArticleClicked(url: String) {
        val bundle = bundleOf("articleUrl" to url)
        navController.navigate(R.id.action_newsFragment_to_articleOpenFragment, bundle)
    }
}

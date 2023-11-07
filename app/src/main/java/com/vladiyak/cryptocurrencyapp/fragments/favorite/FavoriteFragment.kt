package com.vladiyak.cryptocurrencyapp.fragments.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vladiyak.cryptocurrencyapp.R
import com.vladiyak.cryptocurrencyapp.databinding.FragmentDetailBinding
import com.vladiyak.cryptocurrencyapp.databinding.FragmentFavoriteBinding
import com.vladiyak.cryptocurrencyapp.databinding.FragmentHomeBinding
import com.vladiyak.cryptocurrencyapp.fragments.favorite.adapters.FavouriteHomeAdapter
import com.vladiyak.cryptocurrencyapp.model.FavouriteEntity
import com.vladiyak.cryptocurrencyapp.utils.OnClickListenerFavouriteItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.RuntimeException


@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() = _binding ?: throw RuntimeException("FragmentFavoriteBinding == null")

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var favoritesAdapter: FavouriteHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        //Setting the toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarFav)
        (activity as AppCompatActivity).supportActionBar!!.title = "Favourite Coins";

        // Setting the Listeners
        binding.moveToHomeButton.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)
        }

    }

    private fun setRecyclerView() {
        viewModel.getAllFavouriteCoin()
        binding.favouriteRecyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.allFavouriteCoin.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.nothingUi.visibility = View.VISIBLE
            } else {
                binding.nothingUi.visibility = View.INVISIBLE
            }
            favoritesAdapter = FavouriteHomeAdapter(onClickListener = object :
                OnClickListenerFavouriteItem {
                override fun onItemClick(favCoin: FavouriteEntity) {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment2(
                        favCoin.coinId
                    )
                    findNavController().navigate(action)
                }
            })
            binding.favouriteRecyclerView.adapter = favoritesAdapter
            favoritesAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
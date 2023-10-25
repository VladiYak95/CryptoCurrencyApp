package com.vladiyak.cryptocurrencyapp.fragments.favorite.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vladiyak.cryptocurrencyapp.api.newapi.dto.coins.CoinItem
import com.vladiyak.cryptocurrencyapp.fragments.home.adapters.CoinsViewHolder
import com.vladiyak.cryptocurrencyapp.model.FavouriteEntity
import com.vladiyak.cryptocurrencyapp.utils.OnClickListener
import com.vladiyak.cryptocurrencyapp.utils.OnClickListenerFavouriteItem

class FavouriteHomeAdapter(
    private val onClickListener: OnClickListenerFavouriteItem
) : ListAdapter<FavouriteEntity, FavouriteViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, onClickListener)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<FavouriteEntity>() {
        override fun areItemsTheSame(oldItem: FavouriteEntity, newItem: FavouriteEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FavouriteEntity, newItem: FavouriteEntity): Boolean {
            return oldItem.coinId == newItem.coinId
        }

    }
}
package com.vladiyak.cryptocurrencyapp.presentation.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.vladiyak.cryptocurrencyapp.R
import com.vladiyak.cryptocurrencyapp.databinding.CoinListItemBinding
import com.vladiyak.cryptocurrencyapp.domain.models.CoinItem
import com.vladiyak.cryptocurrencyapp.utils.OnClickListener
import com.vladiyak.cryptocurrencyapp.utils.addPrefix
import com.vladiyak.cryptocurrencyapp.utils.addSuffix
import java.util.Locale

class CoinsViewHolder(
    val binding: CoinListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CoinItem, onClickListener: OnClickListener) {
        binding.apply {
            itemLayoutId.setOnClickListener {
                onClickListener.onItemClick(item)
            }

            binding.apply {
                coinImage.load(item.image) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                val chartData = item.sparklineIn7d?.price?.map { it.toInt() }?.takeLast(20)
                val array = chartData?.let { ArrayList(it) }
                if (array != null) {
                    chart.setData(array)
                }
                if (item.priceChangePercentage7dInCurrency > 0) {
                    chart.isGradientLine = true
                    chart.sparkLineColor = Color.parseColor("#00DA5C")
                } else {
                    chart.isGradientLine = true
                    chart.sparkLineColor = Color.parseColor("#FF1A1A")
                }


                tvCoinName.text = item.name
                tvCoinSymbol.text = item.symbol.uppercase(Locale.ROOT)
                tvCoinRank.text = item.marketCapRank.toString()
                tvCoinPrice.text =
                    item.currentPrice.toString().addPrefix("$")
            }

            if (item.priceChangePercentage7dInCurrency > 0) {
                tvPercentage.text = String.format("%.2f", item.priceChangePercentage7dInCurrency).addPrefix("%").addSuffix("+")
                binding.tvPercentage.setBackgroundResource(
                    R.drawable.background_corners_percent_increase
                )
            } else {
                tvPercentage.text = String.format("%.2f", item.priceChangePercentage7dInCurrency).addPrefix("%")
                binding.tvPercentage.setBackgroundResource(
                    R.drawable.background_corners_percent_decrease
                )
            }


        }
    }


    companion object {
        fun from(parent: ViewGroup): CoinsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = CoinListItemBinding.inflate(layoutInflater, parent, false)
            return CoinsViewHolder(binding)
        }
    }
}
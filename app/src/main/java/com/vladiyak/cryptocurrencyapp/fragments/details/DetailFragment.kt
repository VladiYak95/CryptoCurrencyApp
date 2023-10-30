package com.vladiyak.cryptocurrencyapp.fragments.details

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.vladiyak.cryptocurrencyapp.R
import com.vladiyak.cryptocurrencyapp.activities.MainActivity
import com.vladiyak.cryptocurrencyapp.api.newapi.dto.coins.CoinDetail
import com.vladiyak.cryptocurrencyapp.api.newapi.dto.coins.CoinMarketChart
import com.vladiyak.cryptocurrencyapp.databinding.FragmentDetailBinding
import com.vladiyak.cryptocurrencyapp.fragments.favorite.FavoriteViewModel
import com.vladiyak.cryptocurrencyapp.model.FavouriteEntity
import com.vladiyak.cryptocurrencyapp.utils.CustomMarkerView
import com.vladiyak.cryptocurrencyapp.utils.XAxisValueFormatter
import com.vladiyak.cryptocurrencyapp.utils.YAxisValueFormatter
import com.vladiyak.cryptocurrencyapp.utils.addPrefix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private val viewModelFav: FavoriteViewModel by viewModels()
    private var isIncreasing: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.hide()
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        viewModel.getAllData(args.coinId)
        observeData()
        viewModel.getCoinDetail(args.coinId)
        checkingAlreadyFavorite()
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectTimeSpan()

        binding.favtoggleButton.setOnClickListener {

            // Favourite Listener
            binding.coin?.let {
                favouriteListener(it)
            }
        }
    }


    private fun checkingAlreadyFavorite() {
        lifecycleScope.launch {
            viewModelFav.allFavouriteCoin.observe(viewLifecycleOwner) { favouriteEntities ->
                favouriteEntities.forEach {
                    if (it.coinId == args.coinId) {
                        binding.favtoggleButton.setImageResource(R.drawable.ic_star)
                        binding.favtoggleButton.tag = "ON"
                    }
                }
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    if(state.timeRange.value == 7) {
                        setDefaultPercentageChange()
                    }

                    val chartData = getData(state.priceList)
                    displayLineChart(chartData)
                    binding.coin = state.coinDetail
                }
            }
        }
    }

    private fun selectTimeSpan(id: String = args.coinId) {
        with(binding) {
            chip1.setOnClickListener {
                setDefaultPercentageChange()
                viewModel.setCoinChartTimeSpan(1, id)
            }
            chip7.setOnClickListener {
                binding.txtPriceChange.text = viewModel.state.value.coinDetail?.marketData?.priceChangePercentage7d.toString().substring(0, 4).addPrefix("%")
                if ((viewModel.state.value.coinDetail?.marketData?.priceChangePercentage7d ?: 0.0) > 0) {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.green))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_up_24)
                    isIncreasing = true
                } else {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.red))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_down_24)
                    isIncreasing = false
                }
                viewModel.setCoinChartTimeSpan(7, id)
            }
            chip14.setOnClickListener {
                binding.txtPriceChange.text = viewModel.state.value.coinDetail?.marketData?.priceChangePercentage14d.toString().substring(0, 4).addPrefix("%")
                if ((viewModel.state.value.coinDetail?.marketData?.priceChangePercentage14d ?: 0.0) > 0) {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.green))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_up_24)
                    isIncreasing = true
                } else {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.red))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_down_24)
                    isIncreasing = false
                }
                viewModel.setCoinChartTimeSpan(14, id)
            }
            chip30.setOnClickListener {
                binding.txtPriceChange.text = viewModel.state.value.coinDetail?.marketData?.priceChangePercentage30d.toString().substring(0, 4).addPrefix("%")
                if ((viewModel.state.value.coinDetail?.marketData?.priceChangePercentage30d ?: 0.0) > 0) {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.green))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_up_24)
                    isIncreasing = true
                } else {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.red))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_down_24)
                    isIncreasing = false
                }
                viewModel.setCoinChartTimeSpan(30, id)
            }
            chip60.setOnClickListener {
                binding.txtPriceChange.text = viewModel.state.value.coinDetail?.marketData?.priceChangePercentage60d.toString().substring(0, 4).addPrefix("%")
                if ((viewModel.state.value.coinDetail?.marketData?.priceChangePercentage60d ?: 0.0) > 0) {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.green))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_up_24)
                    isIncreasing = true
                } else {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.red))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_down_24)
                    isIncreasing = false
                }
                viewModel.setCoinChartTimeSpan(60, id)
            }
            chip365.setOnClickListener {
                binding.txtPriceChange.text = viewModel.state.value.coinDetail?.marketData?.priceChangePercentage365d.toString().substring(0, 4).addPrefix("%")
                if ((viewModel.state.value.coinDetail?.marketData?.priceChangePercentage365d ?: 0.0) > 0) {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.green))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_up_24)
                    isIncreasing = true
                } else {
                    binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.red))
                    binding.imageView.setImageResource(R.drawable.ic_arrow_down_24)
                    isIncreasing = false
                }
                viewModel.setCoinChartTimeSpan(365, id)

            }
        }
    }

    private fun setDefaultPercentageChange() {
        binding.txtPriceChange.text =
            viewModel.state.value.coinDetail?.marketData?.priceChangePercentage24h.toString().substring(0, 4).addPrefix("%")
        if ((viewModel.state.value.coinDetail?.marketData?.priceChangePercentage24h ?: 0.0) > 0) {
            binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.green))
            binding.imageView.setImageResource(R.drawable.ic_arrow_up_24)
            isIncreasing = true
        } else {
            binding.materialCardPriceChange.setCardBackgroundColor(resources.getColor(R.color.red))
            binding.imageView.setImageResource(R.drawable.ic_arrow_down_24)
            isIncreasing = false
        }
    }

    private fun displayLineChart(chartData: Pair<List<String>, List<Entry>>) {
        binding.lineChart.apply {
            val lineDataSet = LineDataSet(chartData.second, "Value")
            lineDataSet.setDrawFilled(true)
            val formatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return if (value.toInt() < chartData.first.size) chartData.first[value.toInt()] else ""
                }
            }
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            xAxis.valueFormatter = formatter
            xAxis.labelRotationAngle = 90f
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(false)
            legend.isEnabled = false
//            xAxis.valueFormatter = XAxisValueFormatter()

            lineDataSet.setDrawCircles(false)
            lineDataSet.disableDashedLine()
            lineDataSet.setDrawValues(false)
            lineDataSet.highLightColor = Color.MAGENTA

            axisRight.setDrawAxisLine(false)
            axisRight.setDrawGridLines(true)
            axisRight.setDrawLabels(false)
            axisRight.gridColor = Color.WHITE

            axisLeft.setDrawAxisLine(false)
            axisLeft.setDrawGridLines(true)
            axisLeft.setLabelCount(2, true)
            axisLeft.textColor = Color.WHITE
            axisLeft.gridColor = Color.WHITE
            axisLeft.gridLineWidth = 0.2f
//            axisLeft.valueFormatter = YAxisValueFormatter()

            description.text = "Usd"
            val data = LineData(lineDataSet)
            this.data = data
            lineDataSet.fillDrawable = lineFillDrawable()
            setTouchEnabled(true)
            setPinchZoom(true)
            invalidate()
            animateX(1500, Easing.EaseInExpo)
            val customMarker = CustomMarkerView(context, R.layout.custom_marker_view)
            marker = customMarker

        }
    }

    private fun lineFillDrawable(): Drawable? {
        return if (isIncreasing) {
            ContextCompat.getDrawable(requireContext(), R.drawable.chart_bg_increase)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.chart_bg_decrease)
        }
    }

    private fun favouriteListener(data: CoinDetail) {
        // Creating Favourite Entity
        val element = FavouriteEntity(
            data.id,
            data.name,
            data.marketData?.currentPrice?.usd.toString(),
            data.image?.large,
            data.marketData?.priceChangePercentage24h.toString(),
        )

        if (binding.favtoggleButton.tag != "ON") {
            // Changing  the Image To Filled
            binding.favtoggleButton.setImageResource(R.drawable.ic_star)
            //  Adding To DB
            viewModelFav.addToFavourites(element)
            // Changing the TAG to ON
            binding.favtoggleButton.tag = "ON"
        } else {
            // Deleting From Database
            viewModelFav.removeCoinFromFavourite(element)
            //Changing the Image To Border
            binding.favtoggleButton.setImageResource(R.drawable.ic_star_outline)
            // Setting TAG to OFF
            binding.favtoggleButton.tag = "OFF"
        }
    }

    private fun getData(list: List<List<Double>>): Pair<List<String>, List<Entry>> {
        val xAxisValues = arrayListOf<String>()
        val entries = arrayListOf<Entry>()

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

        list.forEachIndexed { index, entry ->
            val date = Date(entry[0].toLong())
            val label = simpleDateFormat.format(date)
            xAxisValues.add(label)
            entries.add(Entry(index.toFloat(), entry[1].toFloat()))
        }

        return Pair(xAxisValues, entries)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
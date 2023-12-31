package com.vladiyak.cryptocurrencyapp.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladiyak.cryptocurrencyapp.domain.models.FavoriteCoin
import com.vladiyak.cryptocurrencyapp.domain.usecases.AddFavoriteUseCase
import com.vladiyak.cryptocurrencyapp.domain.usecases.DeleteFavoriteUseCase
import com.vladiyak.cryptocurrencyapp.domain.usecases.GetAllFavoriteUseCase
import com.vladiyak.cryptocurrencyapp.domain.usecases.impl.AddFavoriteUseCaseImpl
import com.vladiyak.cryptocurrencyapp.domain.usecases.impl.DeleteFavoriteUseCaseImpl
import com.vladiyak.cryptocurrencyapp.domain.usecases.impl.GetAllFavoriteUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase
): ViewModel() {

    private var _allFavouriteCoin: MutableLiveData<List<FavoriteCoin>> =
        MutableLiveData<List<FavoriteCoin>>()
    val allFavouriteCoin: LiveData<List<FavoriteCoin>> = _allFavouriteCoin


    fun addToFavourites(favoriteCoin: FavoriteCoin) {
        viewModelScope.launch(Dispatchers.IO) {
            addFavoriteUseCase(favoriteCoin)
        }
    }

    fun getAllFavouriteCoin() {
        viewModelScope.launch(Dispatchers.IO) {
            _allFavouriteCoin.postValue(getAllFavoriteUseCase())
        }
    }

    fun removeCoinFromFavourite(favoriteCoin: FavoriteCoin) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavoriteUseCase(favoriteCoin)
        }
    }
    init {
        getAllFavouriteCoin()
    }
}
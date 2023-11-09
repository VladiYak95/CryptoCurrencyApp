package com.vladiyak.cryptocurrencyapp.domain.usecases.impl

import com.vladiyak.cryptocurrencyapp.data.local.mappers.FavoriteEntityMapper
import com.vladiyak.cryptocurrencyapp.domain.models.FavoriteCoin
import com.vladiyak.cryptocurrencyapp.domain.repository.CoinRepository
import com.vladiyak.cryptocurrencyapp.domain.usecases.AddFavoriteUseCase
import javax.inject.Inject

class AddFavoriteUseCaseImpl @Inject constructor(
    private val repository: CoinRepository,
    private val favoriteEntityMapper: FavoriteEntityMapper
): AddFavoriteUseCase {

    override suspend operator fun invoke(favoriteCoin: FavoriteCoin) {
        repository.addFavorite(favoriteEntityMapper.mapFromDomainModel(favoriteCoin))
    }
}
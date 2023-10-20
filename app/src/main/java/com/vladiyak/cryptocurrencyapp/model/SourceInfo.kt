package com.vladiyak.cryptocurrencyapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceInfo(
    @Json(name = "img")
    val img: String?,
    @Json(name = "lang")
    val lang: String?,
    @Json(name = "name")
    val name: String?
)

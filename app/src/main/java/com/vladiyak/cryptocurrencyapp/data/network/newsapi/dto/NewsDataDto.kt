package com.vladiyak.cryptocurrencyapp.data.network.newsapi.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vladiyak.cryptocurrencyapp.domain.models.NewsData


@JsonClass(generateAdapter = true)
data class NewsDataDto(
    @Json(name = "body")
    val body: String?,
    @Json(name = "categories")
    val categories: String?,
    @Json(name = "downvotes")
    val downvotes: String?,
    @Json(name = "guid")
    val guid: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "imageurl")
    val imageurl: String?,
    @Json(name = "lang")
    val lang: String?,
    @Json(name = "published_on")
    val publishedOn: Int?,
    @Json(name = "source")
    val source: String?,
    @Json(name = "source_info")
    val sourceInfo: SourceInfoDto?,
    @Json(name = "tags")
    val tags: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "upvotes")
    val upvotes: String?,
    @Json(name = "url")
    val url: String?
)

fun NewsDataDto.toNewsData(): NewsData {
    return NewsData(
        body = body,
        categories = categories,
        downvotes = downvotes,
        guid = guid,
        id = id,
        imageurl = imageurl,
        lang = lang,
        publishedOn = publishedOn,
        source = source,
        sourceInfo = sourceInfo?.toSourceInfo(),
        tags = tags,
        title = title,
        upvotes = upvotes,
        url = url
    )
}
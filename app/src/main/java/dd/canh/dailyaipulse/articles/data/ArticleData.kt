package dd.canh.dailyaipulse.articles.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleData(
    val source: SourceData,
    val title: String,
    val description: String?,
    @param:Json(name = "urlToImage") val imageUrl: String?,
    @param:Json(name = "publishedAt") val date: String,
)

@JsonClass(generateAdapter = true)
data class SourceData(
    val id: String?,
    val name: String,
)

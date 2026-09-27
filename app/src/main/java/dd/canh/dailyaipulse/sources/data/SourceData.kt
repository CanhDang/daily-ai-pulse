package dd.canh.dailyaipulse.sources.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceData(
    val id: String,
    val name: String,
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?,
)

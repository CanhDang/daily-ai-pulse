package dd.canh.dailyaipulse.articles.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
    ): ArticlesResponse
}

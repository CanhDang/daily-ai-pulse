package dd.canh.dailyaipulse.articles.presentation

import dd.canh.dailyaipulse.articles.data.ArticleApi
import dd.canh.dailyaipulse.articles.data.ArticleData
import dd.canh.dailyaipulse.articles.data.ArticlesResponse

class FakeArticleApi(
    private val articles: List<ArticleData> = emptyList(),
    private val error: Exception? = null,
) : ArticleApi {

    override suspend fun getTopHeadlines(country: String): ArticlesResponse {
        error?.let { throw it }
        return ArticlesResponse(status = "ok", totalResults = articles.size, articles = articles)
    }
}

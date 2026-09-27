package dd.canh.dailyaipulse.articles.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val articleApi: ArticleApi,
) {

    suspend fun getArticles(): List<ArticleData> =
        articleApi.getTopHeadlines().articles
}

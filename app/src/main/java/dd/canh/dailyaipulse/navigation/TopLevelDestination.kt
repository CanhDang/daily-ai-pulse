package dd.canh.dailyaipulse.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dd.canh.dailyaipulse.R

enum class TopLevelDestination(
    val route: Any,
    @get:StringRes val labelRes: Int,
    @get:DrawableRes val iconRes: Int,
) {
    ARTICLES(ArticlesRoute, R.string.articles, R.drawable.ic_articles),
    SOURCES(SourcesRoute, R.string.sources, R.drawable.ic_sources),
}

package dd.canh.dailyaipulse.articles.presentation

import dd.canh.dailyaipulse.articles.data.ArticleData
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

private const val DISPLAY_DATE_PATTERN = "MMM d, yyyy"

fun ArticleData.toArticle(): Article = Article(
    title = title,
    description = description,
    imageUrl = imageUrl,
    date = formatDate(date),
    sourceName = source.name,
)

// Falls back to the raw value so a bad date never hides the article.
private fun formatDate(isoDate: String): String = try {
    val formatter = DateTimeFormatter.ofPattern(DISPLAY_DATE_PATTERN, Locale.getDefault())
    Instant.parse(isoDate).atZone(ZoneId.systemDefault()).format(formatter)
} catch (e: DateTimeParseException) {
    isoDate
}

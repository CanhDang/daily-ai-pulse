package dd.canh.dailyaipulse.articles.presentation

import dd.canh.dailyaipulse.articles.data.ArticleData
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

fun ArticleData.toArticle(clock: Clock): Article = Article(
    title = title,
    description = description,
    imageUrl = imageUrl,
    date = toRelativeDate(date, clock),
    sourceName = source.name,
)

private fun toRelativeDate(isoDate: String, clock: Clock): String {
    val publishedDate = try {
        Instant.parse(isoDate).atZone(clock.zone).toLocalDate()
    } catch (e: DateTimeParseException) {
        // Falls back to the raw value so a bad date never hides the article.
        return isoDate
    }
    val daysAgo = ChronoUnit.DAYS.between(publishedDate, LocalDate.now(clock))
    return when {
        // Negative means a future date, e.g. a slightly wrong device clock.
        daysAgo <= 0 -> "Today"
        daysAgo == 1L -> "Yesterday"
        else -> "$daysAgo days ago"
    }
}

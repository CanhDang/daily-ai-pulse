package dd.canh.dailyaipulse.articles.presentation

import dd.canh.dailyaipulse.articles.data.ArticleData
import dd.canh.dailyaipulse.articles.data.SourceData
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class ArticleMapperTest {

    // "Now" is Feb 10, 2026 at 10:00 UTC.
    private val clock = Clock.fixed(Instant.parse("2026-02-10T10:00:00Z"), ZoneOffset.UTC)

    @Test
    fun `maps date from today to Today`() {
        assertEquals("Today", mapDate("2026-02-10T00:30:00Z"))
    }

    @Test
    fun `maps date from yesterday to Yesterday`() {
        assertEquals("Yesterday", mapDate("2026-02-09T23:59:00Z"))
    }

    @Test
    fun `maps older date to days ago`() {
        assertEquals("6 days ago", mapDate("2026-02-04T06:59:45Z"))
    }

    @Test
    fun `maps future date to Today`() {
        assertEquals("Today", mapDate("2026-02-11T08:00:00Z"))
    }

    @Test
    fun `counts calendar days in the device time zone`() {
        // 23:00 UTC on Feb 9 is already Feb 10 in Ho Chi Minh City (UTC+7).
        val vietnamClock = Clock.fixed(Instant.parse("2026-02-10T10:00:00Z"), ZoneOffset.ofHours(7))

        val article = articleData(date = "2026-02-09T23:00:00Z").toArticle(vietnamClock)

        assertEquals("Today", article.date)
    }

    @Test
    fun `keeps raw date when it cannot be parsed`() {
        assertEquals("not-a-date", mapDate("not-a-date"))
    }

    @Test
    fun `uses source name`() {
        val article = articleData(date = "2026-02-10T00:30:00Z").toArticle(clock)

        assertEquals("CNN", article.sourceName)
    }

    private fun mapDate(date: String): String = articleData(date).toArticle(clock).date

    private fun articleData(date: String) = ArticleData(
        source = SourceData(id = "cnn", name = "CNN"),
        title = "Title",
        description = "Description",
        imageUrl = "https://example.com/image.jpg",
        date = date,
    )
}

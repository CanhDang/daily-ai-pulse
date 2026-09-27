package dd.canh.dailyaipulse.articles.presentation

import dd.canh.dailyaipulse.articles.data.ArticleData
import dd.canh.dailyaipulse.articles.data.SourceData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale
import java.util.TimeZone

class ArticleMapperTest {

    private val defaultLocale = Locale.getDefault()
    private val defaultTimeZone = TimeZone.getDefault()

    @Before
    fun setUp() {
        Locale.setDefault(Locale.US)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @After
    fun tearDown() {
        Locale.setDefault(defaultLocale)
        TimeZone.setDefault(defaultTimeZone)
    }

    @Test
    fun `formats ISO date for display`() {
        val article = articleData(date = "2026-02-04T06:59:45Z").toArticle()

        assertEquals("Feb 4, 2026", article.date)
    }

    @Test
    fun `keeps raw date when it cannot be parsed`() {
        val article = articleData(date = "not-a-date").toArticle()

        assertEquals("not-a-date", article.date)
    }

    @Test
    fun `uses source name`() {
        val article = articleData(date = "2026-02-04T06:59:45Z").toArticle()

        assertEquals("CNN", article.sourceName)
    }

    private fun articleData(date: String) = ArticleData(
        source = SourceData(id = "cnn", name = "CNN"),
        title = "Title",
        description = "Description",
        imageUrl = "https://example.com/image.jpg",
        date = date,
    )
}

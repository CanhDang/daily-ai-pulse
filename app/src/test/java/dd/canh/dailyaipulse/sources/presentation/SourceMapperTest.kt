package dd.canh.dailyaipulse.sources.presentation

import dd.canh.dailyaipulse.sources.data.SourceData
import org.junit.Assert.assertEquals
import org.junit.Test

class SourceMapperTest {

    @Test
    fun `joins category, language and country into details`() {
        val source = sourceData(category = "general", language = "en", country = "us").toSource()

        assertEquals("General · EN · US", source.details)
    }

    @Test
    fun `skips missing and blank details`() {
        val source = sourceData(category = null, language = "", country = "us").toSource()

        assertEquals("US", source.details)
    }

    @Test
    fun `keeps id, name and description`() {
        val source = sourceData(category = "general", language = "en", country = "us").toSource()

        assertEquals("abc-news", source.id)
        assertEquals("ABC News", source.name)
        assertEquals("Breaking news", source.description)
    }

    private fun sourceData(category: String?, language: String?, country: String?) = SourceData(
        id = "abc-news",
        name = "ABC News",
        description = "Breaking news",
        url = "https://abcnews.go.com",
        category = category,
        language = language,
        country = country,
    )
}

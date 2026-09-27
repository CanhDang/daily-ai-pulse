package dd.canh.dailyaipulse.sources.presentation

import dd.canh.dailyaipulse.sources.data.SourceData

private const val DETAILS_SEPARATOR = " · "

fun SourceData.toSource(): Source = Source(
    id = id,
    name = name,
    description = description,
    details = toDetails(category, language, country),
)

// Turns "general", "en", "us" into "General · EN · US".
private fun toDetails(category: String?, language: String?, country: String?): String =
    listOfNotNull(
        category?.replaceFirstChar { it.uppercase() },
        language?.uppercase(),
        country?.uppercase(),
    ).filter { it.isNotBlank() }
        .joinToString(DETAILS_SEPARATOR)

package dd.canh.dailyaipulse.sources.presentation

import dd.canh.dailyaipulse.sources.data.SourceApi
import dd.canh.dailyaipulse.sources.data.SourceData
import dd.canh.dailyaipulse.sources.data.SourcesResponse

class FakeSourceApi(
    private val sources: List<SourceData> = emptyList(),
    private val error: Exception? = null,
) : SourceApi {

    override suspend fun getSources(): SourcesResponse {
        error?.let { throw it }
        return SourcesResponse(status = "ok", sources = sources)
    }
}

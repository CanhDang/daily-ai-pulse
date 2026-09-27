package dd.canh.dailyaipulse.sources.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceRepository @Inject constructor(
    private val sourceApi: SourceApi,
) {

    suspend fun getSources(): List<SourceData> =
        sourceApi.getSources().sources
}

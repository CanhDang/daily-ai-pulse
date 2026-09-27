package dd.canh.dailyaipulse.sources.data

import retrofit2.http.GET

interface SourceApi {

    @GET("v2/top-headlines/sources")
    suspend fun getSources(): SourcesResponse
}

package dd.canh.dailyaipulse.sources.presentation

import dd.canh.dailyaipulse.sources.data.SourceData
import dd.canh.dailyaipulse.sources.data.SourceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SourceViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val sourceData = SourceData(
        id = "abc-news",
        name = "ABC News",
        description = "Breaking news",
        url = "https://abcnews.go.com",
        category = "general",
        language = "en",
        country = "us",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        val viewModel = createViewModel(FakeSourceApi())

        assertEquals(SourceUIState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `emits Success with mapped sources`() = runTest(testDispatcher) {
        val viewModel = createViewModel(FakeSourceApi(sources = listOf(sourceData)))

        advanceUntilIdle()

        val expected = Source(
            id = "abc-news",
            name = "ABC News",
            description = "Breaking news",
            details = "General · EN · US",
        )
        assertEquals(SourceUIState.Success(listOf(expected)), viewModel.uiState.value)
    }

    @Test
    fun `emits user-friendly Error when the request fails`() = runTest(testDispatcher) {
        val viewModel = createViewModel(FakeSourceApi(error = IOException("No internet")))

        advanceUntilIdle()

        assertEquals(SourceUIState.Error("Something went wrong, please try again later"), viewModel.uiState.value)
    }

    private fun createViewModel(api: FakeSourceApi) =
        SourceViewModel(SourceRepository(api))
}

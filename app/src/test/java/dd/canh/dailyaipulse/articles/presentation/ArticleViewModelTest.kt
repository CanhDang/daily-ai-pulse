package dd.canh.dailyaipulse.articles.presentation

import dd.canh.dailyaipulse.articles.data.ArticleData
import dd.canh.dailyaipulse.articles.data.ArticleRepository
import dd.canh.dailyaipulse.articles.data.SourceData
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
class ArticleViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val articleData = ArticleData(
        source = SourceData(id = "cnn", name = "CNN"),
        title = "Title",
        description = null,
        imageUrl = "",
        date = "not-a-date",
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
        val viewModel = createViewModel(FakeArticleApi())

        assertEquals(ArticleUIState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `emits Success with mapped articles`() = runTest(testDispatcher) {
        val viewModel = createViewModel(FakeArticleApi(articles = listOf(articleData)))

        advanceUntilIdle()

        val expected = Article(
            title = "Title",
            description = null,
            imageUrl = "",
            date = "not-a-date",
            sourceName = "CNN",
        )
        assertEquals(ArticleUIState.Success(listOf(expected)), viewModel.uiState.value)
    }

    @Test
    fun `emits Error when the request fails`() = runTest(testDispatcher) {
        val viewModel = createViewModel(FakeArticleApi(error = IOException("No internet")))

        advanceUntilIdle()

        assertEquals(ArticleUIState.Error("No internet"), viewModel.uiState.value)
    }

    private fun createViewModel(api: FakeArticleApi) =
        ArticleViewModel(ArticleRepository(api))
}

package dd.canh.dailyaipulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dd.canh.dailyaipulse.articles.data.ArticleRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val GENERIC_ERROR_MESSAGE = "Something went wrong, please try again later"

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticleUIState>(ArticleUIState.Loading)
    val uiState: StateFlow<ArticleUIState> = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            emitState(ArticleUIState.Loading)
            try {
                val articles = articleRepository.getArticles().map { it.toArticle() }
                emitState(ArticleUIState.Success(articles))
            } catch (e: CancellationException) {
                // Cancellation must propagate, otherwise the coroutine keeps running.
                throw e
            } catch (e: Exception) {
                Timber.e(e, "Failed to load articles")
                emitState(ArticleUIState.Error(GENERIC_ERROR_MESSAGE))
            }
        }
    }

    private fun emitState(state: ArticleUIState) {
        Timber.d("Emitting state: %s", state)
        _uiState.value = state
    }
}

package dd.canh.dailyaipulse.articles.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dd.canh.dailyaipulse.R
import dd.canh.dailyaipulse.articles.presentation.Article
import dd.canh.dailyaipulse.articles.presentation.ArticleUIState
import dd.canh.dailyaipulse.articles.presentation.ArticleViewModel
import dd.canh.dailyaipulse.common.ui.ErrorContent
import dd.canh.dailyaipulse.common.ui.LoadingContent

@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArticleScreenContent(uiState = uiState, modifier = modifier)
}

@Composable
fun ArticleScreenContent(
    uiState: ArticleUIState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.articles),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
        )
        when (uiState) {
            ArticleUIState.Loading -> LoadingContent()
            is ArticleUIState.Error -> ErrorContent(message = uiState.message)
            is ArticleUIState.Success -> ArticleList(articles = uiState.articles)
        }
    }
}

@Composable
private fun ArticleList(articles: List<Article>) {
    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
        items(articles) { article ->
            ArticleItem(article = article)
            HorizontalDivider()
        }
    }
}

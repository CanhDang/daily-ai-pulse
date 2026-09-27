package dd.canh.dailyaipulse.sources.ui

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
import dd.canh.dailyaipulse.common.ui.ErrorContent
import dd.canh.dailyaipulse.common.ui.LoadingContent
import dd.canh.dailyaipulse.sources.presentation.Source
import dd.canh.dailyaipulse.sources.presentation.SourceUIState
import dd.canh.dailyaipulse.sources.presentation.SourceViewModel

@Composable
fun SourceScreen(
    modifier: Modifier = Modifier,
    viewModel: SourceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SourceScreenContent(uiState = uiState, modifier = modifier)
}

@Composable
fun SourceScreenContent(
    uiState: SourceUIState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.sources),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
        )
        when (uiState) {
            SourceUIState.Loading -> LoadingContent()
            is SourceUIState.Error -> ErrorContent(message = uiState.message)
            is SourceUIState.Success -> SourceList(sources = uiState.sources)
        }
    }
}

@Composable
private fun SourceList(sources: List<Source>) {
    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
        items(sources, key = { it.id }) { source ->
            SourceItem(source = source)
            HorizontalDivider()
        }
    }
}

package dd.canh.dailyaipulse.sources.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dd.canh.dailyaipulse.common.presentation.GENERIC_ERROR_MESSAGE
import dd.canh.dailyaipulse.sources.data.SourceRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SourceUIState>(SourceUIState.Loading)
    val uiState: StateFlow<SourceUIState> = _uiState.asStateFlow()

    init {
        loadSources()
    }

    fun loadSources() {
        viewModelScope.launch {
            emitState(SourceUIState.Loading)
            try {
                val sources = sourceRepository.getSources().map { it.toSource() }
                emitState(SourceUIState.Success(sources))
            } catch (e: CancellationException) {
                // Cancellation must propagate, otherwise the coroutine keeps running.
                throw e
            } catch (e: Exception) {
                Timber.e(e, "Failed to load sources")
                emitState(SourceUIState.Error(GENERIC_ERROR_MESSAGE))
            }
        }
    }

    private fun emitState(state: SourceUIState) {
        Timber.d("Emitting state: %s", state)
        _uiState.value = state
    }
}

package nd.phuc.core.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

inline fun <T> ViewModel.load(
    isLoading: MutableStateFlow<Boolean>? = null,
    crossinline action: suspend () -> T
) {
    isLoading?.update { true }
    viewModelScope.launch {
        action()
        isLoading?.update { false }
    }
}

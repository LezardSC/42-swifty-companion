package com.fortytwo.swifty.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fortytwo.swifty.data.model.IntraUser
import com.fortytwo.swifty.data.network.NetworkModule
import com.fortytwo.swifty.data.repository.IntraRepository
import com.fortytwo.swifty.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: IntraRepository = IntraRepository(NetworkModule.intraApi)
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<IntraUser>>(UiState.Idle)
    val state: StateFlow<UiState<IntraUser>> = _state.asStateFlow()

    fun search(login: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = repository.findUser(login)
        }
    }

    fun resetState() {
        if (_state.value !is UiState.Loading) _state.value = UiState.Idle
    }
}
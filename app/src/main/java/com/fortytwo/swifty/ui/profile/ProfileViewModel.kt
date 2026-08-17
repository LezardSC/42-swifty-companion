package com.fortytwo.swifty.ui.profile

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

class ProfileViewModel(
    private val repository: IntraRepository = IntraRepository(NetworkModule.intraApi)
): ViewModel() {
    private val _state = MutableStateFlow<UiState<IntraUser>>(UiState.Loading)
    val state: StateFlow<UiState<IntraUser>> = _state.asStateFlow()

    fun load(login: String) {
        viewModelScope.launch {
            _state.value = repository.findUser(login)
        }
    }
}
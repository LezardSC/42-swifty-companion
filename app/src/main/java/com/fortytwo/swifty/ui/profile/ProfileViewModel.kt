package com.fortytwo.swifty.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fortytwo.swifty.data.model.Coalition
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

    private val _coalition = MutableStateFlow<Coalition?>(null)
    val coalition: StateFlow<Coalition?> = _coalition.asStateFlow()

    fun load(login: String) {
        viewModelScope.launch {
            val result = repository.findUser(login)
            // Fetched before revealing the profile so it doesn't flash the default color.
            // Sequential on purpose, to stay under the API rate limit
            if (result is UiState.Success) _coalition.value = repository.findCoalition(login)
            _state.value = result
        }
    }
}
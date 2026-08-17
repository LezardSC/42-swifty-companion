package com.fortytwo.swifty.ui.search

import com.fortytwo.swifty.data.network.NetworkModule
import com.fortytwo.swifty.data.repository.IntraRepository

class SearchViewModel(
    private val repository: IntraRepository = IntraRepository(NetworkModule.intraApi)
) {
}
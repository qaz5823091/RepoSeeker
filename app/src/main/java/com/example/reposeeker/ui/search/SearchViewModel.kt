package com.example.reposeeker.ui.search

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reposeeker.data.DetailUser
import com.example.reposeeker.data.SimpleUser
import com.example.reposeeker.service.GithubServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val githubServiceRepository: GithubServiceRepository
) : ViewModel() {
    private val _isDetailVisible = MutableStateFlow(false)
    val isDetailVisible: StateFlow<Boolean> = _isDetailVisible

    private val _searchState = MutableStateFlow(SearchState.SUCCESS)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    var searchText by mutableStateOf("")
        private set

    private var totalPage by mutableIntStateOf(1)

    private val _userList = MutableStateFlow<List<SimpleUser>>(emptyList())
    val userList: StateFlow<List<SimpleUser>> = _userList.asStateFlow()

    private val _specificUser = MutableStateFlow(DetailUser())
    val specificUser: StateFlow<DetailUser> = _specificUser.asStateFlow()

    fun updateSearchText(value: String) {
        searchText = value
        searchUsers(searchText)
    }

    private fun searchUsers(query: String) {
        if (query.isEmpty()) {
            _userList.value = emptyList()
            _searchState.value = SearchState.SUCCESS
            return
        }
        viewModelScope.launch {
            try {
                _searchState.value = SearchState.LOADING
                totalPage = 1
                val users = githubServiceRepository.getUsers(query)
                if (users.isNotEmpty()) {
                    _userList.value = users
                    _searchState.value = SearchState.SUCCESS
                } else {
                    _userList.value = emptyList()
                    _searchState.value = SearchState.FAILED
                }
            } catch (_: Exception) {
                _searchState.value = SearchState.FAILED
            }
        }
    }

    fun loadMoreUsers() {
        viewModelScope.launch {
            try {
                totalPage = totalPage.inc()
                val users = githubServiceRepository.getUsers(searchText, totalPage)
                _userList.value += users
            } catch (_: Exception) {
                _searchState.value = SearchState.FAILED
            }
        }
    }

    fun searchUser(query: String) {
        viewModelScope.launch {
            val user = githubServiceRepository.getSpecificUser(query)
            _specificUser.value = user
            _isDetailVisible.value = true
            Log.d("45789345", user.toString())
        }
    }

    fun dismissDialog() {
        _isDetailVisible.value = false
    }
}
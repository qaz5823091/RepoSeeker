package com.example.reposeeker

import com.example.reposeeker.service.GithubService
import com.example.reposeeker.service.GithubServiceHolder
import com.example.reposeeker.service.GithubServiceRepository
import com.example.reposeeker.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single { GithubService.getInstance() }
    single { GithubServiceHolder() }
    single { GithubServiceRepository(get()) }
    viewModel { SearchViewModel(get()) }
}
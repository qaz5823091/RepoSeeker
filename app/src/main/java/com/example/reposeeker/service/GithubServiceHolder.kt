package com.example.reposeeker.service

import com.example.reposeeker.data.GithubUser
import com.example.reposeeker.data.GithubUsers

class GithubServiceHolder : GithubService {
    private val service: GithubService = GithubService.getInstance()

    override suspend fun searchUsers(
        username: String,
        page: Int
    ): GithubUsers {
        return service.searchUsers(username, page)
    }

    override suspend fun getUser(username: String): GithubUser {
        return service.getUser(username)
    }
}
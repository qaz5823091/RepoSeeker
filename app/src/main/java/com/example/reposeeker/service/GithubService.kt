package com.example.reposeeker.service

import com.example.reposeeker.data.GithubUser
import com.example.reposeeker.data.GithubUsers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") username: String,
        @Query("page") page: Int
    ): GithubUsers

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GithubUser

    companion object {
        private var githubService: GithubService? = null
        fun getInstance(): GithubService = githubService ?:
            Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService::class.java)
    }
}

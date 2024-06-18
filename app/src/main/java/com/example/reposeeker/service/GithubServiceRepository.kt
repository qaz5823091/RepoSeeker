package com.example.reposeeker.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.reposeeker.data.DetailUser
import com.example.reposeeker.data.GithubUser
import com.example.reposeeker.data.SimpleUser
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class GithubServiceRepository(
    private val githubServiceHolder: GithubServiceHolder
) {
    suspend fun getUsers(
        username: String,
        page: Int = 1
    ): List<SimpleUser> {
        return try {
            val users = githubServiceHolder.searchUsers(username, page).items ?: emptyList()
            users.map {
                SimpleUser(
                    username = it.login ?: "",
                    avatarUrl = it.avatarUrl ?: "",
                    profileUrl = it.htmlUrl ?: "",
                )
            }
        } catch (e: Exception) {
            Log.d("3485794385", e.toString())
            emptyList()
        }
    }

    @SuppressLint("NewApi")
    suspend fun getSpecificUser(username: String): DetailUser {
        return try {
            val user = githubServiceHolder.getUser(username)
            val createdTime = ZonedDateTime
                .parse(user.createdAt, DateTimeFormatter.ISO_DATE_TIME)
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            DetailUser(
                name = user.name ?: "",
                account = user.login ?: "",
                location  = user.location ?: "",
                email = user.email ?: "",
                avatarUrl = user.avatarUrl ?: "",
                profileUrl = user.htmlUrl ?: "",
                followers = user.followers ?: 0,
                following = user.following ?: 0,
                createdAt = createdTime.toString()
            )
        } catch (e: Exception) {
            DetailUser()
        }
    }
}
package com.example.reposeeker.data

import com.google.gson.annotations.SerializedName

data class GithubUsers(
    @SerializedName("items")
    val items: List<GithubUser>? = null
) {
    data class GithubUser(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("login")
        val login: String? = null,
        @SerializedName("avatar_url")
        val avatarUrl: String? = null,
        @SerializedName("html_url")
        val htmlUrl: String? = null,
        @SerializedName("followers")
        val followers: Int? = null,
        @SerializedName("following")
        val following: Int? = null
    )
}
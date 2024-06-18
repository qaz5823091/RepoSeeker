package com.example.reposeeker.data

import com.google.gson.annotations.SerializedName

data class GithubUser (
    @SerializedName("login")
    val login: String? = null,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("followers")
    val followers: Int? = null,
    @SerializedName("following")
    val following: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)
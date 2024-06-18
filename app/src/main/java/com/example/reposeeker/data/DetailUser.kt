package com.example.reposeeker.data

data class DetailUser (
    val name: String = "",
    val account: String = "",
    val location: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val profileUrl: String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val createdAt: String = ""
)
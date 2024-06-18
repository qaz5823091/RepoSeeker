package com.example.reposeeker.ui.search

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.reposeeker.data.DetailUser
import com.example.reposeeker.data.SimpleUser

class SimpleUserDataProvider : PreviewParameterProvider<SimpleUser> {
    override val values = sequenceOf(
        SimpleUser(
            username = "qaz58523091",
            avatarUrl = "https://avatars.githubusercontent.com/u/37237234?v=4",
            profileUrl = "https://github.com/qaz5823091",
        )
    )
}

class DetailUserDataProvider : PreviewParameterProvider<DetailUser> {
    override val values = sequenceOf(
        DetailUser (
            name = "羲加加",
            account = "qaz5823091",
            location = "Tainan City｜ChiaYi City & ChangHua Country",
            email = "cpp1092020@gmail.com",
            avatarUrl = "https://avatars.githubusercontent.com/u/37237234?v=4",
            profileUrl = "",
            followers = 17,
            following = 19,
            createdAt = "2018-03-10T08:00:49Z"
        )
    )
}
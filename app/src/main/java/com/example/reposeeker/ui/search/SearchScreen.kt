package com.example.reposeeker.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.reposeeker.R
import com.example.reposeeker.data.DetailUser
import com.example.reposeeker.data.SimpleUser
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchViewModel>()
    val searchState by viewModel.searchState.collectAsState()
    val userList by viewModel.userList.collectAsState()
    val isDetailVisible by viewModel.isDetailVisible.collectAsState()
    val specificUser by viewModel.specificUser.collectAsState()
    val isSearched by remember {
        derivedStateOf { userList.isNotEmpty() }
    }
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        SearchBar(
            searchText = viewModel.searchText,
            onSearchTextChanged = { viewModel.updateSearchText(it) }
        )
        Text("$searchState")
        if (isSearched) {
            UserItemList(
                list = userList,
                state = searchState,
                onReachBottom = { viewModel.loadMoreUsers() }
            )
        } else {
            SearchHint()
        }


        if (isDetailVisible) {
            DetailUserDialog(user = specificUser, onDismiss = { viewModel.dismissDialog() })
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null
            )
        }
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun UserItemList(
    list: List<SimpleUser>,
    state: SearchState,
    onReachBottom: () -> Unit
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading)
    )
    val listState = rememberLazyListState()
    val reachBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(reachBottom) {
        if (reachBottom) {
            onReachBottom()
            return@LaunchedEffect
        }

        val counter = list.count().div(30)
        if (counter == 1) {
            listState.scrollToItem(0)
        }
    }

    when (state) {
        SearchState.SUCCESS -> {
            LazyColumn(state = listState) {
                itemsIndexed(items = list) { _, user ->
                    UserItem(user = user)
                }
            }
        }
        SearchState.LOADING,
        SearchState.FAILED -> {
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }
    }
}

@Preview
@Composable
fun SearchHint() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Try to search something!",
            style = typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = colorScheme.outline,
        )
    }
}

@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserItem(
    @PreviewParameter(SimpleUserDataProvider::class) user: SimpleUser,
) {
    val viewModel = koinViewModel<SearchViewModel>()
    Card(
        shape = RectangleShape,
        onClick = { viewModel.searchUser(user.username) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = user.username,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DetailUserDialog(
    user: DetailUser,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        DetailUserCard(user = user, onDismiss = onDismiss)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailUserCard(
    user: DetailUser,
    onDismiss: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(16.dp),
        content = {
            Row(modifier = Modifier.align(Alignment.End)) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            Column(
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Followers",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = user.followers.toString(),
                                textAlign = TextAlign.Center,
                                style = typography.titleLarge
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Following",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onPrimaryContainer
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = user.following.toString(),
                                textAlign = TextAlign.Center,
                                style = typography.titleLarge
                            )
                        }
                    }
                }
                FlowRow {
                    Icon(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        imageVector = Icons.Default.Person, contentDescription = "Username"
                    )
                    Text(
                        text = user.name,
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = "(${user.account})",
                        color = colorScheme.tertiary
                    )
                }
                Row {
                    Icon(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        imageVector = Icons.Default.Email, contentDescription = "Email"
                    )
                    Text(
                        text = when (user.email) {
                            "" -> "(empty)"
                            else -> user.email
                        },
                        color = when (user.email) {
                            "" -> colorScheme.outline
                            else -> colorScheme.onPrimaryContainer
                        }
                    )
                }
                Row {
                    Icon(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        imageVector = Icons.Default.LocationOn, contentDescription = "Location"
                    )
                    Text(
                        text = when (user.location) {
                            "" -> "(empty)"
                            else -> user.location
                        },
                        color = when (user.location) {
                            "" -> colorScheme.outline
                            else -> colorScheme.onPrimaryContainer
                        }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Since ${user.createdAt}")
                    TextButton(
                        onClick = { uriHandler.openUri(user.profileUrl) },
                    ) {
                        Text("See Profile")
                    }
                }
            }
        }
    )
}
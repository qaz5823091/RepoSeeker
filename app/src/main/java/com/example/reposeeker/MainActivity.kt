package com.example.reposeeker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.reposeeker.ui.search.SearchScreen
import com.example.reposeeker.ui.theme.RepoSeekerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepoSeekerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(it)) {
                        SearchScreen()
                    }
                }
            }
        }
    }
}
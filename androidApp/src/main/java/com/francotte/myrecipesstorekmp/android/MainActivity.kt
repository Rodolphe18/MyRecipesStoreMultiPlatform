package com.francotte.myrecipesstorekmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.francotte.data.interfaces.CategoriesRepository
import kotlinx.coroutines.flow.first
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    // Resolved from the Koin graph wired in MyRecipesApplication.
    private val categoriesRepository: CategoriesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SmokeTestView(categoriesRepository)
                }
            }
        }
    }
}

@Composable
private fun SmokeTestView(repository: CategoriesRepository) {
    var status by remember { mutableStateOf("Loading categories…") }

    // End-to-end smoke test: Ktor network call → Room persistence → Flow read.
    LaunchedEffect(Unit) {
        status = runCatching {
            repository.refreshAllMealCategories(force = true)
            val categories = repository.observeAllMealCategories().first()
            "Loaded ${categories.size} categories (network + SQLDelight)"
        }.getOrElse { "Failed: ${it.message}" }
    }

    Text(text = status, modifier = Modifier.padding(16.dp))
}

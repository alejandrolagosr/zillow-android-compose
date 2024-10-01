package com.flagos.zillow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.flagos.zillow.DogImagesViewModel.DogImagesIntent
import com.flagos.zillow.data.DogImagesApiClient
import com.flagos.zillow.data.DogImagesRepository
import com.flagos.zillow.ui.theme.ZillowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZillowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ImageListScreen(
                            viewModel = DogImagesViewModel(
                                dogImagesRepository = DogImagesRepository(
                                    dogImagesApiClient = DogImagesApiClient()
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageListScreen(viewModel: DogImagesViewModel) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> {
            CircularProgressIndicator()
        }

        state.error != null -> {
            Text(text = "Error: ${state.error}")
        }

        else -> {
            DogImagesList(state.images)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(DogImagesIntent.GetImages)
    }
}

@Composable
fun DogImagesList(dogImagesUrls: List<String>) {
    LazyColumn {
        items(dogImagesUrls) { dogImageUrl ->
            ImageItem(imageUrl = dogImageUrl)
        }
    }
}

@Composable
fun ImageItem(imageUrl: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentScale = ContentScale.Crop
    )
}
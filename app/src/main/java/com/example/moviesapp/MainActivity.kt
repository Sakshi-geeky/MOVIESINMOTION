package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.ui.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.CardDefaults

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAppTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(moviesViewModel: MovieViewModel = hiltViewModel()) {

    val trendingMovies = moviesViewModel.trendingMoviesList.observeAsState()

    Column(modifier = Modifier
        .background(Color.DarkGray)
        .padding(start = 16.dp, end = 16.dp, top = 34.dp)) {
        Text(
            text = "What do you want to watch?",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Trending",
            style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
        )

//        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(trendingMovies.value?.results?.size ?: 0) { movie ->
                MovieItem(movie = trendingMovies.value?.results?.get(movie) ?: Movie()){

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MovieTabsScreen(movieViewModel = moviesViewModel)
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(4.dp)
            .clickable { onMovieClick(movie) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), // Set card color here
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                    builder = {
                        scale(Scale.FILL)
                    }
                ),
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = movie.title,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun MovieTabsScreen(movieViewModel: MovieViewModel) {
    val categories = MovieCategory.values()
    var selectedTab by remember { mutableStateOf(MovieCategory.NowPlaying)}

    Column {
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = Color.DarkGray,
            contentColor = Color.White
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = selectedTab == category,
                    onClick = { selectedTab = category },
                    modifier = Modifier.weight(1f),
                    text = {
                        Text(
                            text = category.title,
                            fontSize = 8.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            softWrap = false
                        )
                    }
                )
            }
        }
        MovieGridContent(category = selectedTab, movieViewModel = movieViewModel)
    }
}


@Composable
fun MovieGridContent(category: MovieCategory, movieViewModel: MovieViewModel) {

    val nowPlayingMovies = movieViewModel.nowPlayingMovies.observeAsState()
    val upComingMoviesList = movieViewModel.upComingMoviesList.observeAsState()
    val topRatedMoviesList = movieViewModel.topRatedMoviesList.observeAsState()
    val popularMoviesList = movieViewModel.popularMoviesList.observeAsState()

    val movies = when (category) {
        MovieCategory.NowPlaying -> nowPlayingMovies.value?.results
        MovieCategory.Upcoming -> upComingMoviesList.value?.results
        MovieCategory.TopRated -> topRatedMoviesList.value?.results
        MovieCategory.Popular -> popularMoviesList.value?.results
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies?.size ?: 0) { movie ->
            MovieItem(movie = movies?.get(movie) ?: Movie()) {
            }
        }
    }
}

enum class MovieCategory(val title: String) {
    NowPlaying("Now Playing"),
    Upcoming("Upcoming"),
    TopRated("Top Rated"),
    Popular("Popular")
}

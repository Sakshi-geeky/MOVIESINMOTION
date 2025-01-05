package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocalMovies

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAppTheme {
                MyAppNavGraph()
            }
        }
    }
}

@Composable
fun MyAppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") {
            HomeScreen(navcontroller=navController)
        }

        composable("detailScreen") {
            MovieDetailScreen(onBackPressed = { navController.popBackStack() })
        }

    }
}
@Composable
fun HomeScreen(moviesViewModel: MovieViewModel = hiltViewModel(),navcontroller: NavHostController) {

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
                navcontroller.navigate("detailScreen")
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

@Composable
fun MovieDetailScreen(onBackPressed: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("About Movie", "Cast")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp) // Added top padding for the whole screen
    ) {
        // Top section with banner image and header
        Box(modifier = Modifier.height(240.dp)) { // Reduced total height
            // Banner Image
            Image(
                painter = rememberImagePainter(
                    data = "https://image.tmdb.org/t/p/w500${""}",
                    builder = {
                        scale(Scale.FILL)
                    }
                ),
                contentDescription = "Movie Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Reduced banner height
                contentScale = ContentScale.Crop
            )

            // Header with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp), // Increased top padding
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Details",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                // Empty spacer for alignment
                Spacer(modifier = Modifier.size(24.dp))
            }

            // Overlapping movie poster
            Card(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .offset(y = 140.dp) // Adjusted offset
                    .width(100.dp)  // Reduced width
                    .height(140.dp), // Reduced height
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = "https://image.tmdb.org/t/p/w500${""}",
                        builder = {
                            scale(Scale.FILL)
                        }
                    ),
                    contentDescription = "Movie Poster",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Movie info section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 130.dp, end = 16.dp)
        ) {
            Text(
                text = "Movie Title",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Text(
                text = "Date - Genres - Duration",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie metadata with icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Year",
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Year",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Duration",
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "00 Minutes",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalMovies,
                    contentDescription = "Genre",
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Action",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(2.dp)
                        .background(Color.White)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedTab == index) Color.White else Color.Gray
                        )
                    }
                )
            }
        }

        // Tab content in a scrollable column
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> AboutMovieTab()
                1 -> CastTab()
            }
        }
    }
}

@Composable
fun AboutMovieTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun CastTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Cast information will go here",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
enum class MovieCategory(val title: String) {
    NowPlaying("Now Playing"),
    Upcoming("Upcoming"),
    TopRated("Top Rated"),
    Popular("Popular")
}

package com.example.moviesapp

import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.moviesapp.data.model.CastMember

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

        composable("detailScreen/{MovieId}") {
            val movieId = it.arguments?.getString("MovieId")
            MovieDetailScreen(onBackPressed = { navController.popBackStack() },movieId=movieId,
                onPlayVideo = { videoUrl ->
                    // Encode the URL before navigation
                    val encodedUrl = Uri.encode(videoUrl)
                    navController.navigate("videoPlayer/$encodedUrl")
                })
        }
        composable(
            "videoPlayer/{videoUrl}"
        ) {
            val encodedUrl = it.arguments?.getString("videoUrl")
            // Decode the URL before using it
            val decodedUrl = Uri.decode(encodedUrl)
            FullScreenVideoPlayer(
                videoUrl = decodedUrl,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}
@OptIn(UnstableApi::class)
@Composable
fun FullScreenVideoPlayer(
    videoUrl: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            setMediaItem(mediaItem)
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    useController = true
                    controllerShowTimeoutMs = 2000 // Controls hide after 2 seconds
                    controllerHideOnTouch = true
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Back button overlay
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
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
                navcontroller.navigate("detailScreen/${it.id}")
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
fun MovieDetailScreen(moviesViewModel: MovieViewModel = hiltViewModel(),onBackPressed: () -> Unit,movieId:String?,onPlayVideo: (String) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("About Movie", "Cast")
    LaunchedEffect(Unit) {
        if (movieId != null) {
            moviesViewModel.getMovieDetails(movieId.toInt())
            moviesViewModel.getMovieReviews(movieId.toInt())
            moviesViewModel.getCastDetails(movieId.toInt())
            moviesViewModel.getMovieTrailers(movieId.toInt())
        }

    }
    val movieDetails = moviesViewModel.movieDetails.observeAsState()
    val reviews = moviesViewModel.reviews.observeAsState()
    val castDetails = moviesViewModel.castDetails.observeAsState()
    val trailers = moviesViewModel.trailers.observeAsState()
    val trailerUrl = remember(trailers.value) {
        trailers.value?.results?.firstOrNull()?.let { trailer ->
            "https://www.youtube.com/watch?v=${trailer.key}"
        }
    }
    var isPlaying by remember { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp) // Added top padding for the whole screen
    ) {
        // Top section with banner image and header
        Box(modifier = Modifier.height(240.dp)) { // Reduced total height
            if (isPlaying && trailerUrl != null) {
                VideoPlayer(
                    videoUrl = trailerUrl,
                    isFullScreen = isFullScreen,
                    onFullScreenToggle = { isFullScreen = !isFullScreen }
                )
            } else {
                // Banner Image
                Image(
                    painter = rememberImagePainter(
                        data = "https://image.tmdb.org/t/p/w500${movieDetails.value?.backdrop_path}",
                        builder = {
                            scale(Scale.FILL)
                        }
                    ),
                    contentDescription = "Movie Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)// Reduced banner height
                        .clickable {
                            trailerUrl?.let { url ->
                                onPlayVideo(url)}
                        },

                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
                            trailerUrl?.let { url ->
                                onPlayVideo(url)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (trailerUrl != null) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Trailer",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

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
                        data = "https://image.tmdb.org/t/p/w500${movieDetails.value?.poster_path}",
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
                text = movieDetails.value?.title ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Text(
                text = movieDetails.value?.release_date ?: "",
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
                    text = movieDetails.value?.release_date ?: "",
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
                    text = movieDetails.value?.runtime.toString() ?: "",
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
                    text = movieDetails.value?.genres?.joinToString(", ") { it.name } ?: "",
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
                0 -> AboutMovieTab(movieDetails.value?.overview ?: "")
                1 -> CastTab(   castDetails.value?.cast ?: emptyList())
            }
        }
    }
}

@Composable
fun AboutMovieTab(movieOverview: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text= movieOverview,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun CastTab(castMembers: List<CastMember>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(castMembers.size) {
            CastMemberItem(name = castMembers.get(it).name, imageUrl = castMembers.get(it).profile_path)
        }
    }
}
@Composable
fun CastMemberItem(
    name: String = "Name",
    imageUrl: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Circular image container
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.DarkGray)
        ) {
            if (imageUrl != null) {
//                AsyncImage(
//                    model = imageUrl,
//                    contentDescription = "Actor Image",
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
                Image(
                    painter = rememberImagePainter(
                        data = "https://image.tmdb.org/t/p/w500${imageUrl}",
                        builder = {
                            scale(Scale.FILL)
                        }
                    ),
                    contentDescription = "Movie Poster",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Actor name
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    isFullScreen: Boolean = false,
    onFullScreenToggle: () -> Unit
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            setMediaItem(mediaItem)
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isFullScreen) MATCH_PARENT.dp else 200.dp)
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    useController = true
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

enum class MovieCategory(val title: String) {
    NowPlaying("Now Playing"),
    Upcoming("Upcoming"),
    TopRated("Top Rated"),
    Popular("Popular")
}

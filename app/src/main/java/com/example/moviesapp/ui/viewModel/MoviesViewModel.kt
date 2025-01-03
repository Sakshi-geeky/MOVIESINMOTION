package com.example.moviesapp.ui.viewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.model.CreditsResponse
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.model.NowPlayingMoviesResponse
import com.example.moviesapp.data.model.Review
import com.example.moviesapp.data.model.ReviewsResponse
import com.example.moviesapp.data.model.TrendingMoviesResponse
import com.example.moviesapp.data.model.Video
import com.example.moviesapp.data.model.VideosResponse
import com.example.moviesapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository  // Make sure MovieRepository is provided through DI, e.g., Hilt
) : ViewModel() {
    private val _trendingMoviesList = MutableLiveData<TrendingMoviesResponse>()
    val trendingMoviesList: LiveData<TrendingMoviesResponse> = _trendingMoviesList

    private val _nowPlayingMovies= MutableLiveData<NowPlayingMoviesResponse>()
    val nowPlayingMovies: LiveData<NowPlayingMoviesResponse> = _nowPlayingMovies

    private val _popularMoviesList = MutableLiveData<TrendingMoviesResponse>()
    val popularMoviesList: LiveData<TrendingMoviesResponse> = _popularMoviesList

    private val _topRatedMoviesList = MutableLiveData<TrendingMoviesResponse>()
    val topRatedMoviesList: LiveData<TrendingMoviesResponse> = _topRatedMoviesList

    private val _upComingMoviesList = MutableLiveData<TrendingMoviesResponse>()
    val upComingMoviesList: LiveData<TrendingMoviesResponse> = _upComingMoviesList

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val _reviews = MutableLiveData<ReviewsResponse>()
    val reviews: LiveData<ReviewsResponse> = _reviews

    private val _castDetails = MutableLiveData<CreditsResponse>()
    val castDetails: LiveData<CreditsResponse> = _castDetails

    private val _trailers = MutableLiveData<VideosResponse>()
    val trailers: LiveData<VideosResponse> = _trailers

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String> = _apiError

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    init {
        getTrendingMovies("day")
        getNowPlayingMovies()
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }

    fun getTrendingMovies(timeWindow: String) {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getTrendingMovies(timeWindow)) {
                is com.example.moviesapp.data.model.Result.Success -> _trendingMoviesList.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getNowPlayingMovies()) {
                is com.example.moviesapp.data.model.Result.Success -> _nowPlayingMovies.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getPopularMovies()) {
                is com.example.moviesapp.data.model.Result.Success -> _popularMoviesList.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getTopRatedMovies()) {
                is com.example.moviesapp.data.model.Result.Success -> _topRatedMoviesList.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getUpcomingMovies() {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getUpcomingMovies()) {
                is com.example.moviesapp.data.model.Result.Success -> _upComingMoviesList.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getMovieDetails(movieId)) {
                is com.example.moviesapp.data.model.Result.Success -> _movieDetails.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getMovieReviews(movieId: Int) {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getMovieReviews(movieId)) {
                is com.example.moviesapp.data.model.Result.Success -> _reviews.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getCastDetails(movieId: Int) {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getCastDetails(movieId)) {
                is com.example.moviesapp.data.model.Result.Success -> _castDetails.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun getMovieTrailers(movieId: Int) {
        viewModelScope.launch {
            _showLoader.value = true
            when (val result = movieRepository.getMovieVideos(movieId)) {
                is com.example.moviesapp.data.model.Result.Success -> _trailers.value = result.data
                is com.example.moviesapp.data.model.Result.Error -> _apiError.value = result.message
            }
            _showLoader.value = false
        }
    }

    fun clearApiError() {
        _apiError.value = ""
    }
}

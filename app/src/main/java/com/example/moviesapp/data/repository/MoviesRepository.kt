package com.example.moviesapp.data.repository

import android.util.Log
import com.example.moviesapp.data.api.NetworkService
import com.example.moviesapp.data.model.CreditsResponse
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.model.NowPlayingMoviesResponse
import com.example.moviesapp.data.model.ReviewsResponse
import com.example.moviesapp.data.model.TrendingMoviesResponse
import com.example.moviesapp.data.model.VideosResponse
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApiService: NetworkService  // Assuming this is the interface name from Retrofit
) {

    suspend fun getTrendingMovies(timeWindow: String): com.example.moviesapp.data.model.Result<TrendingMoviesResponse> {
        return apiRequest { movieApiService.getTrendingMovies(timeWindow) }
    }

    suspend fun getNowPlayingMovies(): com.example.moviesapp.data.model.Result<NowPlayingMoviesResponse> {
        return apiRequest { movieApiService.getNowPlayingMovies() }
    }

    suspend fun getPopularMovies(): com.example.moviesapp.data.model.Result<TrendingMoviesResponse> {
        return apiRequest { movieApiService.getPopularMovies() }
    }

    suspend fun getTopRatedMovies(): com.example.moviesapp.data.model.Result<TrendingMoviesResponse> {
        return apiRequest { movieApiService.getTopRatedMovies() }
    }

    suspend fun getUpcomingMovies(): com.example.moviesapp.data.model.Result<TrendingMoviesResponse> {
        return apiRequest { movieApiService.getUpcomingMovies() }
    }

    suspend fun getMovieDetails(movieId: Int): com.example.moviesapp.data.model.Result<MovieDetails> {
        return apiRequest { movieApiService.getMovieDetails(movieId) }
    }

    suspend fun getMovieReviews(movieId: Int): com.example.moviesapp.data.model.Result<ReviewsResponse> {
        return apiRequest { movieApiService.getMovieReviews(movieId) }
    }

    suspend fun getCastDetails(movieId: Int): com.example.moviesapp.data.model.Result<CreditsResponse> {
        return apiRequest { movieApiService.getCastDetails(movieId) }
    }

    suspend fun getMovieVideos(movieId: Int): com.example.moviesapp.data.model.Result<VideosResponse> {
        return apiRequest { movieApiService.getMovieVideos(movieId) }
    }

    private suspend fun <T> apiRequest(call: suspend () -> Response<T>): com.example.moviesapp.data.model.Result<T> {
        return try {
            Log.d("api called" , "started")
            val response = call.invoke()
            Log.d("api called" , response.toString())

            if (response.isSuccessful && response.body() != null) {
                Log.d("trending api called" , response.body().toString())
                com.example.moviesapp.data.model.Result.Success(response.body()!!)
            } else {
                Log.d("trending api called" , response.errorBody().toString())
                com.example.moviesapp.data.model.Result.Error(response.errorBody()?.string() ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            com.example.moviesapp.data.model.Result.Error(e.localizedMessage ?: "Network request failed")
        }
    }
}
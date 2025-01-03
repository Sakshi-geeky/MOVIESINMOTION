package com.example.moviesapp.data.api

import com.example.moviesapp.data.model.CreditsResponse
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.model.NowPlayingMoviesResponse
import com.example.moviesapp.data.model.ReviewsResponse
import com.example.moviesapp.data.model.TrendingMoviesResponse
import com.example.moviesapp.data.model.VideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = "154ad8f9017ced85e1b45f006f50d4a0",
        @Query("page") page: Int = 1
    ): Response<TrendingMoviesResponse>

    // Now Playing Movies
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<NowPlayingMoviesResponse>

    // Popular Movies
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<TrendingMoviesResponse>

    // Top-Rated Movies
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<TrendingMoviesResponse>

    // Upcoming Movies
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): Response<TrendingMoviesResponse>

    // Movie Details
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<MovieDetails>

    // Movie Reviews
    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<ReviewsResponse>

    // Cast Details
    @GET("movie/{movie_id}/credits")
    suspend fun getCastDetails(
        @Path("movie_id") movieId: Int,
    ): Response<CreditsResponse>

    // Show Trailers
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): Response<VideosResponse>

}
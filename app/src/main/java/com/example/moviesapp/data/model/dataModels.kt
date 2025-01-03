package com.example.moviesapp.data.model

data class TrendingMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

data class Movie(
    val backdrop_path: String? = "",
    val id: Int = 1,
    val title: String = "",
    val original_title: String="",
    val overview: String = "",
    val poster_path: String? = "",
    val media_type: String = "",
    val adult: Boolean = true,
    val original_language: String = "",
    val genre_ids: List<Int> = emptyList(),
    val popularity: Double = 1.0,
    val release_date: String = "",
    val video: Boolean = true,
    val vote_average: Double = 1.0,
    val vote_count: Int = 1
)

// Use the same Movie class as in TrendingMoviesResponse
data class NowPlayingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

data class Dates(
    val maximum: String,
    val minimum: String
)

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val genres: List<Genre>,
    val runtime: Int?,
    val release_date: String?,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int,
    val original_language: String,
    val backdrop_path: String?
)

data class Genre(
    val id: Int,
    val name: String
)

data class ReviewsResponse(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val total_pages: Int,
    val total_results: Int
)

data class Review(
    val author: String,
    val content: String,
    val created_at: String,
    val updated_at: String,
    val author_details: AuthorDetails
)

data class AuthorDetails(
    val name: String,
    val username: String,
    val avatar_path: String?,
    val rating: Double?
)

data class CreditsResponse(
    val id: Int,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)

data class CastMember(
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val name: String,
    val profile_path: String?,
    val order: Int
)

data class CrewMember(
    val credit_id: String,
    val department: String,
    val job: String,
    val name: String,
    val profile_path: String?
)

data class VideosResponse(
    val id: Int,
    val results: List<Video>
)

data class Video(
    val id: String,
    val iso_639_1: String,
    val iso_3166_1: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
)



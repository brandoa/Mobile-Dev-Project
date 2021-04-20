package com.usf_mobile_dev.filmfriend.data_sources.tmdb_api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface TMDBInterface {
    @GET("discover/movie")
    Call<DiscoverResponse> discoverMovie(
            @Query(value = "api_key", encoded = true)
                    String api_key,
            @Query(value = "page", encoded = true)
                    Integer page,
            @Query(value = "primary_release_date.gte", encoded = true)
                    String primaryReleaseDateLowerBound,
            @Query(value = "primary_release_date.lte", encoded = true)
                    String primaryReleaseDateUpperBound,
            @Query(value = "vote_count.gte", encoded = true)
                    Integer voteCountLowerBound,
            @Query(value = "vote_count.lte", encoded = true)
                    Integer voteCountUpperBound,
            @Query(value = "vote_average.gte", encoded = true)
                    Double voteAverageLowerBound,
            @Query(value = "vote_average.lte", encoded = true)
                    Double voteAverageUpperBound,
            @Query(value = "with_genres", encoded = true)
                    String withGenres,
            @Query(value = "without_genres", encoded = true)
                    String withoutGenres,
            @Query(value = "with_runtime.gte", encoded = true)
                    Integer withRuntimeLowerBound,
            @Query(value = "with_runtime.lte", encoded = true)
                    Integer withRuntimeUpperBound,
            @Query(value = "with_watch_providers", encoded = true)
                    String withWatchProviders,
            @Query(value = "watch_region", encoded = true)
                    String watchRegion,
            @Query(value = "with_original_language", encoded = true)
                    String withOriginalLanguage
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query(value = "api_key", encoded = true)
                String api_key,
            @Query(value = "language", encoded = true)
                String language
    );

    @GET("configuration/languages")
    Call<List<LanguageResponse>> getLanguages(
            @Query(value = "api_key", encoded = true)
                    String api_key
    );

    @GET("watch/providers/movie")
    Call<WatchProviderResponse> getWatchProviders(
            @Query(value = "api_key", encoded = true)
                    String api_key,
            @Query(value = "language", encoded = true)
                    String language,
            @Query(value = "watch_region", encoded = true)
                    String watchRegion
    );
}

package com.usf_mobile_dev.filmfriend.data_sources.data_classes;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;

    private String overview;
    private int releaseYear;
    private Double rating;
    private int voteCount;

    private int tmdbMovieId;

    private String posterPath;
    private String backdropPath;

    public Movie() {
        this.title = "title";
        this.overview = "lorem ipsum";
        this.releaseYear = 1985;
        this.rating = 5.0;
        this.voteCount = 0;
        this.tmdbMovieId = 1;
        this.posterPath = "/";
        this.backdropPath = "/";
    }

    //Used for inserting into room.
    public Movie(String title, String overview, int releaseYear, Double rating, int voteCount,
                 int tmdbMovieId, String posterPath, String backdropPath) {
        this.title = title;
        this.overview = overview;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.voteCount = voteCount;
        this.tmdbMovieId = tmdbMovieId;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getReleaseYearAsStr() {
        return String.valueOf(releaseYear);
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Double getRating() {
        return rating;
    }

    public String getRatingAsFormattedStr(){
        return String.format("%.1f", rating);
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getVoteCountAsString() {
        return String.valueOf(voteCount);
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getTmdbMovieId() {
        return tmdbMovieId;
    }

    public String getTmdbMovieIdAsStr() {
        return String.valueOf(tmdbMovieId);
    }

    public void setTmdbMovieId(int tmdbMovieId) {
        this.tmdbMovieId = tmdbMovieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
}

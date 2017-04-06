package com.esoxjem.movieguide.listing;

import com.esoxjem.movieguide.Movie;

import java.util.List;

/**
 * @author arun
 */
interface MoviesListingView
{
    void showMovies(List<Movie> movies);
    void moreMovies(List<Movie> movies);
    void setMode(int mode);
    int getMode();
    void loadingStarted();
    void loadingFailed(String errorMessage);
    void onMovieClicked(Movie movie);
    public void setLastQuery(String newQuery);
    void toggleLoad();
}

package com.esoxjem.movieguide.listing;

/**
 * @author arun
 */
public interface MoviesListingPresenter
{
    void displayMovies(boolean group, int mode);

    void displayMovies(boolean group);

    void setMode(int mode);

    void appendMovies(int mode, int page, String query);

    void searchMovies(String query);

    void searchTv(String query);

    void setView(MoviesListingView view, boolean group);

    void destroy();
}

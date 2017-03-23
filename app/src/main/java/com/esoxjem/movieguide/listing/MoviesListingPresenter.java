package com.esoxjem.movieguide.listing;

/**
 * @author arun
 */
public interface MoviesListingPresenter
{
    void displayMovies(boolean group);

    void searchMovies(String querry);

    void searchTv(String querry);

    void setView(MoviesListingView view, boolean group);

    void destroy();
}

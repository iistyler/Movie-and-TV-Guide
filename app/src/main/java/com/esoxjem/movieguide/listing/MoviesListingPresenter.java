package com.esoxjem.movieguide.listing;

/**
 * @author arun
 */
public interface MoviesListingPresenter
{
    void displayMovies();

    void searchMovies(String querry);

    void searchTv(String querry);

    void setView(MoviesListingView view);

    void destroy();
}

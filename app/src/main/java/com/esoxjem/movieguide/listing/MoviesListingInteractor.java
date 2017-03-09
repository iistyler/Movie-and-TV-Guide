package com.esoxjem.movieguide.listing;

import com.esoxjem.movieguide.Movie;

import java.util.List;

import rx.Observable;

/**
 * @author arun
 */
public interface MoviesListingInteractor
{
    Observable<List<Movie>> fetchMovies();
    Observable<List<Movie>> searchMovies(String query);
    Observable<List<Movie>> searchTv(String query);
}

package com.esoxjem.movieguide.details;

import android.app.Activity;
import android.view.View;

import com.esoxjem.movieguide.Movie;

/**
 * @author arun
 */
public interface MovieDetailsPresenter
{
    void showDetails(Movie movie);

    void showTrailers(Movie movie);

    void showIMDB(Movie movie);

    void showReviews(Movie movie);

    void showFavoriteButton(Movie movie, int listId);

    void onFavoriteClick(Movie movie, int listId, Activity currentActivity, View currentView);

    void setView(MovieDetailsView view);

    void destroy();
}

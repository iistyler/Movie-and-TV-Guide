package com.esoxjem.movieguide.listing.sorting;

/**
 * @author arun
 */
public interface SortingDialogPresenter
{
    void setLastSavedOption();

    void onPopularMoviesSelected();

    void onHighestRatedMoviesSelected();

    void onSearchMoviesSelected();

    void onSearchTvSelected();

    void onPopularTvSelected();

    void onHighestRatedTvSelected();

    void setView(SortingDialogView view);

    void destroy();
}
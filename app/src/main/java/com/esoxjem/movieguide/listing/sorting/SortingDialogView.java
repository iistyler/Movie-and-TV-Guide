package com.esoxjem.movieguide.listing.sorting;

/**
 * @author arun
 */
interface SortingDialogView
{
    void setPopularMovieChecked();

    void setHighestRatedMovieChecked();

    void setPopularTvChecked();

    void setHighestRatedTvChecked();

    void setSearchMoviesChecked();

    void setSearchTvChecked();

    void dismissDialog();

}

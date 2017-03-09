package com.esoxjem.movieguide.listing.sorting;

/**
 * @author arun
 */
public enum SortType
{
    MOST_POPULAR_MOVIE(0), HIGHEST_RATED_MOVIE(1), MOST_POPULAR_TV(2), HIGHEST_RATED_TV(3), SEARCH_MOVIES(4), SEARCH_TV(5);

    private final int value;

    SortType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}

package com.esoxjem.movieguide.listing.sorting;

/**
 * @author arun
 */
class SortingDialogPresenterImpl implements SortingDialogPresenter
{
    private SortingDialogView view;
    private SortingDialogInteractor sortingDialogInteractor;

    SortingDialogPresenterImpl(SortingDialogInteractor interactor)
    {
        sortingDialogInteractor = interactor;
    }

    @Override
    public void setView(SortingDialogView view)
    {
        this.view = view;
    }

    @Override
    public void destroy()
    {
        view = null;
    }

    @Override
    public void setLastSavedOption()
    {
        if (isViewAttached())
        {
            int selectedOption = sortingDialogInteractor.getSelectedSortingOption();

            if (selectedOption == SortType.MOST_POPULAR_MOVIE.getValue()) {
                view.setPopularMovieChecked();
            } else if (selectedOption == SortType.HIGHEST_RATED_MOVIE.getValue()) {
                view.setHighestRatedMovieChecked();
            } else if (selectedOption == SortType.MOST_POPULAR_TV.getValue()) {
                view.setPopularTvChecked();
            } else if (selectedOption == SortType.SEARCH_MOVIES.getValue()) {
                view.setSearchMoviesChecked();
            } else if (selectedOption == SortType.SEARCH_TV.getValue()) {
                view.setSearchTvChecked();
            } else {
                view.setHighestRatedTvChecked();
            }
        }
    }

    private boolean isViewAttached()
    {
        return view != null;
    }

    @Override
    public void onPopularMoviesSelected()
    {
        if (isViewAttached())
        {
            sortingDialogInteractor.setSortingOption(SortType.MOST_POPULAR_MOVIE);
            view.dismissDialog();
        }
    }

    @Override
    public void onHighestRatedMoviesSelected()
    {
        if (isViewAttached())
        {
            sortingDialogInteractor.setSortingOption(SortType.HIGHEST_RATED_MOVIE);
            view.dismissDialog();
        }
    }

    @Override
    public void onSearchMoviesSelected()
    {
        if (isViewAttached())
        {
            sortingDialogInteractor.setSortingOption(SortType.SEARCH_MOVIES);
            view.dismissDialog();
        }
    }

    @Override
    public void onSearchTvSelected()
    {
        if (isViewAttached())
        {
            sortingDialogInteractor.setSortingOption(SortType.SEARCH_TV);
            view.dismissDialog();
        }
    }

    @Override
    public void onPopularTvSelected()
    {
        if (isViewAttached())
        {
            sortingDialogInteractor.setSortingOption(SortType.MOST_POPULAR_TV);
            view.dismissDialog();
        }
    }

    @Override
    public void onHighestRatedTvSelected()
    {
        if (isViewAttached())
        {
            sortingDialogInteractor.setSortingOption(SortType.HIGHEST_RATED_TV);
            view.dismissDialog();
        }
    }
}

package com.esoxjem.movieguide.listing;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.esoxjem.movieguide.BaseApplication;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.listing.sorting.SortType;
import com.esoxjem.movieguide.listing.sorting.SortingDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesListingFragment extends Fragment implements MoviesListingView {
    @Inject
    MoviesListingPresenter moviesPresenter;

    @Bind(R.id.movies_listing)
    RecyclerView moviesListing;

    private RecyclerView.Adapter adapter;
    private List<Movie> movies = new ArrayList<>();
    private Callback callback;
    private RecyclerView.LayoutManager layoutManager;

    private int loading;

    /*
    * Mode 0 = Most popular movies
    * Mode 1 = Highest rated movies
    * Mode 2 = Most popular TV
    * Mode 3 = Highest rated TV
    * Mode 4 = Search movies
    * Mode 5 = Search TV
    * Mode 6 = Custom List
    * */
    private int listMode;
    private String lastQuery;

    public int getMode() {
        return this.listMode;
    }

    public void setLastQuery(String newQuery) {
        this.lastQuery = newQuery;
    }

    public MoviesListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void toggleLoad() {
        if (this.loading == 1) {
            this.loading = 0;
        } else {
            this.loading = 1;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listMode = 0;
        this.lastQuery = "";
        this.loading = 0;
        setHasOptionsMenu(true);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createListingComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, rootView);
        initLayoutReferences();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesPresenter.setView(this, false);
        setMode(6);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                displaySortingOptions();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySortingOptions() {
        DialogFragment sortingDialogFragment = SortingDialogFragment.newInstance(moviesPresenter);
        sortingDialogFragment.show(getFragmentManager(), "Select Quantity");
    }

    private void initLayoutReferences() {
        moviesListing.setHasFixedSize(true);

        /* Number of columns. */
        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 4;
        } else {
            columns = getResources().getInteger(R.integer.no_of_columns);
        }
        this.layoutManager = new GridLayoutManager(getActivity(), columns);

        /* Scroll listener! */
        moviesListing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView movieListing, int dx, int dy) {
                super.onScrolled(movieListing, dx, dy);
                //int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                int lastVisibleItem = ((LinearLayoutManager)layoutManager).findLastCompletelyVisibleItemPosition();
                int lastVisibleItem2 = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                int nextPage = totalItemCount/20 + 1;

                // Issue with landscape orientation that causes the original last visible to be -1, this will be called if last is seen (even partially).
                if (lastVisibleItem == -1) {
                    lastVisibleItem = lastVisibleItem2;
                }

                if (lastVisibleItem == (totalItemCount - 1)) {
                    if (listMode <= 5) {
                        switch(listMode) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                if (loading == 0) {
                                    toggleLoad();
                                    moviesPresenter.appendMovies(listMode, nextPage, "");
                                    moviesListing.scrollToPosition(totalItemCount);
                                }
                                break;
                            case 4:
                                // We've hit the end; don't load more.
                                if (totalItemCount%20 != 0) {
                                    setMode(6);
                                }

                                // Still mode 4, load more. Will execute if the query count is a multiple of 20.
                                if (listMode != 6) {
                                    if (loading == 0) {
                                        toggleLoad();
                                        moviesPresenter.appendMovies(listMode, nextPage, lastQuery);
                                        moviesListing.scrollToPosition(totalItemCount);
                                    }
                                }
                                break;
                            case 5:
                                // We've hit the end; don't load more.
                                if (totalItemCount%20 != 0) {
                                    setMode(6);
                                }

                                // Still mode 5, load more. Will execute if the query count is a multiple of 20.
                                if (listMode != 6) {
                                    if (loading == 0) {
                                        toggleLoad();
                                        moviesPresenter.appendMovies(listMode, nextPage, lastQuery);
                                        moviesListing.scrollToPosition(totalItemCount);
                                    }
                                }
                                break;
                            case 6:
                            default:
                                // do nothing - custom list or maximum query.
                                break;
                        }
                    } else {
                        // no need to worry.
                    }
                }
            }
        });

        moviesListing.setLayoutManager(layoutManager);
        adapter = new MoviesListingAdapter(movies, this);
        moviesListing.setAdapter(adapter);
    }

    public void setMode(int mode) {
        if ((mode >= 0) && (mode <= 6)) {
            this.listMode = mode;
            moviesListing.scrollToPosition(0);
        }
    }

    @Override
    public void showMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        moviesListing.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        callback.onMoviesLoaded(movies.get(0));
    }

    @Override
    public void moreMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        moviesListing.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();;
    }

    @Override
    public void loadingStarted() {
        Snackbar.make(moviesListing, R.string.loading_movies, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String errorMessage) {
        if ((errorMessage.contains("Invalid index 0, size is 0")) || (errorMessage.contains("Index: 0, Size: 0"))) {
            Snackbar.make(moviesListing, "No results found", Snackbar.LENGTH_INDEFINITE).show();
        } else if (errorMessage.contains("Unable to resolve host")) {
            Snackbar.make(moviesListing, "No internet connection - Could not connect to the external database.", Snackbar.LENGTH_INDEFINITE).show();
        } else {
            Snackbar.make(moviesListing, errorMessage, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        callback.onMovieClicked(movie);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        moviesPresenter.destroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseListingComponent();
    }

    public interface Callback {
        void onMoviesLoaded(Movie movie);
        void onMovieClicked(Movie movie);
    }
}
package com.esoxjem.movieguide.listing;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

public class MoviesListingGroupFragment extends Fragment implements MoviesListingView {
    @Inject
    MoviesListingPresenter moviesPresenter;

    @Bind(R.id.movies_listing)
    RecyclerView moviesListing;

    private RecyclerView.Adapter adapter;
    private List<Movie> movies = new ArrayList<>();
    private List<Movie> newMovies = new ArrayList<>();
    private Callback callback;

    public MoviesListingGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createListingComponent().inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grouping, container, false);
        ButterKnife.bind(this, rootView);
        initLayoutReferences();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesPresenter.setView(this, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int mode) {
        // do nothing.
    }

    @Override
    public void toggleLoad() {
        // do nothing.
    }

    public int getMode() {
        // do nothing.
        return 0;
    }

    public void setLastQuery(String newQuery) {
        // do nothing.
    }

    private void initLayoutReferences() {
        moviesListing.setHasFixedSize(true);

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 4;
        } else {
            columns = getResources().getInteger(R.integer.no_of_columns);
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);

        moviesListing.setLayoutManager(layoutManager);
        adapter = new MoviesListingAdapter(movies, this);
        moviesListing.setAdapter(adapter);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        moviesListing.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        if(movies.size()>0){
            callback.onMoviesLoaded(movies.get(0));
        }
    }

    @Override
    public void moreMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        moviesListing.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        if(movies.size()>0){
            callback.onMoviesLoaded(movies.get(0));
        }
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

    public void changeMovieList(List<Movie> movies) {
        System.out.println("The movie size in function is: " + movies.size());
        showMovies(movies);
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
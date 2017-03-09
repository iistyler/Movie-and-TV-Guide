package com.esoxjem.movieguide.listing.sorting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.esoxjem.movieguide.BaseApplication;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.listing.MoviesListingPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author arun
 */
public class SortingDialogFragment extends DialogFragment implements SortingDialogView, RadioGroup.OnCheckedChangeListener
{
    @Inject
    SortingDialogPresenter sortingDialogPresenter;

    @Bind(R.id.most_popular_movies)
    RadioButton mostPopularMovie;
    @Bind(R.id.highest_rated_movies)
    RadioButton highestRatedMovie;
    @Bind(R.id.most_popular_tv)
    RadioButton mostPopularTv;
    @Bind(R.id.highest_rated_tv)
    RadioButton highestRatedTv;
    @Bind(R.id.search_movies)
    RadioButton searchMovies;
    @Bind(R.id.search_tv)
    RadioButton searchTv;
    @Bind(R.id.sorting_group)
    RadioGroup sortingOptionsGroup;

    private static MoviesListingPresenter moviesListingPresenter;

    public static SortingDialogFragment newInstance(MoviesListingPresenter moviesListingPresenter)
    {
        SortingDialogFragment.moviesListingPresenter = moviesListingPresenter;
        return new SortingDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).getListingComponent().inject(this);
        sortingDialogPresenter.setView(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.sorting_options, null);
        ButterKnife.bind(this, dialogView);
        initViews();

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(dialogView);
        dialog.setTitle(R.string.sort_by);
        dialog.show();
        return dialog;
    }

    private void initViews()
    {
        sortingDialogPresenter.setLastSavedOption();
        sortingOptionsGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void setPopularMovieChecked()
    {
        mostPopularMovie.setChecked(true);
    }

    @Override
    public void setHighestRatedMovieChecked()
    {
        highestRatedMovie.setChecked(true);
    }

    @Override
    public void setPopularTvChecked()
    {
        mostPopularTv.setChecked(true);
    }

    @Override
    public void setHighestRatedTvChecked()
    {
        highestRatedTv.setChecked(true);
    }

    @Override
    public void setSearchMoviesChecked()
    {
        searchMovies.setChecked(false);
    }

    @Override
    public void setSearchTvChecked()
    {
        searchTv.setChecked(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.most_popular_movies:
                sortingDialogPresenter.onPopularMoviesSelected();
                moviesListingPresenter.displayMovies();
                break;

            case R.id.highest_rated_movies:
                sortingDialogPresenter.onHighestRatedMoviesSelected();
                moviesListingPresenter.displayMovies();
                break;

            case R.id.most_popular_tv:
                sortingDialogPresenter.onPopularTvSelected();
                moviesListingPresenter.displayMovies();
                break;

            case R.id.highest_rated_tv:
                sortingDialogPresenter.onHighestRatedTvSelected();
                moviesListingPresenter.displayMovies();
                break;

            case R.id.search_movies:
                final EditText txtUrl = new EditText(getContext());
                txtUrl.setSingleLine(true);

                new AlertDialog.Builder(getContext())
                        .setTitle("Search Movies")
                        .setView(txtUrl)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                sortingDialogPresenter.onSearchMoviesSelected();
                                moviesListingPresenter.searchMovies(txtUrl.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                searchMovies.setChecked(false);
                            }
                        })
                        .show();

                break;

            case R.id.search_tv:
                final EditText txtUrl2 = new EditText(getContext());
                txtUrl2.setSingleLine(true);

                new AlertDialog.Builder(getContext())
                        .setTitle("Search TV")
                        .setView(txtUrl2)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                sortingDialogPresenter.onSearchTvSelected();
                                moviesListingPresenter.searchTv(txtUrl2.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                searchTv.setChecked(false);
                            }
                        })
                        .show();

                break;

        }
    }

    @Override
    public void dismissDialog()
    {
        dismiss();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        sortingDialogPresenter.destroy();
        ButterKnife.unbind(this);
    }
}

package com.esoxjem.movieguide.listing;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import com.esoxjem.movieguide.Api;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.listing.lists.ListInteractor;
import com.esoxjem.movieguide.network.RequestGenerator;
import com.esoxjem.movieguide.network.RequestHandler;
import com.esoxjem.movieguide.listing.sorting.SortType;
import com.esoxjem.movieguide.listing.sorting.SortingOptionStore;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import rx.Observable;
import rx.functions.Func0;

/**
 * @author arun
 */
class MoviesListingInteractorImpl implements MoviesListingInteractor
{
    private ListInteractor listInteractor;
    private RequestHandler requestHandler;
    private SortingOptionStore sortingOptionStore;

    MoviesListingInteractorImpl(ListInteractor listInteractor,
                                RequestHandler requestHandler, SortingOptionStore store)
    {
        this.listInteractor = listInteractor;
        this.requestHandler = requestHandler;
        sortingOptionStore = store;
    }

    @Override
    public Observable<List<Movie>> fetchMovies() {
        return Observable.defer(new Func0<Observable<List<Movie>>>() {
            @Override
            public Observable<List<Movie>> call() {
                try {
                    return Observable.just(get());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }

            private List<Movie> get() throws IOException, JSONException {
                int selectedOption = sortingOptionStore.getSelectedOption();
                if (selectedOption == SortType.MOST_POPULAR_MOVIE.getValue()) {
                    return fetch(Api.GET_POPULAR_MOVIES);
                } else if (selectedOption == SortType.HIGHEST_RATED_MOVIE.getValue()) {
                    return fetch(Api.GET_HIGHEST_RATED_MOVIES);
                } else if (selectedOption == SortType.MOST_POPULAR_TV.getValue()) {
                    return fetch(Api.GET_POPULAR_TV);
                } else if (selectedOption == SortType.HIGHEST_RATED_TV.getValue()) {
                    return fetch(Api.GET_HIGHEST_RATED_TV);
                } else {
                    return listInteractor.getMoviesOnList(0);
                }
            }

            @NonNull
            private List<Movie> fetch(String url) throws IOException, JSONException {

                Request request = RequestGenerator.get(url);
                String response = requestHandler.request(request);
                return MoviesListingParser.parse(response);
            }

        });
    }



    public Observable<List<Movie>> searchMovies(String query)
    {

        final String temp = Api.GET_SEARCH_MOVIES + query;

        return Observable.defer(new Func0<Observable<List<Movie>>>()
        {

            public Observable<List<Movie>> call()
            {
                try
                {
                    return Observable.just(get());
                } catch (Exception e)
                {
                    return Observable.error(e);
                }
            }

            private List<Movie> get() throws IOException, JSONException
            {

                return fetch(temp);

            }

            @NonNull
            private List<Movie> fetch(String url) throws IOException, JSONException {

                Request request = RequestGenerator.get(url);
                String response = requestHandler.request(request);
                return MoviesListingParser.parse(response);
            }

        });
    }


    public Observable<List<Movie>> searchTv(String query)
    {

        final String temp = Api.GET_SEARCH_TV + query;

        return Observable.defer(new Func0<Observable<List<Movie>>>()
        {

            public Observable<List<Movie>> call()
            {
                try
                {
                    return Observable.just(get());
                } catch (Exception e)
                {
                    return Observable.error(e);
                }
            }

            private List<Movie> get() throws IOException, JSONException
            {

                return fetch(temp);

            }

            @NonNull
            private List<Movie> fetch(String url) throws IOException, JSONException {

                Request request = RequestGenerator.get(url);
                String response = requestHandler.request(request);
                return MoviesListingParser.parse(response);
            }

        });
    }


}
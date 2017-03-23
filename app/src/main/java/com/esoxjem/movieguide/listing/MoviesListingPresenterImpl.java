package com.esoxjem.movieguide.listing;

import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.util.RxUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author arun
 */
class MoviesListingPresenterImpl implements MoviesListingPresenter
{
    private MoviesListingView view;
    private MoviesListingInteractor moviesInteractor;
    private Subscription fetchSubscription;

    MoviesListingPresenterImpl(MoviesListingInteractor interactor)
    {
        moviesInteractor = interactor;
    }

    @Override
    public void setView(MoviesListingView view, boolean group)
    {
        this.view = view;
        displayMovies(group);
    }

    @Override
    public void destroy()
    {
        view = null;
        RxUtils.unsubscribe(fetchSubscription);
    }

    @Override
    public void displayMovies(boolean group)
    {
        final boolean groupFlag = group;
        showLoading();
        fetchSubscription = moviesInteractor.fetchMovies().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        // Do nothing
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onMovieFetchFailed(e);
                    }

                    @Override
                    public void onNext(List<Movie> movies)
                    {
                        onMovieFetchSuccessGroup(movies, groupFlag);
                    }
                });
    }

    public void searchMovies(String query)
    {
        showLoading();
        fetchSubscription = moviesInteractor.searchMovies(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        // Do nothing
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onMovieFetchFailed(e);
                    }

                    @Override
                    public void onNext(List<Movie> movies)
                    {
                        onMovieFetchSuccess(movies);
                    }
                });
    }

    public void searchTv(String query)
    {
        showLoading();
        fetchSubscription = moviesInteractor.searchTv(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        // Do nothing
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onMovieFetchFailed(e);
                    }

                    @Override
                    public void onNext(List<Movie> movies)
                    {
                        onMovieFetchSuccess(movies);
                    }
                });
    }

    public void searchMovies(String query)
    {
        showLoading();
        fetchSubscription = moviesInteractor.searchMovies(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        // Do nothing
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onMovieFetchFailed(e);
                    }

                    @Override
                    public void onNext(List<Movie> movies)
                    {
                        onMovieFetchSuccess(movies);
                    }
                });
    }

    public void searchTv(String query)
    {
        showLoading();
        fetchSubscription = moviesInteractor.searchTv(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        // Do nothing
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onMovieFetchFailed(e);
                    }

                    @Override
                    public void onNext(List<Movie> movies)
                    {
                        onMovieFetchSuccess(movies);
                    }
                });
    }

    private void showLoading()
    {
        if (isViewAttached())
        {
            view.loadingStarted();
        }
    }

    private void onMovieFetchSuccess(List<Movie> movies)
    {
        if (isViewAttached())
        {
            view.showMovies(movies);
        }
    }

    private void onMovieFetchSuccessGroup(List<Movie> movies, boolean group)
    {
        if (isViewAttached())
        {
            if (group == false)
                view.showMovies(movies);
        }
    }

    private void onMovieFetchFailed(Throwable e)
    {
        view.loadingFailed(e.getMessage());
    }

    private boolean isViewAttached()
    {
        return view != null;
    }
}

package com.esoxjem.movieguide.details;

import android.util.Log;

import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.Review;
import com.esoxjem.movieguide.Video;
import com.esoxjem.movieguide.listing.lists.ListInteractor;
import com.esoxjem.movieguide.util.RxUtils;

import java.util.Hashtable;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author arun
 */
class MovieDetailsPresenterImpl implements MovieDetailsPresenter
{
    private MovieDetailsView view;
    private MovieDetailsInteractor movieDetailsInteractor;
    private ListInteractor listInteractor;
    private Subscription trailersSubscription;
    private Subscription reviewSubscription;

    MovieDetailsPresenterImpl(MovieDetailsInteractor movieDetailsInteractor, ListInteractor listInteractor)
    {
        this.movieDetailsInteractor = movieDetailsInteractor;
        this.listInteractor = listInteractor;
    }

    @Override
    public void setView(MovieDetailsView view)
    {
        this.view = view;
    }

    @Override
    public void destroy()
    {
        view = null;
        RxUtils.unsubscribe(trailersSubscription, reviewSubscription);
    }

    @Override
    public void showDetails(Movie movie)
    {
        if (isViewAttached())
        {
            view.showDetails(movie);
        }
    }

    private boolean isViewAttached()
    {
        return view != null;
    }

    @Override
    public void showTrailers(Movie movie)
    {
        trailersSubscription = movieDetailsInteractor.getTrailers(movie.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Video>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        // do nothing
                    }

                    @Override
                    public void onNext(List<Video> videos)
                    {
                        onGetTrailersSuccess(videos);
                    }
                });
    }

    private void onGetTrailersSuccess(List<Video> videos)
    {
        if (isViewAttached())
        {
            view.showTrailers(videos);
        }
    }

    public void showIMDB(Movie movie) {
        try {
            Hashtable<String,String> values = movieDetailsInteractor.getRatings(movie.getId(), movie.isMovie());
            view.showAdditionalInfo(values);
        } catch (Exception e) {

            // Log the failure to make request
            Log.e("showIMDB()", "Error: " + e.toString() + " trying to get ratings for movie ID " + movie.getId().toString());
        }
    }

    @Override
    public void showReviews(Movie movie)
    {
        reviewSubscription = movieDetailsInteractor.getReviews(movie.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Review>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        // do nothing
                    }

                    @Override
                    public void onNext(List<Review> reviews)
                    {
                        onGetReviewsSuccess(reviews);
                    }
                });
    }

    private void onGetReviewsSuccess(List<Review> reviews)
    {
        if (isViewAttached())
        {
            view.showReviews(reviews);
        }
    }

    @Override
    public void showFavoriteButton(Movie movie)
    {
        boolean isFavorite = listInteractor.isOnList(movie.getId(), 0);
        if (isViewAttached())
        {
            if (isFavorite)
            {
                view.showFavorited();
            } else
            {
                view.showUnFavorited();
            }
        }
    }

    @Override
    public void onFavoriteClick(Movie movie)
    {
        if (isViewAttached())
        {
            boolean isFavorite = listInteractor.isOnList(movie.getId(), 0);
            if (isFavorite)
            {
                listInteractor.removeFromList(movie.getId(), 0);
                view.showUnFavorited();
            } else
            {
                listInteractor.addToList(movie, 0);
                view.showFavorited();
            }
        }
    }
}

package com.esoxjem.movieguide.listing;

import com.esoxjem.movieguide.listing.favorites.ListInteractor;
import com.esoxjem.movieguide.network.RequestHandler;
import com.esoxjem.movieguide.listing.sorting.SortingOptionStore;

import dagger.Module;
import dagger.Provides;

/**
 * @author pulkitkumar
 * @author arunsasidharan
 */
@Module
public class ListingModule
{
    @Provides
    MoviesListingInteractor provideMovieListingInteractor(ListInteractor listInteractor,
                                                          RequestHandler requestHandler,
                                                          SortingOptionStore sortingOptionStore)
    {
        return new MoviesListingInteractorImpl(listInteractor, requestHandler, sortingOptionStore);
    }

    @Provides
    MoviesListingPresenter provideMovieListingPresenter(MoviesListingInteractor interactor)
    {
        return new MoviesListingPresenterImpl(interactor);
    }
}

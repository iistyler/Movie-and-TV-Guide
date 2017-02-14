package com.esoxjem.movieguide.details;

import com.esoxjem.movieguide.listing.favorites.ListInteractor;
import com.esoxjem.movieguide.network.RequestHandler;

import dagger.Module;
import dagger.Provides;

/**
 * @author pulkitkumar
 * @author arunsasidharan
 */
@Module
public class DetailsModule
{
    @Provides
    @DetailsScope
    MovieDetailsInteractor provideInteractor(RequestHandler requestHandler)
    {
        return new MovieDetailsInteractorImpl(requestHandler);
    }

    @Provides
    @DetailsScope
    MovieDetailsPresenter providePresenter(MovieDetailsInteractor detailsInteractor,
                                           ListInteractor listInteractor)
    {
        return new MovieDetailsPresenterImpl(detailsInteractor, listInteractor);
    }
}

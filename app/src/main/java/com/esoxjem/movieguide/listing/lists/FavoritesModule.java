package com.esoxjem.movieguide.listing.lists;

import com.esoxjem.movieguide.AppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author pulkitkumar
 */
@Module(includes = AppModule.class)
public class FavoritesModule
{
    @Provides
    @Singleton
    ListInteractor provideFavouritesInteractor()
    {
        return new ListInteractorImpl();
    }
}

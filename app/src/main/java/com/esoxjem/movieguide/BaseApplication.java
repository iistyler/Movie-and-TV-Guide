package com.esoxjem.movieguide;

import android.app.Application;
import android.os.StrictMode;

import com.esoxjem.movieguide.details.DetailsComponent;
import com.esoxjem.movieguide.details.DetailsModule;
import com.esoxjem.movieguide.listing.lists.DBClass;
import com.esoxjem.movieguide.listing.lists.FavoritesModule;
import com.esoxjem.movieguide.listing.ListingComponent;
import com.esoxjem.movieguide.listing.ListingModule;
import com.esoxjem.movieguide.listing.lists.GroupInteractorImpl;
import com.esoxjem.movieguide.listing.lists.ListInteractor;
import com.esoxjem.movieguide.listing.lists.ListInteractorImpl;
import com.esoxjem.movieguide.network.NetworkModule;

import java.util.List;

/**
 * @author arun
 */
public class BaseApplication extends Application
{
    private AppComponent appComponent;
    private DetailsComponent detailsComponent;
    private ListingComponent listingComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        StrictMode.enableDefaults();
        appComponent = createAppComponent();
        DBClass.createDB();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StrictMode.enableDefaults();
        appComponent = createAppComponent();
        DBClass.createDB();

        ListInteractor listInteractor = ListInteractorImpl.getInstance();
        listInteractor.setupListStores();
    }

    private AppComponent createAppComponent()
    {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .favoritesModule(new FavoritesModule())
                .build();
    }

    public DetailsComponent createDetailsComponent()
    {
        detailsComponent = appComponent.plus(new DetailsModule());
        return detailsComponent;
    }

    public void releaseDetailsComponent()
    {
        detailsComponent = null;
    }

    public ListingComponent createListingComponent()
    {
        listingComponent = appComponent.plus(new ListingModule());
        return listingComponent;
    }

    public void releaseListingComponent()
    {
        listingComponent = null;
    }

    public ListingComponent getListingComponent()
    {
        return listingComponent;
    }
}

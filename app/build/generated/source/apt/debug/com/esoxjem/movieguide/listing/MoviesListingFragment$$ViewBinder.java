// Generated code from Butter Knife. Do not modify!
package com.esoxjem.movieguide.listing;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MoviesListingFragment$$ViewBinder<T extends com.esoxjem.movieguide.listing.MoviesListingFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492994, "field 'moviesListing'");
    target.moviesListing = finder.castView(view, 2131492994, "field 'moviesListing'");
  }

  @Override public void unbind(T target) {
    target.moviesListing = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.esoxjem.movieguide.listing;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MoviesListingAdapter$ViewHolder$$ViewBinder<T extends com.esoxjem.movieguide.listing.MoviesListingAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492986, "field 'poster'");
    target.poster = finder.castView(view, 2131492986, "field 'poster'");
    view = finder.findRequiredView(source, 2131492996, "field 'titleBackground'");
    target.titleBackground = view;
    view = finder.findRequiredView(source, 2131492989, "field 'name'");
    target.name = finder.castView(view, 2131492989, "field 'name'");
  }

  @Override public void unbind(T target) {
    target.poster = null;
    target.titleBackground = null;
    target.name = null;
  }
}

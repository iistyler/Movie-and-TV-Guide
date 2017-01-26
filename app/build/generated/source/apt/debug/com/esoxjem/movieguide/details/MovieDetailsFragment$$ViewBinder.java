// Generated code from Butter Knife. Do not modify!
package com.esoxjem.movieguide.details;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MovieDetailsFragment$$ViewBinder<T extends com.esoxjem.movieguide.details.MovieDetailsFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492986, "field 'poster'");
    target.poster = finder.castView(view, 2131492986, "field 'poster'");
    view = finder.findRequiredView(source, 2131492985, "field 'collapsingToolbar'");
    target.collapsingToolbar = finder.castView(view, 2131492985, "field 'collapsingToolbar'");
    view = finder.findRequiredView(source, 2131492989, "field 'title'");
    target.title = finder.castView(view, 2131492989, "field 'title'");
    view = finder.findRequiredView(source, 2131492990, "field 'releaseDate'");
    target.releaseDate = finder.castView(view, 2131492990, "field 'releaseDate'");
    view = finder.findRequiredView(source, 2131492991, "field 'rating'");
    target.rating = finder.castView(view, 2131492991, "field 'rating'");
    view = finder.findRequiredView(source, 2131492992, "field 'overview'");
    target.overview = finder.castView(view, 2131492992, "field 'overview'");
    view = finder.findRequiredView(source, 2131493017, "field 'label'");
    target.label = finder.castView(view, 2131493017, "field 'label'");
    view = finder.findRequiredView(source, 2131493019, "field 'trailers'");
    target.trailers = finder.castView(view, 2131493019, "field 'trailers'");
    view = finder.findRequiredView(source, 2131493018, "field 'horizontalScrollView'");
    target.horizontalScrollView = finder.castView(view, 2131493018, "field 'horizontalScrollView'");
    view = finder.findRequiredView(source, 2131493020, "field 'reviews'");
    target.reviews = finder.castView(view, 2131493020, "field 'reviews'");
    view = finder.findRequiredView(source, 2131493021, "field 'reviewsContainer'");
    target.reviewsContainer = finder.castView(view, 2131493021, "field 'reviewsContainer'");
    view = finder.findRequiredView(source, 2131492993, "field 'favorite' and method 'onClick'");
    target.favorite = finder.castView(view, 2131492993, "field 'favorite'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492987, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131492987, "field 'toolbar'");
  }

  @Override public void unbind(T target) {
    target.poster = null;
    target.collapsingToolbar = null;
    target.title = null;
    target.releaseDate = null;
    target.rating = null;
    target.overview = null;
    target.label = null;
    target.trailers = null;
    target.horizontalScrollView = null;
    target.reviews = null;
    target.reviewsContainer = null;
    target.favorite = null;
    target.toolbar = null;
  }
}

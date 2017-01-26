// Generated code from Butter Knife. Do not modify!
package com.esoxjem.movieguide.listing.sorting;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SortingDialogFragment$$ViewBinder<T extends com.esoxjem.movieguide.listing.sorting.SortingDialogFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493014, "field 'mostPopular'");
    target.mostPopular = finder.castView(view, 2131493014, "field 'mostPopular'");
    view = finder.findRequiredView(source, 2131493015, "field 'highestRated'");
    target.highestRated = finder.castView(view, 2131493015, "field 'highestRated'");
    view = finder.findRequiredView(source, 2131493016, "field 'favorites'");
    target.favorites = finder.castView(view, 2131493016, "field 'favorites'");
    view = finder.findRequiredView(source, 2131493013, "field 'sortingOptionsGroup'");
    target.sortingOptionsGroup = finder.castView(view, 2131493013, "field 'sortingOptionsGroup'");
  }

  @Override public void unbind(T target) {
    target.mostPopular = null;
    target.highestRated = null;
    target.favorites = null;
    target.sortingOptionsGroup = null;
  }
}

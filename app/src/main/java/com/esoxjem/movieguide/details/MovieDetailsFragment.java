package com.esoxjem.movieguide.details;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esoxjem.movieguide.BaseApplication;
import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.Review;
import com.esoxjem.movieguide.Video;
import com.squareup.picasso.Picasso;
import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.RxMDTextView;
import com.yydcdut.rxmarkdown.RxMarkdown;
import com.yydcdut.rxmarkdown.callback.OnLinkClickCallback;
import com.yydcdut.rxmarkdown.factory.EditFactory;
import com.yydcdut.rxmarkdown.factory.TextFactory;
import com.yydcdut.rxmarkdown.loader.DefaultLoader;
import com.yydcdut.rxmarkdown.loader.RxMDImageLoader;

import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;

public class MovieDetailsFragment extends Fragment implements MovieDetailsView, View.OnClickListener
{
    @Inject
    MovieDetailsPresenter movieDetailsPresenter;

    @Bind(R.id.movie_poster)
    ImageView poster;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.movie_name)
    TextView title;
    @Bind(R.id.movie_year)
    TextView releaseDate;
    @Bind(R.id.movie_rating)
    TextView rating;
    @Bind(R.id.movie_description)
    TextView overview;
    @Bind(R.id.trailers_label)
    TextView label;
    @Bind(R.id.trailers)
    LinearLayout trailers;
    @Bind(R.id.trailers_container)
    HorizontalScrollView horizontalScrollView;
    @Bind(R.id.reviews_label)
    TextView reviews;
    @Bind(R.id.reviews)
    LinearLayout reviewsContainer;
    @Bind(R.id.favorite)
    FloatingActionButton favorite;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.movie_runtime)
    TextView runTime;

    int listId;

    private Movie movie;

    public MovieDetailsFragment()
    {
        // Required empty public constructor
    }

    public static MovieDetailsFragment getInstance(@NonNull Movie movie, int listId)
    {
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE, movie);
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        movieDetailsFragment.listId = listId;
        return movieDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createDetailsComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, rootView);
        setToolbar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null)
        {
            Movie movie = (Movie) getArguments().get(Constants.MOVIE);
            if (movie != null)
            {
                this.movie = movie;
                movieDetailsPresenter.setView(this);
                movieDetailsPresenter.showDetails(movie);
                movieDetailsPresenter.showFavoriteButton(movie, listId);
                movieDetailsPresenter.showIMDB(movie);
            }
        }
    }

    private void setToolbar()
    {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        collapsingToolbar.setTitle("Movie Details");
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null)
        {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    public void showAdditionalInfo(Hashtable<String,String> values) {
        if (values.size() > 2) {
            if (values.get("mc").contains("N/A")) {
                rating.setText(String.format("%s, & IMDB: %s/10", rating.getText().toString(), values.get("imdb")));
                if (values.get("totalSeasons") != null) {
                    runTime.setText(String.format("Number of Seasons: %s", values.get("totalSeasons")));
                }
            } else {
                rating.setText(String.format("%s, IMDB: %s/10, & Metascore: %s/100", rating.getText().toString(), values.get("imdb"), values.get("mc")));
                if (values.get("runtime") != null) {
                    runTime.setText(String.format("Runtime: %s minutes", values.get("runtime")));
                }
            }
        }
    }

    @Override
    public void showDetails(Movie movie)
    {
        Glide.with(getContext()).load(movie.getBackdropPath()).into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
        rating.setText(String.format("TMDB: %s/10", String.valueOf(movie.getVoteAverage())));
        overview.setText(movie.getOverview());
        if (movie.isMovie()) {
            collapsingToolbar.setTitle("Movie Details");
            movieDetailsPresenter.showTrailers(movie);
        } else {
            collapsingToolbar.setTitle("TV Show Details");
        }
        movieDetailsPresenter.showReviews(movie);
    }

    @Override
    public void showTrailers(List<Video> trailers)
    {
        if (trailers.isEmpty())
        {
            label.setVisibility(View.GONE);
            this.trailers.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);

        } else
        {
            label.setVisibility(View.VISIBLE);
            this.trailers.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.VISIBLE);

            this.trailers.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            Picasso picasso = Picasso.with(getContext());
            for (Video trailer : trailers)
            {
                View thumbContainer = inflater.inflate(R.layout.video, this.trailers, false);
                ImageView thumbView = ButterKnife.findById(thumbContainer, R.id.video_thumb);
                thumbView.setTag(Video.getUrl(trailer));
                thumbView.requestLayout();
                thumbView.setOnClickListener(this);
                picasso
                        .load(Video.getThumbnailUrl(trailer))
                        .resizeDimen(R.dimen.video_width, R.dimen.video_height)
                        .centerCrop()
                        .placeholder(R.color.colorPrimary)
                        .into(thumbView);
                this.trailers.addView(thumbContainer);
            }
        }
    }

    @Override
    public void showReviews(List<Review> reviews)
    {
        if (reviews.isEmpty())
        {
            this.reviews.setVisibility(View.GONE);
            reviewsContainer.setVisibility(View.GONE);
        } else
        {
            this.reviews.setVisibility(View.VISIBLE);
            reviewsContainer.setVisibility(View.VISIBLE);

            reviewsContainer.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();

            for (Review review : reviews)
            {

                Log.d("REVIEW", "Trying to add review " + review.getAuthor());
                final ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review, reviewsContainer, false);
                TextView reviewAuthor = ButterKnife.findById(reviewContainer, R.id.review_author);
                final RxMDTextView reviewContent = (RxMDTextView) ButterKnife.findById(reviewContainer, R.id.review_content);
                reviewAuthor.setText(review.getAuthor());

                String content = review.getContent();

                reviewContent.setOnClickListener(this);
                reviewsContainer.addView(reviewContainer);

                /* Want to display the reviews with the RxMarkdown library as the reviews have markdown syntax */
                /* Attach the reviewContent textview to the content */
                Log.d("REVIEW", "review = " + content);

                RxMarkdown.with(content, reviewContainer.getContext())
                        .config(null)
                        .factory(TextFactory.create())
                        .intoObservable()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<CharSequence>() {

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("REVIEW", "ERROR: " + e.toString());
                            }

                            @Override
                            public void onNext(CharSequence charSequence) {
                                Log.d("REVIEW", "onNext() called with: " + charSequence);
                                reviewContent.setText(charSequence, TextView.BufferType.SPANNABLE);
                            }
                        });

            }
        }
    }

    @Override
    public void showFavorited()
    {
        ColorStateList csl = new ColorStateList(new int[][]{{}}, new int[]{Color.parseColor("#7D0010")});
        favorite.setBackgroundTintList(csl);
        favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white_24dp));
    }

    @Override
    public void showUnFavorited()
    {
        ColorStateList csl = new ColorStateList(new int[][]{{}}, new int[]{Color.parseColor("#1F466D")});
        favorite.setBackgroundTintList(csl);
        favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_white_24dp));
    }

    @OnClick(R.id.favorite)
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.video_thumb:
                onThumbnailClick(view);
                break;

            case R.id.review_content:
                onReviewClick((RxMDTextView) view);
                break;

            case R.id.favorite:
                onFavoriteClick(view);
                break;

            default:
                Log.d("onClick()", "Unknown item was pressed with view.getId() == " + view.getId());
                break;
        }
    }

    private void onReviewClick(RxMDTextView view)
    {
        if (view.getMaxLines() == 5)
        {
            view.setMaxLines(500);
        } else
        {
            view.setMaxLines(5);
        }
    }

    private void onThumbnailClick(View view)
    {
        String videoUrl = (String) view.getTag();
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    private void onFavoriteClick(View view)
    {

        movieDetailsPresenter.onFavoriteClick(movie, this.listId, this.getActivity(), view);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        movieDetailsPresenter.destroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseDetailsComponent();
    }

}

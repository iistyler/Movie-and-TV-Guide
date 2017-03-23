package com.esoxjem.movieguide.details;

import com.esoxjem.movieguide.Review;
import com.esoxjem.movieguide.Video;

import org.json.JSONException;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import rx.Observable;

/**
 * @author arun
 */
public interface MovieDetailsInteractor
{
    Hashtable<String, String> getRatings(String id, Boolean tvMovie) throws IOException, JSONException;
    Observable<List<Video>> getTrailers(String id);
    Observable<List<Review>> getReviews(String id);
}

package com.esoxjem.movieguide.details;

import android.util.Log;

import com.esoxjem.movieguide.Api;
import com.esoxjem.movieguide.Movie;
import android.os.StrictMode;
import com.esoxjem.movieguide.Review;
import com.esoxjem.movieguide.Video;
import com.esoxjem.movieguide.network.RequestGenerator;
import com.esoxjem.movieguide.network.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Hashtable;
import java.util.List;

import okhttp3.Request;
import rx.Observable;
import rx.functions.Func0;

import static com.esoxjem.movieguide.listing.MoviesListingParser.getMovie;

/**
 * @author arun
 */
class MovieDetailsInteractorImpl implements MovieDetailsInteractor
{
    private RequestHandler requestHandler;

    private static final String IMDB_ID = "imdb_id";
    private static final String IMDB = "imdbRating";
    private static final String MC = "Metascore";
    private static final String RUNTIME = "runtime";
    private static final String SEASONS = "totalSeasons";

    MovieDetailsInteractorImpl(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public Hashtable<String, String> getRatings(String id, Boolean tvMovie) throws IOException, JSONException {
        Hashtable<String, String> values = new Hashtable<String, String>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url;
        Request request;
        JSONObject response;

        // Get movie details
        if (tvMovie == true) {
            url = String.format(Api.GET_MOVIE_DETAILS, id);
        } else {
            url = String.format(Api.GET_TV_IMDB, id);
        }
        request = RequestGenerator.get(url);
        response = new JSONObject(requestHandler.request(request));
        if (!response.isNull(IMDB_ID)) {
            values.put(IMDB_ID, response.getString(IMDB_ID));
        } else {
            return values;
        }
        if (!response.isNull(RUNTIME)) {
            values.put(RUNTIME, response.getString(RUNTIME));
        }

        // Get ratings here.
        url = String.format(Api.GET_OMDB_DETAILS, values.get(IMDB_ID));
        request = RequestGenerator.get(url);
        response = new JSONObject(requestHandler.request(request));
        if (!response.isNull(IMDB)) {
            values.put("imdb", response.getString(IMDB));
        }
        if (!response.isNull(MC)) {
            values.put("mc", response.getString(MC));
        }
        if (!response.isNull(SEASONS)) {
            values.put(SEASONS, response.getString(SEASONS));
        }

        return values;
    }

    @Override
    public Observable<List<Video>> getTrailers(final String id) {
        return Observable.defer(new Func0<Observable<List<Video>>>()
        {
            @Override
            public Observable<List<Video>> call()
            {
                try
                {
                    return Observable.just(get(id));
                } catch (Exception e)
                {
                    return Observable.error(e);
                }
            }

            private List<Video> get(String id) throws IOException, JSONException
            {
                String url = String.format(Api.GET_TRAILERS, id);
                Request request = RequestGenerator.get(url);
                String body = requestHandler.request(request);
                return MovieDetailsParser.parseTrailers(body);
            }
        });
    }

    @Override
    public Observable<List<Review>> getReviews(final String id)
    {
        return Observable.defer(new Func0<Observable<List<Review>>>()
        {
            @Override
            public Observable<List<Review>> call()
            {
                try
                {
                    return Observable.just(get(id));
                } catch (Exception e)
                {
                    return Observable.error(e);
                }
            }

            private List<Review> get(String id) throws IOException, JSONException
            {
                String url = String.format(Api.GET_REVIEWS, id);
                Request request = RequestGenerator.get(url);
                String body = requestHandler.request(request);
                return MovieDetailsParser.parseReviews(body);
            }
        });
    }
}

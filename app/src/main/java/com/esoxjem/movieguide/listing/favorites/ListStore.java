package com.esoxjem.movieguide.listing.favorites;

import android.content.Context;

import com.esoxjem.movieguide.Api;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.network.NetworkModule;
import com.esoxjem.movieguide.network.RequestGenerator;
import com.esoxjem.movieguide.network.RequestHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import javax.inject.Singleton;

import okhttp3.Request;

import static com.esoxjem.movieguide.listing.MoviesListingParser.getMovie;

/**
 * @author arun
 */
@Singleton
public class ListStore
{
    private RequestHandler requestHandler;
    private static final String PREF_NAME = "ListStore";

    @Inject
    public ListStore()
    {
        NetworkModule networkModule = new NetworkModule();
        requestHandler = networkModule.provideRequestHandler(networkModule.provideOkHttpClient());
    }

    public void setFavorite(Movie movie, int listID)
    {

        DBClass movieDB = DBClass.getInstance();

        if (isFavorite(movie.getId(), listID))
            return;

        // Add movie to favourite
        movieDB.query("INSERT INTO SavedMovies (movieId, listId) VALUES ('" + movie.getId() + "', '"
                + listID + "')");
    }

    public boolean isFavorite(String movieId, int listID)
    {
        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select Count(movieId) AS numMovies from "+
                "SavedMovies WHERE movieId = '" + movieId + "' AND listId = '" + listID + "'");

        if (Integer.parseInt( result.get(0).get("numMovies") ) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Movie> getFavorites(int listID) throws IOException
    {
        ArrayList<Movie> movies = new ArrayList<>(24);

        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select movieId from SavedMovies");

        for (Map<String, String> current : result) {
            movies.add( idToMovieObject( current.get("movieId") ));
        }

        return movies;
    }

    public void unfavorite(String movieId, int listID)
    {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("DELETE FROM SavedMovies WHERE movieId = '" + movieId +
                "' AND listId = '" + listID + "'");
    }

    private Movie idToMovieObject(String id) {

        String url = String.format(Api.GET_MOVIE_DETAILS, id.toString());
        Request request = RequestGenerator.get(url);
        Movie movieObject = new Movie();

        try {
            String body = requestHandler.request(request);
            System.out.println(body);
            JSONObject response = new JSONObject(body);
            movieObject = getMovie(response);
        } catch (Exception e) {
            // Throw an error
        }

        return movieObject;
    }
}

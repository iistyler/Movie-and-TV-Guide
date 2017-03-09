package com.esoxjem.movieguide.listing.lists;

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
	private int listID;
	private String listName;
		
    @Inject
    public ListStore( int listID, String listName )
    {
        NetworkModule networkModule = new NetworkModule();
        requestHandler = networkModule.provideRequestHandler(networkModule.provideOkHttpClient());
    	this.listID = listID;
        this.listName = listName;
	}

    public void setFavoriteById(int id, String type) {
        /*TODO: Add type to db*/

        // Add movie to favourite
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("INSERT INTO SavedMovies (movieId, listId) VALUES ('" + id + "', '"
                + listID + "')");
    }

    public void setFavorite(Movie movie)
    {

        DBClass movieDB = DBClass.getInstance();

        /* Ensure the movie isn't already a favourite */
        if (isFavorite(movie.getId()))
            return;

        // Add movie to favourite
        movieDB.query("INSERT INTO SavedMovies (movieId, listId) VALUES ('" + movie.getId() + "', '"
                + listID + "')");
    }

    public boolean isFavorite(String movieId)
    {
        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select Count(movieId) AS numMovies from "+
                "SavedMovies WHERE movieId = '" + movieId + "' AND listId = '" + this.getID() + "'");

        if (Integer.parseInt( result.get(0).get("numMovies") ) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Movie> getFavorites() throws IOException
    {
        ArrayList<Movie> movies = new ArrayList<>(24);

        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select movieId from SavedMovies WHERE " +
                "listId = " + this.getID() + " ORDER BY movieId DESC");

        for (Map<String, String> current : result) {
            movies.add( idToMovieObject( current.get("movieId"), 0 ));
        }

        return movies;
    }

    public void unfavorite(String movieId)
    {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("DELETE FROM SavedMovies WHERE movieId = '" + movieId +
                "' AND listId = '" + this.getID() + "'");
    }


	/* Return the listStore's ID */
	public int getID() {
		return listID;
	}

	/* Return the name of the list */
	public String getListName() {
		return listName;
	}	

	/* Update the List's name, locally. Database is changed by ListInteractor */
	public void setListName(String name) {
        this.listName = name;
	}

    private Movie idToMovieObject(String id, int numTries) {

        String url = String.format(Api.GET_MOVIE_DETAILS, id.toString());
        Request request = RequestGenerator.get(url);
        Movie movieObject = new Movie();

        try {
            String body = requestHandler.request(request);
            System.out.println(body);
            JSONObject response = new JSONObject(body);
            movieObject = getMovie(response);
            if (movieObject.getId() == null) {
                //TODO: Throw an error
                System.out.println("Not existent");
            }

        } catch (Exception e) {
            // Throw an error
            if (numTries < 10) {
                numTries++;
                movieObject = idToMovieObject(id, numTries);
            } else {
                //TODO: Throw an error
                System.out.println("Could not fetch movie");
            }
        }

        // Verify movie object is correct
        if ( !movieObject.getId().equals(id) ) {
            if (numTries < 10) {
                numTries++;
                movieObject = idToMovieObject(id, numTries);
            } else {
                //TODO: Throw an error
                System.out.println("Could not fetch movie");
            }
        }

        return movieObject;
    }

}

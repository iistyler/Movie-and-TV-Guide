package com.esoxjem.movieguide.listing.lists;

import android.util.Log;

import com.esoxjem.movieguide.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author arun
 */
public class ListInteractorImpl implements ListInteractor
{
    private static ListInteractorImpl listInteractor = new ListInteractorImpl();
	private List<ListStore> allLists;
    private int listCount;
		
    public static ListInteractorImpl getInstance() {
        return listInteractor;
    }

    ListInteractorImpl()
    {
    	listCount = 0;
		allLists = new ArrayList();
	}

    public void setupListStores() {
        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select listId, listName from lists");

        for (Map<String, String> current : result) {
            ListStore newListStore = new ListStore(Integer.parseInt( current.get("listId") ),
                    current.get("listName"));
            allLists.add(newListStore);
        }
    }

    public int getListCount() {
        return listCount;
    }

    /* This function is used to reset the List Interactor between tests. This
    * should never be called for any other reason
    */
    public void resetListInteractor() {
        listCount = 0;
        allLists = new ArrayList();
        setupListStores();
    }

    @Override
    public void addToList(Movie movie, int listId)
    {
        ListStore temp = getListStoreByID(listId);
        if (temp != null) {
            temp.setFavorite(movie);
        }
	}

    @Override
    public void addToListById(int id, int type, int listId)
    {
        ListStore temp = getListStoreByID(listId);
        temp.setFavoriteById(id, type);
    }

    @Override
    public boolean isOnList(String id, int listId)
    {
        ListStore temp = getListStoreByID(listId);
        if (temp != null) {
            return temp.isFavorite(id);
        }

        return false;
    }

    public List<Movie> getMovieExportOnList(int listId) {
        ListStore listStore = getListStoreByID(listId);

        /* Return empty list if the listid can't be found */
        if (listStore == null) {
            /* Log the error and return an empty list */
            Log.e("getMoviesOnList()", "Could not get the ListStore with ID " + String.valueOf(listId));
            return new ArrayList<>(0);
        }

        try
        {
            return listStore.getFavoritesExport();
        } catch (IOException ignored)
        {
            Log.e("getMoviesOnList()", "Tried to access NULL ListStore with ID " + String.valueOf(listId));
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<Movie> getMoviesOnList(int listId)
    {
        ListStore listStore = getListStoreByID(listId);

        /* Return empty list if the listid can't be found */
        if (listStore == null) {
            /* Log the error and return an empty list */
            Log.e("getMoviesOnList()", "Could not get the ListStore with ID " + String.valueOf(listId));
            return new ArrayList<>(0);
        }

        try
        {
            return listStore.getFavorites();
        } catch (IOException ignored)
        {
            Log.e("getMoviesOnList()", "Tried to access NULL ListStore with ID " + String.valueOf(listId));
            return new ArrayList<>(0);
        }
    }

    @Override
    public void removeFromList(String id, int listId)
    {
        ListStore temp = getListStoreByID(listId);

        if (temp != null) {
            temp.unfavorite(id);
        }
    }


    @Override
    public Map<Integer, String> getLists(int groupId) {
        Map<Integer, String> lists = new HashMap<>();

        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select listId, listName from " +
                "lists WHERE groupId = " + groupId + " ORDER BY listId ASC");

        for (Map<String, String> current : result) {
            lists.put( Integer.parseInt( current.get("listId") ), current.get("listName") );
        }

        /* Do we need error checking here? */
        return lists;
    }

    @Override
    public void changeListName(int listId, String newName) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("UPDATE lists SET listName = '" + newName + "' WHERE listId = '" + listId + "'");

        ListStore temp = getListStoreByID(listId);

        if (temp != null) {
            temp.setListName(newName);
        }
    }

    @Override
    public int createList(String listName, int groupId) {

        // Add to the database
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("INSERT INTO lists (listName, groupId) VALUES ('" + listName + "', " + groupId + ")");

        // Create ListStore object
        int id = DBClass.getLastInsertID();
        ListStore newListStore = new ListStore(id, listName);
        allLists.add(newListStore);
        listCount++;

        return id;
    }

    @Override
    public void removeList(int listId) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("DELETE FROM lists WHERE listId = " + listId);

        // Remove the liststore object, note to avoid ConcurrentModificationException
        // we can't use the enhanced for loop, have to manually make the iterator
        for (Iterator<ListStore> iter = allLists.iterator(); iter.hasNext(); ) {
            ListStore temp = iter.next();
            if (temp.getID() == listId) {
                iter.remove();
            }
        }

        // Decrement the counter
        listCount--;
    }

    @Override
    public JSONArray exportLists(int groupId) throws JSONException {
        JSONArray listOfLists = new JSONArray();
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        /*TODO: Get movies*/
        Map<Integer, String> lists = listInteractor.getLists(groupId);
        Set<Integer> listIdsSet = lists.keySet();
        List<Integer> listIds = new ArrayList<Integer>(listIdsSet);
        Collections.sort(listIds);


        for (Integer currentList : listIds) {
            JSONObject json = new JSONObject();
            json.put("name", lists.get(currentList) );
            json.put("movies", listInteractor.exportMovies( currentList ));
            listOfLists.put(json);
        }

        return listOfLists;
    }

    public JSONArray exportMovies(int listId) throws JSONException {
        JSONArray listOfMovies = new JSONArray();
        List<Movie> movies = getMovieExportOnList(listId);

        for (Movie currentMovie : movies) {
            if (currentMovie.getId() == null)
                continue;
            JSONObject json = new JSONObject();
            json.put("id", currentMovie.getId() );
            json.put("type", currentMovie.getTvMovie() );
            listOfMovies.put(json);
        }

        return listOfMovies;
    }

    /* TODO: Handle the case where there is no ListStore with the given ID*/
    private ListStore getListStoreByID(int listID) {
        for (ListStore temp: allLists) {
            if (temp.getID() == listID) {
                return temp;
            }
        }
        return null;
    }
}

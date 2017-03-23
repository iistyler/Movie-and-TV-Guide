package com.esoxjem.movieguide.listing.lists;

import com.esoxjem.movieguide.Movie;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * @author arun
 */
public interface ListInteractor
{
    // Movie Fav
    void addToList(Movie movie, int listId);
    boolean isOnList(String id, int listId);
    List<Movie> getMoviesOnList(int listId);
    void removeFromList(String id, int listId);

    // List Editing
    Map<Integer, String> getLists(int groupId);
    void changeListName(int id, String newName);
    int createList(String listName, int groupId);
    void removeList(int id);
    JSONArray exportLists(int groupId) throws JSONException;
    void addToListById(int id, int type, int listId);
    void setupListStores();
}

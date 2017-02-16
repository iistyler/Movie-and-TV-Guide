package com.esoxjem.movieguide.listing.favorites;

import com.esoxjem.movieguide.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author arun
 */
public class ListInteractorImpl implements ListInteractor
{
    private static ListStore listStore = new ListStore();
    private static ListInteractorImpl favoritesInteractor = new ListInteractorImpl(listStore);

    public static ListInteractorImpl getInstance() {
        return favoritesInteractor;
    }

    ListInteractorImpl(ListStore store)
    {
        listStore = store;
    }

    @Override
    public void addToList(Movie movie, int listId)
    {
        listStore.setFavorite(movie, listId);
    }

    @Override
    public boolean isOnList(String id, int listId)
    {
        return listStore.isFavorite(id, listId);
    }

    @Override
    public List<Movie> getMoviesOnList(int listId)
    {
        try
        {
            return listStore.getFavorites(listId);
        } catch (IOException ignored)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public void removeFromList(String id, int listId)
    {
        listStore.unfavorite(id, listId);
    }


    @Override
    public Map<Integer, String> getLists(int groupId) {
        Map<Integer, String> lists = new HashMap<>();

        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select listId, listName from " +
                "lists WHERE groupId = " + groupId);

        for (Map<String, String> current : result) {
            lists.put( Integer.parseInt( current.get("listId") ), current.get("listName") );
        }

        return lists;
    }

    @Override
    public void changeListName(int listId, String newName) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("UPDATE lists SET listName = '" + newName + "' WHERE listId = '" + listId + "'");
    }

    @Override
    public void createList(String listName, int groupId) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("INSERT INTO lists (listName, groupId) VALUES ('" + listName + "', " + groupId + ")");
    }

    @Override
    public void removeList(int listId) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("DELETE FROM lists WHERE listId = " + listId);
    }
}

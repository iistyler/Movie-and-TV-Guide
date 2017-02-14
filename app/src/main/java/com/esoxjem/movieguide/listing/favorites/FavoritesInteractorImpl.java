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
public class FavoritesInteractorImpl implements FavoritesInteractor
{
    private static FavoritesStore favoritesStore = new FavoritesStore();
    private static FavoritesInteractorImpl favoritesInteractor = new FavoritesInteractorImpl(favoritesStore);

    public static FavoritesInteractorImpl getInstance() {
        return favoritesInteractor;
    }

    FavoritesInteractorImpl(FavoritesStore store)
    {
        favoritesStore = store;
    }

    @Override
    public void setFavorite(Movie movie)
    {
        favoritesStore.setFavorite(movie, 0);
    }

    @Override
    public boolean isFavorite(String id)
    {
        return favoritesStore.isFavorite(id, 0);
    }

    @Override
    public List<Movie> getFavorites()
    {
        try
        {
            return favoritesStore.getFavorites(0);
        } catch (IOException ignored)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public void unFavorite(String id)
    {
        favoritesStore.unfavorite(id, 0);
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

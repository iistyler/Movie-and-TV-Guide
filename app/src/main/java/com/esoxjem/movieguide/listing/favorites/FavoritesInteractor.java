package com.esoxjem.movieguide.listing.favorites;

import com.esoxjem.movieguide.Movie;

import java.util.List;
import java.util.Map;

/**
 * @author arun
 */
public interface FavoritesInteractor
{
    // Movie Fav
    void setFavorite(Movie movie);
    boolean isFavorite(String id);
    List<Movie> getFavorites();
    void unFavorite(String id);

    // List Editing
    Map<Integer, String> getLists(int groupId);
    void changeListName(int id, String newName);
    void createList(String listName, int groupId);
    void removeList(int id);

}

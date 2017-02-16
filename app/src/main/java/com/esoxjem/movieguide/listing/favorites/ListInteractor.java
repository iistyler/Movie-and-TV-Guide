package com.esoxjem.movieguide.listing.favorites;

import com.esoxjem.movieguide.Movie;

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
    void createList(String listName, int groupId);
    void removeList(int id);

}

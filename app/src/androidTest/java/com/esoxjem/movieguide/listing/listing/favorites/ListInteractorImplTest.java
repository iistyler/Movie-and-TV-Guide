package com.esoxjem.movieguide.listing.listing.favorites;

import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.listing.favorites.DBClass;
import com.esoxjem.movieguide.listing.favorites.ListInteractorImpl;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ListInteractorImplTest {

    @Test
    public void setFavoriteTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        // Create a movie object with the ID of Interstellar
        Movie movie = new Movie();
        movie.setId("157336");
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        // Add to favourites
        listInteractor.addToList(movie, 0);

        // Check if it was favourited
        List<Movie> newFavorites = listInteractor.getMoviesOnList(0);
        assertEquals("157336", newFavorites.get(0).getId());
    }

    @Test
    public void unfavouriteTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        // Create a movie object with the ID of Interstellar
        Movie movie = new Movie();
        movie.setId("157336");
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        // Add to favourites
        listInteractor.addToList(movie, 0);
        List<Movie> newFavorites = listInteractor.getMoviesOnList(0);

        // Check it was added
        assertEquals(newFavorites.size(), 1);

        // Remove from favourites
        listInteractor.removeFromList("157336", 0);

        // Check if it was un-favorited
        newFavorites = listInteractor.getMoviesOnList(0);
        assertEquals(newFavorites.size(), 0);
    }

    @Test
    public void isFavouriteTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        // Make sure its not currently a favorite
        Boolean isFav = listInteractor.isOnList("157336", 0);
        assertFalse(isFav);

        // Create a movie object with the ID of Interstellar
        Movie movie = new Movie();
        movie.setId("157336");
        listInteractor.addToList(movie, 0);

        // Make sure its not currently a favorite
        isFav = listInteractor.isOnList("157336", 0);
        assertTrue(isFav);
    }

    @Test
    public void createListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        // Create list
        listInteractor.createList("Group4", 2);
        Map<Integer, String> lists = listInteractor.getLists(2);

        // Check to see if list exists
        Integer firstKey = lists.keySet().iterator().next();
        assertEquals(lists.get(firstKey), "Group4");
    }

    @Test
    public void removeListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        // Create some lists
        listInteractor.createList("List1", 1);
        listInteractor.createList("List2", 1);
        listInteractor.createList("List3", 1);
        Map<Integer, String> lists = listInteractor.getLists(1);

        // Remove group
        for (Map.Entry<Integer, String> current : lists.entrySet()) {
            if (current.getValue().equals("List1") || current.getValue().equals("List3") )
                listInteractor.removeList(current.getKey());
        }

        // Check list was updated
        lists = listInteractor.getLists(1);
        assertEquals("List2", lists.values().toArray()[0]);
    }

    @Test
    public void updateListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        // Create first group
        listInteractor.createList("List4", 2);
        Map<Integer, String> groups = listInteractor.getLists(2);

        // Update group
        Integer firstKey = groups.keySet().iterator().next();
        listInteractor.changeListName(firstKey, "List7");


        // Check group was updated
        groups = listInteractor.getLists(2);
        assertEquals(groups.values().toArray()[0], "List7");
    }
}


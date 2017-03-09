package com.esoxjem.movieguide.listing.listing.favorites;

import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.listing.lists.DBClass;
import com.esoxjem.movieguide.listing.lists.ListInteractorImpl;

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

        // Get the list interactor and setup for test
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create the list
        int id = listInteractor.createList("Test", 0);

        // Add to favourites
        listInteractor.addToList(movie, id);

        // Verify it added properly
        assertEquals(1, listInteractor.getListCount());

        // Check if it was favourited
        List<Movie> newFavorites = listInteractor.getMoviesOnList(id);
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

        // Get list interactor and setup for test
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create List
        int id = listInteractor.createList("Test", 0);

        // Add to favourites
        listInteractor.addToList(movie, id);
        List<Movie> newFavorites = listInteractor.getMoviesOnList(id);

        // Check it was added
        assertEquals(newFavorites.size(), 1);

        // Remove from favourites
        listInteractor.removeFromList("157336", id);

        // Check if it was un-favorited
        newFavorites = listInteractor.getMoviesOnList(id);
        assertEquals(newFavorites.size(), 0);

    }

    @Test
    public void isFavouriteTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        // Get list interactor and setup for tests
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Make sure its not currently a favorite
        Boolean isFav = listInteractor.isOnList("157336", 0);
        assertFalse(isFav);

        // Create a movie object with the ID of Interstellar
        Movie movie = new Movie();
        movie.setId("157336");

        // Create the list
        int id = listInteractor.createList("Test", 0);

        // Add to list
        listInteractor.addToList(movie, id);

        // Make sure its not currently a favorite
        isFav = listInteractor.isOnList("157336", id);
        assertTrue(isFav);

        // Remove list
        listInteractor.removeList(0);
    }

    @Test
    public void createListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create list
        int id = listInteractor.createList("Group4", 2);
        Map<Integer, String> lists = listInteractor.getLists(2);

        // Check to see if list exists
        Integer firstKey = lists.keySet().iterator().next();
        assertEquals(lists.get(firstKey), "Group4");

        // Remove list
        listInteractor.removeList(0);
    }

    @Test
    public void removeListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        // Get list interactor
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create some lists in same group
        int list1 = listInteractor.createList("List1", 1);
        int list2 = listInteractor.createList("List2", 1);
        int list3 = listInteractor.createList("List3", 1);
        Map<Integer, String> lists = listInteractor.getLists(1);

        // Assert list is now proper size
        assertEquals(3, listInteractor.getListCount());

        // Remove item 1 & 3
        for (Map.Entry<Integer, String> current : lists.entrySet()) {
            int key = current.getKey();
            if (key == list1 || key == list3)
                listInteractor.removeList(key);
        }

        // Check list was updated
        lists = listInteractor.getLists(1);
        assertEquals("List2", lists.values().toArray()[0]);
        assertEquals(1, listInteractor.getListCount());

        // Remove the last list
        listInteractor.removeList(list2);

        // Internal variable is the right size
        assertEquals(0, listInteractor.getListCount());

        // Verify the list was removed from database
        List<Movie> removedFav = listInteractor.getMoviesOnList(0);
        assertEquals(0, removedFav.size());
    }

    @Test
    public void updateListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create first group
        listInteractor.createList("List4", 2);
        Map<Integer, String> groups = listInteractor.getLists(2);

        // Update group
        Integer firstKey = groups.keySet().iterator().next();
        listInteractor.changeListName(firstKey, "List7");


        // Check group was updated
        groups = listInteractor.getLists(2);
        assertEquals(groups.values().toArray()[0], "List7");

        listInteractor.removeList(0);
    }
}


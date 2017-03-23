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
import static junit.framework.Assert.fail;

public class ListInteractorImplTest {

    @Test
    public void setFavoriteTest() {
        DBClass.createDB();
        DBClass.resetDB();

        // Get the list interactor and setup for test
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create the list
        int id = listInteractor.createList("Test", 0);

        // Add to favourites
        listInteractor.addToListById(157336, 1, id);

        // Verify it added properly
        assertEquals(1, listInteractor.getListCount());

        // Check if it was favourited
        List<Movie> newFavorites = listInteractor.getMoviesOnList(id);
        assertEquals("157336", newFavorites.get(0).getId());

    }

    @Test
    public void unfavouriteTest() {
        DBClass.createDB();
        DBClass.resetDB();

        // Get list interactor and setup for test
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create List
        int id = listInteractor.createList("Test", 0);

        // Add to favourites
        listInteractor.addToListById(157336, 1, id);
        List<Movie> newFavorites = listInteractor.getMoviesOnList(id);

        // Check it was added
        assertEquals(newFavorites.size(), 1);

        // Remove from favourites
        listInteractor.removeFromList("157336", id);

        // Check if it was un-favorited
        newFavorites = listInteractor.getMoviesOnList(id);
        assertEquals(newFavorites.size(), 0);

        DBClass.resetDB();
        DBClass.setupInitialDB();
    }

    @Test
    public void isFavouriteTest() {
        DBClass.createDB();
        DBClass.resetDB();

        // Get list interactor and setup for tests
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Make sure its not currently a favorite
        Boolean isFav = listInteractor.isOnList("157336", 0);
        assertFalse(isFav);

        // Create the list
        int id = listInteractor.createList("Test", 0);

        // Add to list
        listInteractor.addToListById(157336, 1, id);

        // Make sure its not currently a favorite
        isFav = listInteractor.isOnList("157336", id);
        assertTrue(isFav);

        // Remove list
        listInteractor.removeList(0);

        DBClass.resetDB();
        DBClass.setupInitialDB();
    }

    @Test
    public void createListTest() {
        DBClass.createDB();
        DBClass.resetDB();

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

        DBClass.resetDB();
        DBClass.setupInitialDB();
    }

    @Test
    public void removeListTest() {
        DBClass.createDB();
        DBClass.resetDB();

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

        // Reset Database
        DBClass.resetDB();
        DBClass.setupInitialDB();
    }

    @Test
    public void updateListTest() {
        DBClass.createDB();
        DBClass.resetDB();

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

        DBClass.resetDB();
        DBClass.setupInitialDB();
    }

    @Test
    public void wrongListIDTest() {

        /* Want to make sure the following functions handle getting a ListID
        * that does not exist:
        * -> isOnList()
        * -> getMoviesOnList()
        * -> removeFromList()
        * -> changeListName()
        * -> removeLists()
        * */

        DBClass.createDB();
        DBClass.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create a list
        int listid = listInteractor.createList("List1", 2);


        // Ensure isOnList() handles incorrect IDs
        assertEquals(false, listInteractor.isOnList("nothing", -1));

        // Ensure we get an empty list from getMoviesOnList()
        List<Movie> movieList = listInteractor.getMoviesOnList(-1);
        assertEquals(0, movieList.size());

        // Ensure removeFromList() doesn't break on invalid ID
        listInteractor.removeFromList("ID", -1);

        // Ensure changeListName() doesn't break on invalid ID
        listInteractor.changeListName(-1, "test");

        // Ensure removeLists() handles invalid IDs
        listInteractor.removeList(-1);

    }


    @Test
    public void TVShowTest() {
        DBClass.createDB();
        DBClass.resetDB();

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        listInteractor.resetListInteractor();

        // Create a sample list and add a TV show to it
        int l1 = listInteractor.createList("List1", 1);
        listInteractor.addToListById(1402, 0, l1);

        // Check if correct TV show was fetched (and was not a movie)
        List<Movie> movieList = listInteractor.getMoviesOnList(l1);
        if (!movieList.get(0).getTitle().equals("The Walking Dead")) {
            fail("TV Show could not be added to list");
        }

        DBClass.resetDB();
        DBClass.setupInitialDB();
    }

    @Test
    public void checkTVShow() {
        // Test movie
        Movie movie = new Movie();
        movie.setTvMovie(1);

        if (movie.getTvMovie() != 1)
            fail("1 Should be movie");

        // Test TV Show
        Movie tvShow = new Movie();
        movie.setTvMovie(0);

        if (tvShow.getTvMovie() != 0)
            fail("0 Should be TV Show");
    }
}


package com.esoxjem.movieguide.listing.listing.favorites;

import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.listing.favorites.DBClass;
import com.esoxjem.movieguide.listing.favorites.FavoritesInteractorImpl;
import com.esoxjem.movieguide.listing.favorites.GroupInteractorImpl;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class favouritesInteractorImplTest {

    @Test
    public void setFavoriteTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        // Create a movie object with the ID of Interstellar
        Movie movie = new Movie();
        movie.setId("157336");
        FavoritesInteractorImpl favoritesInteractor = FavoritesInteractorImpl.getInstance();

        // Add to favourites
        favoritesInteractor.setFavorite(movie);

        // Check if it was favourited
        List<Movie> newFavorites = favoritesInteractor.getFavorites();
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
        FavoritesInteractorImpl favoritesInteractor = FavoritesInteractorImpl.getInstance();

        // Add to favourites
        favoritesInteractor.setFavorite(movie);
        List<Movie> newFavorites = favoritesInteractor.getFavorites();

        // Check it was added
        assertEquals(newFavorites.size(), 1);

        // Remove from favourites
        favoritesInteractor.unFavorite("157336");

        // Check if it was un-favorited
        newFavorites = favoritesInteractor.getFavorites();
        assertEquals(newFavorites.size(), 0);
    }

    @Test
    public void isFavouriteTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        FavoritesInteractorImpl favoritesInteractor = FavoritesInteractorImpl.getInstance();

        // Make sure its not currently a favorite
        Boolean isFav = favoritesInteractor.isFavorite("157336");
        assertFalse(isFav);

        // Create a movie object with the ID of Interstellar
        Movie movie = new Movie();
        movie.setId("157336");
        favoritesInteractor.setFavorite(movie);

        // Make sure its not currently a favorite
        isFav = favoritesInteractor.isFavorite("157336");
        assertTrue(isFav);
    }

    @Test
    public void createListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        FavoritesInteractorImpl favoritesInteractor = FavoritesInteractorImpl.getInstance();

        // Create list
        favoritesInteractor.createList("Group4", 2);
        Map<Integer, String> lists = favoritesInteractor.getLists(2);

        // Check to see if list exists
        Integer firstKey = lists.keySet().iterator().next();
        assertEquals(lists.get(firstKey), "Group4");
    }

    @Test
    public void removeListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        FavoritesInteractorImpl favoritesInteractor = FavoritesInteractorImpl.getInstance();

        // Create some lists
        favoritesInteractor.createList("List1", 1);
        favoritesInteractor.createList("List2", 1);
        favoritesInteractor.createList("List3", 1);
        Map<Integer, String> lists = favoritesInteractor.getLists(1);

        // Remove group
        for (Map.Entry<Integer, String> current : lists.entrySet()) {
            if (current.getValue().equals("List1") || current.getValue().equals("List3") )
                favoritesInteractor.removeList(current.getKey());
        }

        // Check list was updated
        lists = favoritesInteractor.getLists(1);
        assertEquals("List2", lists.values().toArray()[0]);
    }

    @Test
    public void updateListTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        FavoritesInteractorImpl favoritesInteractor = FavoritesInteractorImpl.getInstance();

        // Create first group
        favoritesInteractor.createList("List4", 2);
        Map<Integer, String> groups = favoritesInteractor.getLists(2);

        // Update group
        Integer firstKey = groups.keySet().iterator().next();
        favoritesInteractor.changeListName(firstKey, "List7");


        // Check group was updated
        groups = favoritesInteractor.getLists(2);
        assertEquals(groups.values().toArray()[0], "List7");
    }
}


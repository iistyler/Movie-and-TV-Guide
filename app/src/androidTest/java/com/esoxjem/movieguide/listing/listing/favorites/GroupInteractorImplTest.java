package com.esoxjem.movieguide.listing.listing.favorites;


import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.listing.lists.DBClass;
import com.esoxjem.movieguide.listing.lists.GroupInteractorImpl;
import com.esoxjem.movieguide.listing.lists.ListInteractorImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class GroupInteractorImplTest {

    @Test
    public void createGroupTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();

        // Create group
        groupInteractor.createGroup("Group1");
        Map<Integer, String> groups = groupInteractor.getAllGroups();

        // Check to see if group exists
        Integer firstKey = groups.keySet().iterator().next();
        assertEquals(groups.get(firstKey), "Group1");
    }

    @Test
    public void updateGroupTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();

        // Create first group
        groupInteractor.createGroup("Group1");
        Map<Integer, String> groups = groupInteractor.getAllGroups();

        // Update group
        Integer firstKey = groups.keySet().iterator().next();
        groupInteractor.changeGroupName(firstKey, "Group2");


        // Check group was updated
        groups = groupInteractor.getAllGroups();
        assertEquals(groups.values().toArray()[0], "Group2");
    }

    @Test
    public void removeGroupTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();

        // Create some groups
        groupInteractor.createGroup("Group1");
        groupInteractor.createGroup("Group2");
        groupInteractor.createGroup("Group3");
        Map<Integer, String> groups = groupInteractor.getAllGroups();

        // Remove group
        for (Map.Entry<Integer, String> current : groups.entrySet()) {
            if (current.getValue().equals("Group1") || current.getValue().equals("Group3") )
                groupInteractor.removeGroup(current.getKey());
        }

        // Check group was updated
        groups = groupInteractor.getAllGroups();
        assertEquals("Group2", groups.values().toArray()[0]);

    }

    @Test
    public void getLastInsertIDTest() {
        DBClass movieDB = DBClass.getInstance();
        movieDB.createDB();
        movieDB.resetDB();

        Integer sample = -1;

        // Create some groups
        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
        groupInteractor.createGroup("Group1");
        groupInteractor.createGroup("Group2");
        Map<Integer, String> groups = groupInteractor.getAllGroups();

        // Get the ID of the last inserted group
        for (Map.Entry<Integer, String> current : groups.entrySet())
            if ( current.getValue().equals("Group2") )
                sample = current.getKey();

        // Get the last insert ID from DB Class
        Integer sample2 = DBClass.getLastInsertID();

        // Compare to see if equal
        assertEquals(sample, sample2);
    }

    @Test
    public void exportGroupTest() {
        // Get Interactors
        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        DBClass movieDB = DBClass.getInstance();

        // Reset
        movieDB.createDB();
        movieDB.resetDB();
        listInteractor.resetListInteractor();

        List<Integer> groups = new ArrayList<>();

        // Create some groups
        int group1Id = groupInteractor.createGroup("Group1");
        groups.add( group1Id );

        int group2Id = groupInteractor.createGroup("Group2");
        groups.add( group2Id );

        // Add lists to those groups
        listInteractor.createList("List1", group1Id);
        int list2 = listInteractor.createList("List2", group1Id);
        int list3 = listInteractor.createList("List3", group2Id);

        // Create some movies
        Movie movie = new Movie();
        movie.setId("157336");

        Movie movie2 = new Movie();
        movie2.setId("135397");

        // Add those movies to some lists
        listInteractor.addToList(movie, list2);
        listInteractor.addToList(movie2, list2);
        listInteractor.addToList(movie, list3);

        // Try the export and check accuracy
        try {
            String exportedGroup = groupInteractor.exportGroups(groups);

            final String expectedExport = "[{\"name\":\"Group1\",\"lists\":[{\"name\":\"List1\",\"movies\":[]},{\"name\":\"List2\",\"movies\":[{\"id\":\"157336\",\"type\":\"movie\"},{\"id\":\"135397\",\"type\":\"movie\"}]}]}, {\"name\":\"Group2\",\"lists\":[{\"name\":\"List3\",\"movies\":[{\"id\":\"157336\",\"type\":\"movie\"}]}]}]";
            assertEquals(expectedExport, exportedGroup);

        } catch (JSONException e) {
            fail("Should not have thrown JSON exception on export");
        }
    }

    @Test
    public void importGroupTest() {
        final String importJSON = "[{\"name\":\"Group1\",\"lists\":[{\"name\":\"List1\",\"movies\":[]},{\"name\":\"List2\",\"movies\":[{\"id\":\"157336\",\"type\":\"movie\"},{\"id\":\"135397\",\"type\":\"movie\"}]}]}, {\"name\":\"Group2\",\"lists\":[{\"name\":\"List3\",\"movies\":[{\"id\":\"157336\",\"type\":\"movie\"}]}]}]";
        // Get Interactors
        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        DBClass movieDB = DBClass.getInstance();

        // Reset
        movieDB.createDB();
        movieDB.resetDB();
        listInteractor.resetListInteractor();

        List<Integer> groups = new ArrayList<>();
        groups.add( -1 );

        try {
            groupInteractor.importGroups(importJSON);
            String exportedGroup = groupInteractor.exportGroups(groups);

            assertEquals(importJSON, exportedGroup);
        } catch (JSONException e) {
            fail("Should not have thrown JSON exception");
        }
    }
}

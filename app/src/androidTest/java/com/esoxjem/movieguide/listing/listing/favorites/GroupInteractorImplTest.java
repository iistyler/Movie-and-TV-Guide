package com.esoxjem.movieguide.listing.listing.favorites;


import com.esoxjem.movieguide.listing.favorites.DBClass;
import com.esoxjem.movieguide.listing.favorites.GroupInteractorImpl;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

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

}

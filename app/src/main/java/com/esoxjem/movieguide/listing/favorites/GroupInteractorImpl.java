package com.esoxjem.movieguide.listing.favorites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupInteractorImpl implements GroupInteractor {

    static GroupInteractorImpl groupInteractor = new GroupInteractorImpl();

    public static GroupInteractorImpl getInstance() {
        return groupInteractor;
    }

    public Map<Integer, String> getAllGroups() {
        Map<Integer, String> groups = new HashMap<>();

        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select * from Groups");

        for (Map<String, String> current : result) {
            groups.put( Integer.parseInt(current.get("groupId")), current.get("groupName") );
        }

        return groups;
    }

    public void createGroup(String groupName) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("INSERT INTO Groups (groupName) VALUES ('" + groupName + "')");
    }

    public void removeGroup(int groupId) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("DELETE FROM Groups WHERE groupId = " + groupId);
    }

    public void changeGroupName(int groupId, String newName) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("UPDATE Groups SET groupName = '" + newName + "' WHERE groupId = " + groupId);
    }
}

package com.esoxjem.movieguide.listing.lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupInteractorImpl implements GroupInteractor {

    static GroupInteractorImpl groupInteractor = new GroupInteractorImpl();

    public static GroupInteractorImpl getInstance() {
        return groupInteractor;
    }

    public Map<Integer, String> getAllGroups() {
        Map<Integer, String> groups = new HashMap<>();

        DBClass movieDB = DBClass.getInstance();
        List<Map<String, String>> result = movieDB.query("Select groupId, groupName from Groups ORDER BY groupId ASC");

        for (Map<String, String> current : result) {
            groups.put( Integer.parseInt( current.get("groupId") ), current.get("groupName") );
        }

        return groups;
    }

    public int createGroup(String groupName) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("INSERT INTO Groups (groupName) VALUES ('" + groupName + "')");

        return movieDB.getLastInsertID();
    }

    public void removeGroup(int groupId) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("DELETE FROM Groups WHERE groupId = " + groupId);
    }

    public void changeGroupName(int groupId, String newName) {
        DBClass movieDB = DBClass.getInstance();
        movieDB.query("UPDATE Groups SET groupName = '" + newName + "' WHERE groupId = " + groupId);
    }

    public String exportGroups(List<Integer> groupId) throws JSONException {
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        List<JSONObject> listOfGroups = new ArrayList<>();
        Map<Integer, String> groups = getAllGroups();

        // Note to export all
        if (groupId.get(0) == -1) {
            groupId.remove(0);
            Set<Integer> groupKeys = groups.keySet();
            List<Integer> groupKeysList = new ArrayList<Integer>(groupKeys);
            Collections.sort(groupKeysList);

            for (Integer currentGroupId : groupKeysList) {
                groupId.add(currentGroupId);
            }
        }

        // Go through each object to export
        for (Integer currentGroup : groupId) {

            if (groups.containsKey( currentGroup )) {

                JSONObject json = new JSONObject();
                json.put("name", groups.get( currentGroup ) );
                json.put("lists", listInteractor.exportLists( currentGroup ));
                listOfGroups.add(json);
            }
        }

        return listOfGroups.toString();
    }

    public void importGroups(String importData) throws JSONException {
        // Get Interactors
        JSONArray jsonGroups = new JSONArray(importData);
        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
        DBClass movieDB = DBClass.getInstance();

        // Go through groups
        for (int i = 0; i < jsonGroups.length(); i++) {
            JSONObject currentJsonGroup = jsonGroups.getJSONObject(i);
            String groupName = currentJsonGroup.getString("name");
            int currentGroupId = groupInteractor.createGroup(groupName);


            // Go through lists
            JSONArray jsonLists = new JSONArray( currentJsonGroup.getString("lists") );
            for (int j = 0; j < jsonLists.length(); j++) {
                JSONObject currentJsonList = jsonLists.getJSONObject(j);
                String listName = currentJsonList.getString("name");
                int currentListId = listInteractor.createList(listName, currentGroupId);

                // Go through movies
                JSONArray jsonMovies = new JSONArray( currentJsonList.getString("movies") );
                for (int k = 0; k < jsonMovies.length(); k++) {
                    JSONObject currentJsonMovie = jsonMovies.getJSONObject(k);
                    String movieId = currentJsonMovie.getString("id");
                    String movieType = currentJsonMovie.getString("type");

                    listInteractor.addToListById(Integer.parseInt(movieId), movieType, currentListId);
                    System.out.println("        Movies: " + movieId);
                }
                System.out.println("    Lists: " + listName);
            }
            System.out.println("Group: " + groupName);
        }
    }
}

package com.esoxjem.movieguide.listing.lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface GroupInteractor {
    public Map<Integer, String> getAllGroups();
    public int createGroup(String groupName);
    public void removeGroup(int groupId);
    public void changeGroupName(int groupId, String newName);
    public String exportGroups(List<Integer> groupId) throws JSONException;
}

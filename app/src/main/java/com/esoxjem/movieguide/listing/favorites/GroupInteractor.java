package com.esoxjem.movieguide.listing.favorites;

import java.util.List;
import java.util.Map;

public interface GroupInteractor {
    public Map<Integer, String> getAllGroups();
    public void createGroup(String groupName);
    public void removeGroup(int groupId);
    public void changeGroupName(int groupId, String newName);
}

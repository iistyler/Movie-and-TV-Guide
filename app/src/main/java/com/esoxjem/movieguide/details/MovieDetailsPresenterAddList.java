package com.esoxjem.movieguide.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.listing.MoviesListingDrawerAdapter;
import com.esoxjem.movieguide.listing.lists.GroupInteractorImpl;
import com.esoxjem.movieguide.listing.lists.ListInteractor;
import com.esoxjem.movieguide.listing.lists.ListInteractorImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDetailsPresenterAddList extends AppCompatActivity
{
    private static ExpandableListAdapter listAdapter;

    private static List<String> categoryStrings;
    private static List<Integer> categoryIds;
    private static ExpandableListView groupsList;
    private static HashMap<String, List<String>> categoryItems;
    private static Activity classActivity;
    private static View classView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    public static void setupList(Activity currentActivity, Movie movie, View lastView) {
        groupsList = (ExpandableListView)currentActivity.findViewById(R.id.groupsList);
        addDrawerItems(currentActivity, movie);
        groupsList.setAdapter(listAdapter);
        classActivity = currentActivity;
        classView = lastView;

        TextView closeButton = (TextView)currentActivity.findViewById(R.id.closeAddButton);
        closeButton.setOnClickListener( closeWindow );
    }

    private static View.OnClickListener closeWindow = new View.OnClickListener() {
        public void onClick(View v) {
            classView = (View)classView.getParent();
            ViewGroup view1 = (ViewGroup)classView.getParent();
            if (view1 != null)
                view1.removeView(classView);

            classActivity.setContentView(classView);
        }
    };

    private static void addDrawerItems(Activity currentActivity, final Movie movie)
    {
        /* Create a new hash map containing the list heads as the key
        *  and String lists as items */
        categoryStrings = new ArrayList<>();
        categoryItems = new HashMap<String, List<String>>();
        categoryIds = new ArrayList<>();

        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        Map<Integer, String> groups = groupInteractor.getAllGroups();

        List<Integer> groupKeysList = new ArrayList<>(groups.keySet());
        Collections.sort(groupKeysList);

        // Go through each group and add it
        for (Integer currentGroupId : groupKeysList) {
            categoryStrings.add( groups.get(currentGroupId) );
            categoryIds.add( currentGroupId );

            String currentGroupName = groups.get(currentGroupId);
            List<String> listArray = new ArrayList<>();

            // go through each list and add it
            Map<Integer, String> lists = listInteractor.getLists( currentGroupId );
            List<Integer> listKeysList = new ArrayList<>(lists.keySet());
            Collections.sort(listKeysList);

            for (Integer currentListId : listKeysList) {
                listArray.add( lists.get(currentListId) );
            }

            categoryItems.put(currentGroupName, listArray);
        }

        groupsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                // Get Info
                ListInteractor listInteractor = ListInteractorImpl.getInstance();
                Integer groupId = categoryIds.get(groupPosition);
                Map<Integer, String> lists = listInteractor.getLists(groupId);
                String listName = categoryItems.get(categoryStrings.get(groupPosition)).get(childPosition);

                // Fetch list ID
                Integer listId = -1;
                for (Map.Entry<Integer, String> list : lists.entrySet()) {
                    if (list.getValue().equals( listName )) {
                        listId = list.getKey();
                        break;
                    }
                }

                listInteractor.addToList(movie, listId);

                classView = (View)classView.getParent();
                ViewGroup view1 = (ViewGroup)classView.getParent();
                if (view1 != null)
                    view1.removeView(classView);

                classActivity.setContentView(classView);

                return true;
            }

        });

        /* Create a new list adapter */
        listAdapter = new AddMoviesDrawerAdapter(currentActivity, categoryStrings, categoryItems, groupsList);
    }
}
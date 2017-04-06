package com.esoxjem.movieguide.listing;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.os.StrictMode;

import com.esoxjem.movieguide.NFC;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.details.MovieDetailsActivity;
import com.esoxjem.movieguide.details.MovieDetailsFragment;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.details.MovieDetailsGroupActivity;
import com.esoxjem.movieguide.listing.lists.GroupInteractorImpl;
import com.esoxjem.movieguide.listing.lists.ListInteractor;
import com.esoxjem.movieguide.listing.lists.ListInteractorImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MoviesListingGroupActivity extends AppCompatActivity implements MoviesListingGroupFragment.Callback
{
    public static final String DETAILS_FRAGMENT = "DetailsFragment";
    private boolean twoPaneMode;
    private ExpandableListView mDrawerList;
    private MoviesListingDrawerAdapter listAdapter;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private List<String> categoryStrings;
    private List<Integer> categoryIds;
    private HashMap<String, List<String>> categoryItems;
    private List<Movie> moviesList;
    private Context context;
    private int listID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouping);
        setToolbar();

        context = this;

        MoviesListingGroupFragment frag = (MoviesListingGroupFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);

        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        //Getting listID from MovieListingActivity.
        listID = getIntent().getIntExtra("MOVIES_ID",0);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        moviesList = listInteractor.getMoviesOnList(listID);

        frag.changeMovieList(moviesList);

        mDrawerList = (ExpandableListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        addDrawerItems();
        setupDrawer();

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setHomeButtonEnabled(true);

        if (findViewById(R.id.movie_details_container) != null)
        {
            twoPaneMode = true;

            if (savedInstanceState == null)
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new MovieDetailsFragment())
                        .commit();
            }
        } else
        {
            twoPaneMode = false;
        }
    }

    private void addDrawerItems()
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

        categoryStrings.add(0, "");

        // Go through each group and add it
        for (int i = 0; i < groupKeysList.size(); i++) {
            categoryStrings.add( groups.get(groupKeysList.get(i)) );
            categoryIds.add(groupKeysList.get(i));

            String currentGroupName = groups.get(groupKeysList.get(i));
            List<String> listArray = new ArrayList<>();

            // go through each list and add it
            Map<Integer, String> lists = listInteractor.getLists(groupKeysList.get(i));
            List<Integer> listKeysList = new ArrayList<>(lists.keySet());
            Collections.sort(listKeysList);

            for (int j = 0; j < listKeysList.size(); j++) {
                listArray.add(lists.get(listKeysList.get(j)));
            }

            categoryItems.put(currentGroupName, listArray);
        }

        /* Create a new list adapter */
        listAdapter = new MoviesListingDrawerAdapter(this, categoryStrings, categoryItems, mDrawerList);
    }

    private void setupDrawer() {

        mDrawerList.setAdapter(listAdapter);

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                /* If the list doesn't get collapsed/expanded, return true (we are handling it)*/
//                if (groupPosition != 0)
//                    return true;

                return false;
            }

        });

        /*The next two functions are if we decide not to handle it. They will simply expand or collapse with notice*/
        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), categoryStrings.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }

        });

        mDrawerList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), categoryStrings.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
            }

        });

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
//                Toast.makeText(getApplicationContext(), categoryStrings.get(groupPosition) + " : " +
//                        categoryItems.get(categoryStrings.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                // Get Info
                ListInteractor listInteractor = ListInteractorImpl.getInstance();
                Integer groupId = categoryIds.get(groupPosition - 1);
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

                /**Toast.makeText(getApplicationContext(), groupId + " : " +
                        listId, Toast.LENGTH_SHORT).show();**/

                mDrawerLayout.closeDrawers();

                Intent intent = new Intent(context, MoviesListingGroupActivity.class);
                intent.putExtra("MOVIES_ID", listId);
                startActivity(intent);

                return true;
            }

        });

        /* Function to toggle the drawer being open or closed */
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        /* Make sure the toggle indicator is enabled and set the layout to use the new toggle */
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void setToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(R.string.movie_guide);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_share:
                //Toast.makeText(MoviesListingActivity.this, "NFC Button Event", Toast.LENGTH_SHORT).show();
                //Call required NFC Functions here.
                Intent intent = new Intent(this, NFC.class);
                startActivity(intent);
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main_group, menu);
        return true;
    }

    @Override
    public void onMoviesLoaded(Movie movie)
    {
        if(twoPaneMode)
        {
            loadMovieFragment(movie);
        } else
        {
            // Do not load in single pane view
        }
    }

    @Override
    public void onMovieClicked(Movie movie)
    {
        if (twoPaneMode)
        {
            loadMovieFragment(movie);
        } else
        {
            startMovieActivity(movie);
        }
    }

    private void startMovieActivity(Movie movie)
    {
        Intent intent = new Intent(this, MovieDetailsGroupActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE, movie);
        intent.putExtras(extras);
        intent.putExtra("MOVIES_ID", getIntent().getIntExtra("MOVIES_ID",0));

        startActivity(intent);
    }

    private void loadMovieFragment(Movie movie)
    {
        int listId = getIntent().getIntExtra("MOVIES_ID",0);
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movie, listId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, movieDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onResume() {
        super.onResume();
        addDrawerItems();
        setupDrawer();

        MoviesListingGroupFragment frag = (MoviesListingGroupFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
        moviesList = ListInteractorImpl.getInstance().getMoviesOnList(listID);
        frag.changeMovieList(moviesList);

        listAdapter.notifyDataSetChanged();
    }
}
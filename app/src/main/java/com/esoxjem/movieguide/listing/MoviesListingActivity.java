package com.esoxjem.movieguide.listing;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.esoxjem.movieguide.NFC;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.details.MovieDetailsActivity;
import com.esoxjem.movieguide.details.MovieDetailsFragment;
import com.esoxjem.movieguide.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoviesListingActivity extends AppCompatActivity implements MoviesListingFragment.Callback
{
    public static final String DETAILS_FRAGMENT = "DetailsFragment";
    private boolean twoPaneMode;
    private ExpandableListView mDrawerList;
    private MoviesListingDrawerAdapter listAdapter;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private List<String> categoryStrings;
    private HashMap<String, List<String>> categoryItems;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        mDrawerList = (ExpandableListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        //String[] osArray = { "Most Popular", "Highest Rated", "Favorites"};
        //mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        categoryStrings = new ArrayList<>();
        categoryItems = new HashMap<String, List<String>>();

        categoryStrings.add("Favourites");
        categoryStrings.add("Stuff to Watch");
        categoryStrings.add("Recomendations");

        List<String> options = new ArrayList<>();
        options.add("+ Add");
        options.add("- Delete");

        categoryItems.put(categoryStrings.get(0), options);
        categoryItems.put(categoryStrings.get(1), null);
        categoryItems.put(categoryStrings.get(2), null);

        listAdapter = new MoviesListingDrawerAdapter(this, categoryStrings, categoryItems, mDrawerList);
    }

    private void setupDrawer() {

        mDrawerList.setAdapter(listAdapter);

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                if (groupPosition != 0)
                    return true;

                return false;
            }

        });

        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), categoryStrings.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }

        });

        mDrawerList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), categoryStrings.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
            }

        });

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), categoryStrings.get(groupPosition) + " : " +
                        categoryItems.get(categoryStrings.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }

        });

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void loadMovieFragment(Movie movie)
    {
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, movieDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }
}

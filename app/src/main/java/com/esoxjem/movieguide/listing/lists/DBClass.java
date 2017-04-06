package com.esoxjem.movieguide.listing.lists;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esoxjem.movieguide.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBClass {

    private static DBClass dbClass = new DBClass( );

    public static DBClass getInstance() {
        return dbClass;
    }

    // Database Name
    private static final String DATABASE_NAME = "/data/data/com.esoxjem.movieguide/MovieGuideDB";
    private static SQLiteDatabase mainDB = null;

    private static String[] databaseScheme = {
            "Groups (groupId INTEGER PRIMARY KEY AUTOINCREMENT, groupName VARCHAR(255))",
            "Lists (listId INTEGER PRIMARY KEY AUTOINCREMENT, listName VARCHAR(255), groupId INTEGER NOT NULL)",
            "SavedMovies (movieId INTEGER NOT NULL, listId INTEGER NOT NULL, objectType INTEGER NOT NULL)" // Object type
    };


    public static void createDB() {

        mainDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        int dbExists = checkIfExists();

        for (String aDatabaseScheme : databaseScheme) {
            mainDB.execSQL("CREATE TABLE IF NOT EXISTS " + aDatabaseScheme);
        }

        if (dbExists == 0)
            setupInitialDB();
    }

    // This is mainly for dev purposes
    public static void resetDB() {
        mainDB.execSQL("DELETE FROM Groups");
        mainDB.execSQL("DELETE FROM Lists");
        mainDB.execSQL("DELETE FROM SavedMovies");
    }

    public static List<Map<String, String>> query(String statement) {

        // Check for select
        if (statement == null)
            return null;

        Cursor resultsCursor = mainDB.rawQuery(statement, null);
        resultsCursor.moveToFirst();

        List<Map<String, String>> resultData = new ArrayList<>();

        int i = 0;
        while(!resultsCursor.isAfterLast()){

            Map<String, String> map = new HashMap<String, String>();
            int count = resultsCursor.getColumnCount();

            // Add each column and value pair to map
            for (int j = 0; j < count; j++) {
                String fieldName = resultsCursor.getColumnName(j);
                String fieldValue = resultsCursor.getString(j);
                map.put(fieldName, fieldValue);
            }
            resultsCursor.moveToNext();

            // Add map to result data
            resultData.add(i, map);
            i++;
        }
        resultsCursor.close();

        return resultData;
    }

    public static Integer checkIfExists() {
        List<Map<String, String>> result;
        result = DBClass.query("SELECT COUNT(name) as doesExist FROM sqlite_master WHERE type='table' AND name='Groups'");

        if (Integer.parseInt( result.get(0).get("doesExist") ) > 0)
            return 1;
        else
            return 0;
    }

    public static void setupInitialDB() {
        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();

        // Create some groups
        int g1 = groupInteractor.createGroup("Favourites");
        //int l1 = listInteractor.createList("Movies", g1);
        //listInteractor.createList("TV Shows", g1);

        //listInteractor.addToListById(263115, 1, l1);
        //listInteractor.addToListById(1402, 0, l1);
    }

    public static Integer getLastInsertID() {
        List<Map<String, String>> result;
        result = DBClass.query("SELECT last_insert_rowid() AS lastID");

        return Integer.parseInt( result.get(0).get("lastID") );
    }
}

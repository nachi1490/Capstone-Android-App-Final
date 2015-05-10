package com.example.ido.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Anshul on 4/1/2015.
 *
 */
// This class is used for interacting with database
public class DatabaseAdapter {

	// Some variables used for interacting with database
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase sqLiteDatabase;
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	
	// Current context (activity)
	private final Context context;
	
	// Database name
	public static final String DATABASE_NAME = "hrs_dashboard.db";
	
	// Database version
	public static final int DATABASE_VERSION =5 ;

    // Dashboard table
    // Dashboard table - id column name
    public static final String Dashboard_TABLE_COLUMN_ID = "_id";
    // Dashboard table name
    public static final String Dashboard_TABLE_NAME = "_dashboard";
    // Task table - id column name
    public static final String Dashboard_TABLE_COLUMN_NAME = "_name";
    // Task table - title column name
    public static final String Dashboard_TABLE_COLUMN_URL = "_url";
    public static final String Dashboard_TABLE_COLUMN_LABEL = "_label";
    public static final String Dashboard_TABLE_COLUMN_STAR = "_star";
    // Dashboard table create statement
    public static final String Dashboard_TABLE_CREATE
            = "create table " + Dashboard_TABLE_NAME
            + " ( "
            +Dashboard_TABLE_COLUMN_ID +  " text primary key, "
            + Dashboard_TABLE_COLUMN_NAME + " text not null,  "
            + Dashboard_TABLE_COLUMN_LABEL + " integer not null,  "
            + Dashboard_TABLE_COLUMN_STAR + " integer not null,  "
            + Dashboard_TABLE_COLUMN_URL + " text not null  "
            + " );";

	public static String sample1data;
    public static String sample2data;
	// STATIC CLASS DATABASE HELPER
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		// Must override implicit constructor
		public DatabaseHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL(DatabaseAdapter.Dashboard_TABLE_CREATE);
            addSampleData(db);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop Collaborator table is exist
            db.execSQL("Drop table if exists " + DatabaseAdapter.Dashboard_TABLE_NAME);
			onCreate(db);
		}

        public void addSampleData(SQLiteDatabase db){

            ContentValues initialValues = new ContentValues();
            initialValues.put(Dashboard_TABLE_COLUMN_ID, 1);
            initialValues.put(Dashboard_TABLE_COLUMN_NAME, "Sample 1");
            initialValues.put(Dashboard_TABLE_COLUMN_LABEL, 1);
            initialValues.put(Dashboard_TABLE_COLUMN_STAR, 1);
            initialValues.put(Dashboard_TABLE_COLUMN_URL, "https://public.tableau.com/profile/publish/WorldIndicators_317/GDPperCapitaDashboard#!/publish-confirm");

            db.insert(Dashboard_TABLE_NAME, null, initialValues);

            ContentValues initialValues1 = new ContentValues();
            initialValues1.put(Dashboard_TABLE_COLUMN_ID, 2);
            initialValues1.put(Dashboard_TABLE_COLUMN_NAME, "Sample 2");
            initialValues1.put(Dashboard_TABLE_COLUMN_LABEL, 2);
            initialValues1.put(Dashboard_TABLE_COLUMN_STAR, 0);
            initialValues1.put(Dashboard_TABLE_COLUMN_URL, "https://public.tableau.com/profile/anshul2488#!/vizhome/wb1_2/Dashboard2");

            db.insert(Dashboard_TABLE_NAME, null, initialValues1);

            ContentValues initialValues2 = new ContentValues();
            initialValues2.put(Dashboard_TABLE_COLUMN_ID, 3);
            initialValues2.put(Dashboard_TABLE_COLUMN_NAME, "Sample 3");
            initialValues2.put(Dashboard_TABLE_COLUMN_LABEL, 0);
            initialValues2.put(Dashboard_TABLE_COLUMN_STAR, 1);
            initialValues2.put(Dashboard_TABLE_COLUMN_URL, "https://www.facebook.com");

            db.insert(Dashboard_TABLE_NAME, null, initialValues2);


        }
	}
	
	// UTILITY FUNCTIONS FOR CREATING DATABASE AND MANIPULATING DATA
	
	// Constructor, pass the current activity to the context
	public DatabaseAdapter(Context context){
		this.context = context;
	}
	
	// Open connection to database, should be called right after constructor
	public DatabaseAdapter open(){
		databaseHelper = new DatabaseHelper(context, this.DATABASE_NAME, null, this.DATABASE_VERSION);
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		return this;
	}
	
	// Close connection to database, should be called at the end when everything is finished
	public void close(){
		databaseHelper.close();
	}


    // Insert a new Dashboard into Dashboard table
    public void insertDashboard(DashBoard dashboard){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Dashboard_TABLE_COLUMN_ID, dashboard.getId());
        initialValues.put(Dashboard_TABLE_COLUMN_NAME, dashboard.getName());
        initialValues.put(Dashboard_TABLE_COLUMN_LABEL, dashboard.getLabel());
        initialValues.put(Dashboard_TABLE_COLUMN_STAR, dashboard.getStar());
        initialValues.put(Dashboard_TABLE_COLUMN_URL, dashboard.getUrl());

        sqLiteDatabase.insert(Dashboard_TABLE_NAME, null, initialValues);


    }
    // anshul's sample api
    public Cursor getLists (CharSequence constraint,int pos)  {


        if(constraint==null|| constraint.length()==0){
            switch (pos) {
                case 0:
                        return getAllDashboards();
                case 1:
                        return getStarDashboards();
                case 2:
                case 3:
                case 4:
                case 5:
                        return getLabelDashboards(pos-2);
                default:
                    return null;
            }
        }
        else{
            String [] asColumnsToReturn = {Dashboard_TABLE_NAME + "." + Dashboard_TABLE_COLUMN_NAME};
            String value = "%"+constraint.toString()+"%";

            switch (pos) {
                case 0:
                    return sqLiteDatabase.query(Dashboard_TABLE_NAME, null, "_dashboard._name LIKE ?", new String[]{value}, null, null, null);
                case 1:
                    return sqLiteDatabase.query(Dashboard_TABLE_NAME, null, "_dashboard._name LIKE ? AND _dashboard._star>0", new String[]{value}, null, null, null);
                case 2:
                case 3:
                case 4:
                case 5:
                    return sqLiteDatabase.query(Dashboard_TABLE_NAME, null, "_dashboard._name LIKE ? AND _dashboard._label = "+(pos-2), new String[]{value}, null, null, null);
                default:
                    return null;
            }

        }
    }

    public Cursor getList (CharSequence constraint,int pos)  {



        String [] asColumnsToReturn = {Dashboard_TABLE_NAME + "." + Dashboard_TABLE_COLUMN_NAME};
        String value = "%"+constraint.toString()+"%";

        switch (pos) {
            case 0:
            if (constraint == null || constraint.length() == 0) {
                //  Return the full list
                return getAllDashboards();
            } else {
                Log.v("verbose", "inside getList constraint");
                return sqLiteDatabase.query(Dashboard_TABLE_NAME, null, "_dashboard._name LIKE ?", new String[]{value}, null, null, null);
            }

            case 1:
                if (constraint == null || constraint.length() == 0) {
                    //  Return the full list
                    return getStarDashboards();
                } else {
                    Log.v("verbose", "inside getList constraint");
                    return sqLiteDatabase.query(Dashboard_TABLE_NAME, null, "_dashboard._name LIKE ? AND _dashboard._star>0", new String[]{value}, null, null, null);
                }


            case 2:
            case 3:
            case 4:
            case 5:
                if (constraint == null || constraint.length() == 0) {
                    //  Return the full list
                    return getLabelDashboards(pos-2);
                } else {
                    Log.v("verbose", "inside getList constraint");
                    return sqLiteDatabase.query(Dashboard_TABLE_NAME, null, "_dashboard._name LIKE ? AND _dashboard._label = "+(pos-2), new String[]{value}, null, null, null);
                }


             default:
                 return null;

        }

    }
    public Cursor getAllDashboards(){
        return sqLiteDatabase.query(Dashboard_TABLE_NAME,
                new String[] {Dashboard_TABLE_COLUMN_ID,Dashboard_TABLE_COLUMN_NAME,Dashboard_TABLE_COLUMN_LABEL,Dashboard_TABLE_COLUMN_STAR, Dashboard_TABLE_COLUMN_URL},
                null, null, null, null, null);
    }
    public Cursor getStarDashboards(){
        return sqLiteDatabase.query(Dashboard_TABLE_NAME,
                new String[] {Dashboard_TABLE_COLUMN_ID , Dashboard_TABLE_COLUMN_NAME,Dashboard_TABLE_COLUMN_LABEL,Dashboard_TABLE_COLUMN_STAR, Dashboard_TABLE_COLUMN_URL},
                Dashboard_TABLE_COLUMN_STAR + " > 0 ", null, null, null, null);
    }
    public Cursor getLabelDashboards(int label){
        return sqLiteDatabase.query(Dashboard_TABLE_NAME,
                new String[] { Dashboard_TABLE_COLUMN_ID , Dashboard_TABLE_COLUMN_NAME, Dashboard_TABLE_COLUMN_LABEL, Dashboard_TABLE_COLUMN_STAR, Dashboard_TABLE_COLUMN_URL},
                Dashboard_TABLE_COLUMN_LABEL + " = " + label + "", null, null, null, null);
    }

    // Find dashboard by its id
    public Cursor getDashboardById(String dashboardId){
        return sqLiteDatabase.query(Dashboard_TABLE_NAME,
                new String[] {Dashboard_TABLE_COLUMN_ID , Dashboard_TABLE_COLUMN_NAME,Dashboard_TABLE_COLUMN_LABEL,Dashboard_TABLE_COLUMN_STAR, Dashboard_TABLE_COLUMN_URL},
                Dashboard_TABLE_COLUMN_ID + " = '" + dashboardId + "'", null, null, null, null);
    }

    // Edit an existing Dashboard
    public void editExistingDashboard(DashBoard dashboard){
        ContentValues updateValues;

        // Update Task table first
        updateValues = new ContentValues();
        updateValues.put(Dashboard_TABLE_COLUMN_NAME, dashboard.getName());
        updateValues.put(Dashboard_TABLE_COLUMN_LABEL, dashboard.getLabel());
        updateValues.put(Dashboard_TABLE_COLUMN_STAR, dashboard.getStar());
        updateValues.put(Dashboard_TABLE_COLUMN_URL, dashboard.getUrl());

        sqLiteDatabase.update(Dashboard_TABLE_NAME, updateValues, Dashboard_TABLE_COLUMN_ID + " = '" + dashboard.getId() + "'", null);

        // Update Collaborator table
    }

    // delete the selected Dashboard
    public void deleteDashboard(DashBoard dashboard){
        deleteDashboard(dashboard.getId());
    }


    // delete the selected Dashboard
    public void deleteDashboard(String dashboardId){
        sqLiteDatabase.delete(Dashboard_TABLE_NAME, Dashboard_TABLE_COLUMN_ID + " = '" + dashboardId + "'", null);
    }


    // Return a new randomly generated Dashboard id
    public String getNewDashboardId(){
        String uuid = null;
        Cursor cursor = null;

        // Create a random uuid and then check if it's exist
        do {
            uuid = UUID.randomUUID().toString();
            cursor = getDashboardById(uuid);
        } while (cursor.getCount() > 0);

        return uuid;
    }



}

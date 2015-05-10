package com.example.ido.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.ido.R;
import com.example.ido.controller.ApplicationNavigationHandler;
import com.example.ido.model.DashBoard;
import com.example.ido.model.DatabaseAdapter;

/**
 * Created by Anshul & Nachiket   on 4/1/2015.
 *

 */


public class ViewAllDashBoardActivity extends GeneralActivity {
    public static int  drawerElementSize=6;
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getApplicationContext(),position, Toast.LENGTH_LONG).show();
            selectItem(position);
        }
    }
    private String[] mNavigationDrawerItemTitles;
    private int[] drawable_icons;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    // The List View that shows all Tasks
    private ListView allDashBoardsListView;
    private int drawerIndexSelected =0;
    private CharSequence searchString=null;
    ActionBarDrawerToggle mDrawerToggle;

    // DatabaseAdapter for interacting with database
    private DatabaseAdapter databaseAdapter;

    // The cursor for query all groups from database
    private Cursor allDashboardsCursor;

    // Adapter for All Tasks List View
    private SimpleCursorAdapter allDashboardsListViewAdapter;

    private EditText inputSearch;
    // The Add New Task request code
    public static final int ADD_NEW_DASHBOARD_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_dashboard);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawable_icons = new int[drawerElementSize];
        drawable_icons[0] = R.drawable.ic_action_home;
        drawable_icons[1] = R.drawable.ic_action_star_5;
        drawable_icons[2] = R.drawable.ic_action_users;
        drawable_icons[3] =  R.drawable.ic_action_calculator;
        drawable_icons[4] = R.drawable.ic_action_line_chart;
        drawable_icons[5] =  R.drawable.ic_action_paste;

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[drawerElementSize];
        for(int i=0;i<drawerElementSize;i++){
            drawerItem[i] = new ObjectDrawerItem(drawable_icons[i], mNavigationDrawerItemTitles[i]);
        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // set action listener for allTasksListView
        allDashBoardsListView = (ListView) findViewById(R.id.activity_view_all_dashboards_Listview_all_dashboards);
        allDashBoardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                allDashboardListViewItemClickHandler(arg0, arg1, arg2);
            }
        });

        // Open the connection to database
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();
        // Init all tasks and add them to list view
        initAllDashboardsListView();
        search();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }



    public Cursor getDashboards(int index){
        switch(index){
            case 0:
                return databaseAdapter.getAllDashboards();
            case 1:
                return databaseAdapter.getStarDashboards();
            default:
                return databaseAdapter.getLabelDashboards(index-2);

        }
    }


    // Init the Dashboard List View
    // Load all Dashboards from database and put them to list view
    public void initAllDashboardsListView(){
        // Check if the databaseAdapter is not null
        if(this.databaseAdapter != null){
            // Get all Tasks
            allDashboardsCursor = databaseAdapter.getLists(null,0);
            // TODO replace the deprecated startManagingCursor method with an alternative one
            startManagingCursor(allDashboardsCursor);
            // Get data from which column
            String[] from = new String[]{DatabaseAdapter.Dashboard_TABLE_COLUMN_NAME};
            // Put data to which components in layout
            int[] to = new int[]{R.id.activity_view_dashboard_all_groups_listview_all_groups_layout_textview_group_title};
            allDashboardsListViewAdapter = new SimpleCursorAdapter(this,
                    R.layout.activity_view_dashboard_all_groups_listview_all_groups_layout, allDashboardsCursor, from, to ,1);
            // Set the adapter for the list view
            this.allDashBoardsListView.setAdapter(allDashboardsListViewAdapter);
        }
        else{

        }
    }

    private void search(){
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        allDashBoardsListView.setFastScrollEnabled(true);
        allDashBoardsListView.setTextFilterEnabled(true);


        EditText etext=(EditText)findViewById(R.id.inputSearch);
        etext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//            ListView av = (ListView)findViewById(R.id.activity_view_dashboard_all_groups_listview_starred);
//            SimpleCursorAdapter filterAdapter = (SimpleCursorAdapter)av.getAdapter();
                allDashboardsListViewAdapter.getFilter().filter(s.toString());

            }
        });
        setFilterQuery(0);


    }

    // Handle the item clicked event of allTasksListView
    private void allDashboardListViewItemClickHandler(AdapterView<?> adapterView, View listView, int selectedItemId){
        // Create a new Dashboard object and init the data
        // After that pass it to the next activity to display detail
        DashBoard selectedDashboard = new DashBoard();
        allDashboardsCursor=databaseAdapter.getLists(searchString,drawerIndexSelected);
        // move the cursor to the right position
        allDashboardsCursor.moveToFirst();
        allDashboardsCursor.move(selectedItemId);

        selectedDashboard.setId(allDashboardsCursor.getString(allDashboardsCursor.getColumnIndex(DatabaseAdapter.Dashboard_TABLE_COLUMN_ID)));
        selectedDashboard.setName(allDashboardsCursor.getString(allDashboardsCursor.getColumnIndex(DatabaseAdapter.Dashboard_TABLE_COLUMN_NAME)));
        selectedDashboard.setLabel(allDashboardsCursor.getInt(allDashboardsCursor.getColumnIndex(DatabaseAdapter.Dashboard_TABLE_COLUMN_LABEL)));
        selectedDashboard.setStar(allDashboardsCursor.getInt(allDashboardsCursor.getColumnIndex(DatabaseAdapter.Dashboard_TABLE_COLUMN_STAR)));
        selectedDashboard.setUrl(allDashboardsCursor.getString(allDashboardsCursor.getColumnIndex(DatabaseAdapter.Dashboard_TABLE_COLUMN_URL)));
        // start the activity
        ApplicationNavigationHandler.viewDashboardDetail(this, selectedDashboard);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.activity_view_all_dashboards_Menu_actionbar_Item_add_dashboard:
                ApplicationNavigationHandler.addNewDashboard(this, this.databaseAdapter);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_all_dashboards, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        allDashboardsCursor.requery();
    }
    @Override
    protected void onResume(){
        stopManagingCursor(allDashboardsCursor);
        super.onResume();
        allDashboardsCursor=databaseAdapter.getLists(searchString,drawerIndexSelected);
        // Get data from which column
        String[] from = new String[]{DatabaseAdapter.Dashboard_TABLE_COLUMN_NAME};
        // Put data to which components in layout
        int[] to = new int[]{R.id.activity_view_dashboard_all_groups_listview_all_groups_layout_textview_group_title};
        allDashboardsListViewAdapter = new SimpleCursorAdapter(this,
                R.layout.activity_view_dashboard_all_groups_listview_all_groups_layout, allDashboardsCursor, from, to ,1);
        // Set the adapter for the list view
        this.allDashBoardsListView.setAdapter(allDashboardsListViewAdapter);
        setFilterQuery(drawerIndexSelected);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void setFilterQuery(final int pos){
        allDashboardsListViewAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                if(constraint.length()==0){
                    searchString=null;
                }
                else{
                    searchString=constraint;
                }
                allDashboardsCursor = databaseAdapter.getLists(constraint,pos);
                return databaseAdapter.getLists(constraint, pos);
            }
        });
    }

    public void reloadPage(final int pos){
        if(this.databaseAdapter != null){
            // Get  Dashboards
            allDashboardsCursor=databaseAdapter.getLists(null,pos);

            String[] from = new String[]{DatabaseAdapter.Dashboard_TABLE_COLUMN_NAME};
            // Put data to which components in layout
            int[] to = new int[]{R.id.activity_view_dashboard_all_groups_listview_all_groups_layout_textview_group_title};
            // Init the adapter for list view
            allDashboardsListViewAdapter = new SimpleCursorAdapter(this,
                    R.layout.activity_view_dashboard_all_groups_listview_all_groups_layout, allDashboardsCursor, from, to ,1);

            setFilterQuery(pos);
            // Set the adapter for the list view
            allDashBoardsListView.setAdapter(allDashboardsListViewAdapter);

            mDrawerList.setItemChecked(pos, true);
            mDrawerList.setSelection(pos);
            mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        }
    }


    private void selectItem(final int position) {
        EditText etext=(EditText)findViewById(R.id.inputSearch);
        drawerIndexSelected =position;
        switch (position) {
            case 0:
                this.setTitle(R.string.app_name);
                searchString=null;
                etext.setText("");

                reloadPage(position);
                break;
            case 1:
                this.setTitle(mNavigationDrawerItemTitles[position]);
                searchString=null;
                etext.setText("");
                reloadPage(position);
                break;
            case 2:

            case 3:

            case 4:

            case 5:
                this.setTitle(mNavigationDrawerItemTitles[position]);
                searchString=null;
                etext.setText("");
                reloadPage(position);
                break;

            default:
                break;
        }
    }



}
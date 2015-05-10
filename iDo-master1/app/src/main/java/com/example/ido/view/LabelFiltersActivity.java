package com.example.ido.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


import android.database.Cursor;
import android.view.View;
import android.content.Context;
import android.widget.Spinner;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.ido.R;
import com.example.ido.model.DashBoard;
import com.example.ido.model.DatabaseAdapter;

/**
 * Created by Anshul & Nachiket   on 4/1/2015.
 *
 */

public class LabelFiltersActivity extends Activity {


    private DashBoard dashboard;
    private Button button;
    private DatabaseAdapter databaseAdapter;
    private Cursor allDashboardsCursor;
    private SimpleCursorAdapter allDashboardsListViewAdapter;
    private ListView allDashBoardsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label_filters);

        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();

        button = (Button) findViewById(R.id.label_filter);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                int labelIndex = ((Spinner)findViewById(R.id.activity_filter_dashboard_label_spinner)).getSelectedItemPosition();

                if(databaseAdapter != null){
                    // Get all Tasks
                    allDashboardsCursor = databaseAdapter.getLabelDashboards(labelIndex);
                    // TODO replace the deprecated startManagingCursor method with an alternative one
                    startManagingCursor(allDashboardsCursor);
                    // Get data from which column
                    String[] from = new String[]{DatabaseAdapter.Dashboard_TABLE_COLUMN_NAME};
                    // Put data to which components in layout
                    int[] to = new int[]{R.id.activity_view_dashboard_all_groups_listview_all_groups_layout_textview_group_title};
                    // Init the adapter for list view
                    // TODO replace the deprecated SimpleCursorAdapter with an alternative one
                    allDashboardsListViewAdapter = new SimpleCursorAdapter(getApplicationContext(),
                            R.layout.activity_view_dashboard_all_groups_listview_all_groups_layout, allDashboardsCursor, from, to ,1);
                    // Set the adapter for the list view
                    allDashBoardsListView.setAdapter(allDashboardsListViewAdapter);
                }
                else{

                }


            }

        });
    }



}

package com.example.ido.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ido.R;
import com.example.ido.R.layout;
import com.example.ido.controller.ApplicationNavigationHandler;
import com.example.ido.controller.ConfirmDeletionDialog;
import com.example.ido.model.DashBoard;
import com.example.ido.model.DatabaseAdapter;

/**
 * Created by Anshul
 *
 */
public class ViewDashboardDetailActivity extends GeneralActivity {

	// The task object that this activity is going to display
	// It's retrieved from bundle in onCreate()
	private DashBoard dashboard;
    private Button button;

	// Request code when user select edit
	public static final int EDIT_DASHBOARD_REQUEST_CODE = 1;

	// Database Adapter for interacting with database
	private DatabaseAdapter databaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_view_dashboard_detail);

		// Open the connection to database
		databaseAdapter = new DatabaseAdapter(this);
		databaseAdapter.open();

		// Retrieve the task object from the bundle
		Bundle dashboardDetailBundle = this.getIntent().getExtras();
		try{
			this.dashboard = (DashBoard) dashboardDetailBundle.getSerializable(DashBoard.Dashboard_BUNDLE_KEY);
		} catch (Exception ex){
			ex.printStackTrace();
		}

		// Next, check if the task object is null or not
		if(this.dashboard == null){
			// If null, close this activity
			this.finish();
		} else {
			// If not null, get all data from the task object and then put it to the view
			this.putDataToView();
		}

        button = (Button) findViewById(R.id.browseButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TextView dashboardUrl = (TextView) findViewById(R.id.activity_view_dashboard_detail_TextView_dashboard_url_content);
                String URL = dashboardUrl.getText().toString();
                if (!URLUtil.isValidUrl(URL)) {
                    Toast.makeText(getApplicationContext(), "Invalid URL specified", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent internetIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(URL));
                    //internetIntent.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
                    internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(internetIntent);
                }
            }

        });



    }

	// Extract data members in this.task object and then put the information into view
	private void putDataToView(){
		// First, check if the task object is null or not
		if(this.dashboard == null){
			// If null, close this activity
			this.finish();
		} else {
			// If not null, get all data from the task object and then put it to the view
			// set the title
			TextView dashboardNameTextView = (TextView) findViewById(R.id.activity_view_dashboard_detail_TextView_dashboard_name_content);
            dashboardNameTextView.setText(this.dashboard.getName());
			// set the due date
			TextView dashboardUrl = (TextView) findViewById(R.id.activity_view_dashboard_detail_TextView_dashboard_url_content);
            dashboardUrl.setText(this.dashboard.getUrl());

            TextView dashboardTextLabel = (TextView) findViewById(R.id.activity_view_dashboard_detail_TextView_label_value);
            String dashboardLabel;
            String labelsArray[] = getResources().getStringArray(R.array.activity_modify_dashboard_label_values);

            dashboardLabel= labelsArray[this.dashboard.getLabel()];
            dashboardTextLabel.setText(dashboardLabel);

            RatingBar starRating=(RatingBar) findViewById(R.id.activity_view_dashboard_detail_rating_value);
            starRating.setRating(this.dashboard.getStar());

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_dashboard_detail, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == EDIT_DASHBOARD_REQUEST_CODE){
			this.dashboard = (DashBoard) data.getExtras().getSerializable(DashBoard.Dashboard_BUNDLE_KEY);
			this.putDataToView();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.activity_view_dashboard_detail_Menu_actionbar_Item_edit:
			ApplicationNavigationHandler.editExistingDashboard(this, this.dashboard);
			return true;
		case R.id.activity_view_dashboard_detail_Menu_actionbar_Item_delete:
			ConfirmDeletionDialog.showConfirmDeleteDialogForDashboard(this, this.dashboard, this.databaseAdapter);
		default:
			return super.onOptionsItemSelected(item);
		}
	}


}

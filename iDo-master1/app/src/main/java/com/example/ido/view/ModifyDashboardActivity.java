package com.example.ido.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.example.ido.R;
import com.example.ido.R.layout;
import com.example.ido.controller.ConfirmCancelDialogHandler;
import com.example.ido.model.DashBoard;
import com.example.ido.model.DatabaseAdapter;

/**
 * Created by Anshul & Nachiket   on 4/1/2015.
 *
 */
// This activity is used for 2 purposes: Creating new Task and Editing existing Task
// Every time it's loaded, the program should check whether the Task object in the bundle is exist
// If it's exist, this means the activity is going to edit that task
// Otherwise, the activity is going to create a new task
public class ModifyDashboardActivity extends GeneralActivity {

	// VARIABLES DEFINED HERE

	private DashBoard dashboard = null;

	// store the current job of this activity, Edit or Add task
	// value will be set from those 2 static variables below
	private int currentJob;
	private final int CURRENT_JOB_EDIT = 1;
	private final int CURRENT_JOB_ADD = 2;

	// DatabaseAdapter for interacting with database
	private DatabaseAdapter databaseAdapter;

//	// Adapter for All  List View
//	private SimpleCursorAdapter allLabelSpinnerAdapter;

	// OVERIDE FUNCTIONS

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent resultIntent;
		Bundle resultBundle;

		switch (item.getItemId()){

		// When user select Cancel or Home button, show a confirmation dialog
		case R.id.activity_modify_dashboard_Menu_actionbar_Item_cancel:
		case android.R.id.home:
			// set the result for the previous activity
			resultIntent = new Intent();
			resultBundle = new Bundle();
			resultBundle.putSerializable(DashBoard.Dashboard_BUNDLE_KEY, this.dashboard);
			resultIntent.putExtras(resultBundle);
			setResult(ViewDashboardDetailActivity.EDIT_DASHBOARD_REQUEST_CODE, resultIntent);

			// Show the confirmation dialog
			// The rest (leave activity or not), the function ApplicationDialogHandler.showConfirmCancelDialog() will handle
			ConfirmCancelDialogHandler.showConfirmCancelDialog(this);
			return true;

			// When user select Save
		case R.id.activity_modify_dashboard_Menu_actionbar_Item_save:
			// validate form

			// check whether the current job is add new or edit
			if(this.currentJob == CURRENT_JOB_ADD){
                // add new task to database
               addNewDashboard();
			} else {
				// edit the current task
				editExistingDashboard();

				// set the result for the previous activity
				resultIntent = new Intent();
				resultBundle = new Bundle();
				resultBundle.putSerializable(DashBoard.Dashboard_BUNDLE_KEY, this.dashboard);
				resultIntent.putExtras(resultBundle);
				setResult(ViewDashboardDetailActivity.EDIT_DASHBOARD_REQUEST_CODE, resultIntent);

			}

			// close this activity
			finish();
			return true;

			// default case, return the base class function
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		// set the result for the previous activity
		Intent resultIntent = new Intent();
		Bundle resultBundle = new Bundle();
		resultBundle.putSerializable(DashBoard.Dashboard_BUNDLE_KEY, this.dashboard);
		resultIntent.putExtras(resultBundle);
		setResult(ViewDashboardDetailActivity.EDIT_DASHBOARD_REQUEST_CODE, resultIntent);

		// Show the confirmation dialog
		// The rest (leave activity or not), the function ApplicationDialogHandler.showConfirmCancelDialog() will handle
		ConfirmCancelDialogHandler.showConfirmCancelDialogDashboard(this);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_modify_dashboard);

		// Open the connection to database
		databaseAdapter = new DatabaseAdapter(this);
		databaseAdapter.open();

		// Check whether this activity is going to create a new Task or edit an existing one
		// First, retrieve the Task object from the bundle
		Bundle modifyTaskBundle = this.getIntent().getExtras();
		try {
			this.dashboard = (DashBoard) modifyTaskBundle.getSerializable(DashBoard.Dashboard_BUNDLE_KEY);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		// Next, check if it is null
		if (dashboard != null){
			// Change the currentJob to edit task
			this.currentJob = this.CURRENT_JOB_EDIT;
			// If the Task object exist, load data from it and put to the form
			putDataToForm();
			// After that, change the title of the activity to "Edit task " + task name
		} else {
			// Init the new Task object
			this.dashboard = new DashBoard();
			// Change the currentJob to add new task
			this.currentJob = this.CURRENT_JOB_ADD;
			// If the Task object not exist, change the title of the activity to "Create new Task"
		}


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modify_dashboard, menu);
		return true;
	}


	// UTILITY FUNCTIONS

	// This function is used to edit the current task
	private void editExistingDashboard(){
		// Load data from form to this.task object
		updateDashboardObject();
		// Call the database adapter to update the task
		databaseAdapter.editExistingDashboard(this.dashboard);
	}

	// This function is used to retrieve data from form and update the this.task object
	private void updateDashboardObject(){
		// Retrieve data from form and put into this.task object
		// No need to set collaborators since the list view handles it automatically
		// set task title
		String dashboardName = ((EditText)findViewById(R.id.activity_modify_dashboard_Edittext_dashboard_name)).getText().toString();
		this.dashboard.setName(dashboardName);

        int labelIndex = ((Spinner)findViewById(R.id.activity_modify_dashboard_label_spinner)).getSelectedItemPosition();
        this.dashboard.setLabel(labelIndex);

        float starred = ((RatingBar) findViewById(R.id.activity_modify_dashboard_starred)).getRating();

        int rating=(int) starred;
        this.dashboard.setStar(rating);
        String dashboardURL = ((EditText)findViewById(R.id.activity_modify_dashboard_EditText_dashboard_url)).getText().toString();
        this.dashboard.setUrl(dashboardURL);




	}

	// This function is used to add new task to database
	private void addNewDashboard(){


		// Load data from form to this.task object
        updateDashboardObject();
		// Since we add new task, need to get a new task id
		// set task id
		String dashboardId = databaseAdapter.getNewDashboardId();
		this.dashboard.setId(dashboardId);

		// Call the database adapter to add new task
		this.databaseAdapter.insertDashboard(this.dashboard);
	}





	// This function is used to load data from this.task object and put it to form
	private void putDataToForm(){
		// First check if the current job is edit task
		if (this.currentJob == this.CURRENT_JOB_EDIT){
			// Now retrieve data from this Task oject and put it into form components
			// no need to set Collaborator list since it's a special component and there is another function that handles that task

			// set task title
			EditText dashboardNameEditText = (EditText) findViewById(R.id.activity_modify_dashboard_Edittext_dashboard_name);
            dashboardNameEditText.setText(this.dashboard.getName());

            Spinner labelSpinner = (Spinner) findViewById(R.id.activity_modify_dashboard_label_spinner);
            labelSpinner.setSelection(this.dashboard.getLabel());

            RatingBar starRating=(RatingBar)findViewById(R.id.activity_modify_dashboard_starred);
            starRating.setRating(this.dashboard.getStar());
			// set task note
			EditText dashboardUrlEditText = (EditText) findViewById(R.id.activity_modify_dashboard_EditText_dashboard_url);
            dashboardUrlEditText.setText(this.dashboard.getUrl());


		}
	}



}

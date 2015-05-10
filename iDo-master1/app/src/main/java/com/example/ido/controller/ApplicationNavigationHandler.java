package com.example.ido.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.ido.model.DashBoard;
import com.example.ido.model.DatabaseAdapter;

import com.example.ido.view.LabelFiltersActivity;
import com.example.ido.view.ModifyDashboardActivity;

import com.example.ido.view.ViewAllDashBoardActivity;

import com.example.ido.view.ViewDashboardDetailActivity;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;

// A class consists of static functions that help the application navigation between activity
// The idea of the class is that many events can call to the same activity,
// so that every time they need to start one particular activity, just call the static function here
// and it will do the rest of the work
// This is for reusable purpose
/**
 * Created by Anshul & Nachiket   on 4/1/2015.
 *
 */
public class ApplicationNavigationHandler {
	


    // Go to ViewDashboardDetailActivity
    public static void viewDashboardDetail(Activity sourceActivity, DashBoard dashboard){
        // create new intent
      // here webview component would come

        Intent viewDashboardDetailIntent = new Intent(sourceActivity, ViewDashboardDetailActivity.class);
        // put the Task object into the bundle
        Bundle viewDashboardDetailBundle = new Bundle();
        viewDashboardDetailBundle.putSerializable(DashBoard.Dashboard_BUNDLE_KEY, dashboard);
        // put the bundle into the intent
        viewDashboardDetailIntent.putExtras(viewDashboardDetailBundle);
        // start the activity
        sourceActivity.startActivity(viewDashboardDetailIntent);
    }
	

    public static void showAllDashboards(Activity sourceActivity){
        Intent showAllDashboardsIntent = new Intent(sourceActivity, ViewAllDashBoardActivity.class);
        sourceActivity.startActivity(showAllDashboardsIntent);
    }




    // Go to ModifyDashboardActivity to edit and existing Dashboard
    public static void editExistingDashboard(Activity sourceActivity, DashBoard existingDashboard){
        Intent editExistingDashboardIntent = new Intent(sourceActivity, ModifyDashboardActivity.class);
        // put the task to edit into the bundle
        Bundle editExistingDashboardBundle = new Bundle();
        // editExistingTaskBundle.putSerializable(Task.TASK_BUNDLE_KEY, existingTask);
        editExistingDashboardBundle.putSerializable(DashBoard.Dashboard_BUNDLE_KEY, existingDashboard);
        // put the bundle into intent
        editExistingDashboardIntent.putExtras(editExistingDashboardBundle);
        // start the activity
        sourceActivity.startActivityForResult(editExistingDashboardIntent, ViewDashboardDetailActivity.EDIT_DASHBOARD_REQUEST_CODE);
    }
	

    // Go to DashboardActivity to add new task
    public static void addNewDashboard(Activity sourceActivity, DatabaseAdapter databaseAdapter){

            Intent addNewDashboardIntent = new Intent(sourceActivity, ModifyDashboardActivity.class);
            sourceActivity.startActivityForResult(addNewDashboardIntent, ViewAllDashBoardActivity.ADD_NEW_DASHBOARD_REQUEST_CODE);

    }
    public static void filterDashboard(Activity sourceActivity, DatabaseAdapter databaseAdapter){


        Intent b = new Intent(sourceActivity, LabelFiltersActivity.class);
        sourceActivity.startActivity(b);
//        Intent filterDashboard = new Intent(sourceActivity, FilterLabelActivity.class);
//        sourceActivity.startActivity(filterDashboard);

    }


}

package com.example.ido.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Anshul   on 4/1/2015.
 *
 */
// A Model Class to reflect a Task
public class DashBoard implements Serializable {
	// A static variable to use when put it into bundle
	public static final String Dashboard_BUNDLE_KEY = "dashboard_bundle_key";

    private String id;
    private String name;
	private String url;
    private int label;
    private int star;

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


	public DashBoard(){

		this.name = "";
		this.url = "";
        this.id="";


	}

	public DashBoard(String id,String name, String url) {
		super();
		this.name = name;
		this.url = url;
        this.id=id;

	}

}

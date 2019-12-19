package com.example.regbadminton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Course {
    private Date date;
    private String time;
    private String classID;
    private String location;
    private String locationID;
    private String comment;



    public Course(JSONObject jsonCourse){
        try {
            date=new SimpleDateFormat("yyyy-MM-dd").parse(jsonCourse.getString("date"));
            time=jsonCourse.getString("time");
            classID=jsonCourse.getString("classID");
            location=jsonCourse.getString("location");
            locationID=jsonCourse.getString("locationID");
            comment=jsonCourse.getString("comment");
        }
        catch (JSONException je) {je.printStackTrace();}
        catch (ParseException pe){pe.printStackTrace();}
    }

    public String getDate(String format){return new SimpleDateFormat(format).format(this.date);}

    public String getTime(){return time;}

    public String getClassID(){return classID;}

    public String getLocation(){return location;}

    public String getLocationID(){return locationID;}

    public String getComment(){return comment;}
}

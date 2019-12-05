package com.example.regbadminton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Course {
    private String date;
    private String time;
    private String classId;

    public Course(JSONObject jsonCourse){
        try {
            date=jsonCourse.getString("date");
            time=jsonCourse.getString("time");
            classId= jsonCourse.getString("classId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public String getDate(String format) {
        String date="";
        try {
            date= new SimpleDateFormat(format).format(new SimpleDateFormat("yyyy-MM-dd").parse(this.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getClassId() {
        return classId;
    }
}

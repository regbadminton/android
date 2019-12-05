package com.example.regbadminton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button buttonLogin;
    Button buttonSession1;
    Button buttonSession2;
    Button buttonSingleSession;
    TextView textViewDate;
    TextView textViewNone;
    TextView textViewUpdated;
    TextView textViewTitle;
    ImageView imageViewLogo;
    ProgressBar progressBar;

    Calendar tomorrow= Calendar.getInstance();

    final String DATE_FORMAT="yyyyMMdd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        tomorrow.add(Calendar.DATE,1);
        new GetCoursesTask(new SimpleDateFormat(DATE_FORMAT).format(tomorrow.getTime())).execute();

//        buttonLogin.setOnClickListener(setButtonLoginListener());
    }

    View.OnClickListener setButtonLoginListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WebViewActivity.class).putExtra("url","https://accounts.surrey.ca/auth.aspx"));
            }
        };
    }

    void initializeViews(){
        progressBar=findViewById(R.id.progressBar);
        buttonLogin=findViewById(R.id.buttonLogin);
        buttonSession1=findViewById(R.id.buttonSession1);
        buttonSession2=findViewById(R.id.buttonSession2);
        buttonSingleSession=findViewById(R.id.buttonSingleSession);
        textViewDate=findViewById(R.id.textViewDate);
        textViewNone=findViewById(R.id.textViewNone);
        textViewUpdated=findViewById(R.id.textViewUpdated);
        textViewTitle=findViewById(R.id.textViewTitle);
        imageViewLogo=findViewById(R.id.imageViewLogo);
    }

    class GetCoursesTask extends AsyncTask<Void, Void, String> {
        private String date;
        protected  void onPostExecute(String response){
            textViewDate.setText("For "+new SimpleDateFormat("EEEE MMMM d yyyy").format(tomorrow.getTime()));
            textViewUpdated.setText("Last updated:\n"+new SimpleDateFormat("EEE MMM d yyyy h:mm:ss a").format(new Date()));
            textViewTitle.setText(R.string.app_name);
            imageViewLogo.setImageResource(R.drawable.icon);
            progressBar.setVisibility(View.GONE);
            try {
//                String testCourse="[{\"date\":\"2019-12-03\",\"time\":\"6:30pm - 8:00pm\",\"classId\":\"d46a622c-8fca-4674-bf7f-8cf0d87e1dbf\"}]";
                JSONArray jsonArray=new JSONArray(response);
                Course[] courses=new Course[jsonArray.length()];
                for(int i=0;i<jsonArray.length();++i)courses[i]=new Course(jsonArray.getJSONObject(i));
                switch (jsonArray.length()){
                    case 2:
                        buttonSession1.setOnClickListener(setButtonSessionListener(courses[0]));
                        buttonSession2.setOnClickListener(setButtonSessionListener(courses[1]));
                        buttonSession1.setVisibility(View.VISIBLE);
                        buttonSession2.setVisibility(View.VISIBLE);
                    break;

                    case 1:
                        buttonSingleSession.setOnClickListener(setButtonSessionListener(courses[0]));
                        buttonSingleSession.setVisibility(View.VISIBLE);
                    break;

                    case 0:
                    default:
                        textViewNone.setVisibility(View.VISIBLE);
                    break;
                }
            } catch (JSONException e) {
                textViewNone.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        private View.OnClickListener setButtonSessionListener(final Course course) {
            final String url="https://cityofsurrey.perfectmind.com/23615/Store/BookMe4LandingPages/Class?widgetId=15f6af07-39c5-473e-b053-96653f77a406&embed=False&redirectedFromEmbededMode=False&classId="+course.getClassId()+"&occurrenceDate="+course.getDate(DATE_FORMAT);
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,WebViewActivity.class).putExtra("url",url));
                }
            };
        }

        GetCoursesTask(String date){
            this.date=date;
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://regbadminton.com/api/?d="+date);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.toString(), e);
                return null;
            }
        }

//        @Override
//        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
//        }
    }
}

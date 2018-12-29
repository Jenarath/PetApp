package com.lovepetapp.pet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ActivityCalendar extends AppCompatActivity {

    String dateAdd = "";
    String y,m,d;
    Button btnAdd;
    String PointAPI = "http://159.65.10.157/php_web_services/api_get_date.php";
    int IOConnect = 0;
    TextView txtM;
    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month;
    String date ="2018-12-14";
    String name = "c";
    String sub = "a";
    public static ArrayList<Long> point_id = new ArrayList<Long>();
    public static ArrayList<String> point_name = new ArrayList<String>();
    public static ArrayList<String> point_date = new ArrayList<String>();
    public static ArrayList<String> point_time = new ArrayList<String>();
    public static ArrayList<String> point_data = new ArrayList<String>();

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = this.findViewById(R.id.toolbar_appoint);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        HomeCollection.date_collection_arr = new ArrayList<HomeCollection>();

        HomeCollection.date_collection_arr.add(new HomeCollection("2018-12-08", "Diwali", "Holiday", "this is holiday"));
        HomeCollection.date_collection_arr.add(new HomeCollection("2018-12-18", "Diwali", "Holiday", "this is holiday"));
        HomeCollection.date_collection_arr.add(new HomeCollection("2018-12-28", "Diwali", "Holiday", "this is holiday"));
        HomeCollection.date_collection_arr.add(new HomeCollection("2018-24-28", "Diwali", "Holiday", "this is holiday"));



        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new HwAdapter(this, cal_month,HomeCollection.date_collection_arr);

        btnAdd = (Button) findViewById(R.id.btnAppoint);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));


        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(ActivityCalendar.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(ActivityCalendar.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);
                ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, ActivityCalendar.this);
            }

        });

        new getDataTask().execute();



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCalendar.this, ActivityAppointment.class);
                startActivity(intent);
            }
        });
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask(){

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // parse json data from server in background
            parseJSONData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            System.out.println("point_date" +point_date);
            System.out.println("point_time" +point_time);
            System.out.println("point_data" +point_data);
            System.out.println("pointid" +point_id.size());


//            for (i=0 ; i < point_id.size(); i++)
//            {
//
//
//            }
//


            if(IOConnect == 0 && point_date != null) {
                HomeCollection.date_collection_arr = new ArrayList<HomeCollection>();

                HomeCollection.date_collection_arr.add(new HomeCollection(date, name, sub, name));

            }
            else {

                Toast.makeText(ActivityCalendar.this, getString(R.string.error_server), Toast.LENGTH_SHORT).show();

            }


        }
    }

    public void parseJSONData(){

        clearData();

        try {
            // request data from Category API
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(PointAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));

            String line;
            String str = "";
            while ((line = in.readLine()) != null){
                str += line;
            }

            // parse json data and store into arraylist variables
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);

                JSONObject Appointment = object.getJSONObject("Menus");

                point_id.add(Long.parseLong(Appointment.getString("point_id")));
                point_name.add(Appointment.getString("point_name"));
                point_date.add(Appointment.getString("point_date"));
                point_time.add(Appointment.getString("point_time"));
                point_data.add(Appointment.getString("point_data"));


            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            IOConnect = 1;
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void clearData(){
        point_data.clear();
        point_id.clear();
        point_name.clear();
        point_date.clear();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //cla.imageLoader.clearCache();
//        listPets.setAdapter(null);
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig)
    {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

}

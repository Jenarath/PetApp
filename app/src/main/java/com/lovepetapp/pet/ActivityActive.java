package com.lovepetapp.pet;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.ArrayList;

public class ActivityActive extends AppCompatActivity {

    AdapterActivity cla;
    GridView listActivity;
    ProgressBar prgLoading;
    TextView txtAlert;
    SwipeRefreshLayout swipeRefreshLayout = null;

    String ActivityAPI;
    int IOConnect = 0;

    public static ArrayList<Long> ActivityID = new ArrayList<Long>();
    public static ArrayList<String> ImageActivity = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);

        Toolbar toolbar = this.findViewById(R.id.toolbar_active);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.red);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listActivity = (GridView) findViewById(R.id.listActive);
        txtAlert = (TextView) findViewById(R.id.txtAlert);

        cla = new AdapterActivity(ActivityActive.this);

        ActivityAPI = "http://159.65.10.157/php_web_services/api_get_activity.php";

        new ActivityActive.getDataTask().execute();

        listActivity.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listActivity != null && listActivity.getChildCount() > 0) {
                    boolean firstItemVisible = listActivity.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listActivity.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });



    }

    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask(){
            if(!prgLoading.isShown()){
                prgLoading.setVisibility(View.VISIBLE);
                txtAlert.setVisibility(View.GONE);
            }
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
            // when finish parsing, hide progressbar
            prgLoading.setVisibility(View.GONE);

            // if internet connection and data available show data on list
            // otherwise, show alert text
            if((ActivityID.size() > 0) && (IOConnect == 0)){
                listActivity.setVisibility(View.VISIBLE);
                listActivity.setAdapter(cla);
            }else{
                txtAlert.setVisibility(View.VISIBLE);
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
            HttpUriRequest request = new HttpGet(ActivityAPI);
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

                JSONObject Activity = object.getJSONObject("Menus");

                ActivityID.add(Long.parseLong(Activity.getString("activity_id")));
                ImageActivity.add(Activity.getString("activity_picture"));


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
        ActivityID.clear();
        ImageActivity.clear();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //cla.imageLoader.clearCache();
        listActivity.setAdapter(null);
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


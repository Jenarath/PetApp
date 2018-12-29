package com.lovepetapp.pet;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class ActivityContact  extends AppCompatActivity {

    TextView text_address,text_web,text_tel;
    String ContactAPI;
    String Cid,addressC,webC,telC;
    int IOConnect = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = this.findViewById(R.id.toolbar_con);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        text_address = (TextView) findViewById(R.id.textAddress);
        text_web = (TextView) findViewById(R.id.textWeb);
        text_tel = (TextView) findViewById(R.id.textTel);

        ContactAPI = "http://159.65.10.157/php_web_services/api_get_contact.php";

        new getDataTask().execute();


    }

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
            // when finish parsing, hide progressbar
            // if internet connection and data available show data
            // otherwise, show alert text
            if ((text_address != null) && IOConnect == 0) {



                text_address.setText(addressC);
                text_web.setText(webC);
                text_tel.setText(telC);



            } else {
//                txtAlert.setVisibility(View.VISIBLE);
            }
        }
    }

    // method to parse json data from server
    public void parseJSONData() {

        try {
            // request data from menu detail API
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(ContactAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();


            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));

            String line;
            String str = "";
            while ((line = in.readLine()) != null) {
                str += line;
            }

            // parse json data and store into tax and currency variables
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part

            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);

                JSONObject contact = object.getJSONObject("Contact");

                addressC = contact.getString("con_address");
                webC = contact.getString("con_web");
                telC = contact.getString("con_tel");


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


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //cla.imageLoader.clearCache();
//        listActivity.setAdapter(null);
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
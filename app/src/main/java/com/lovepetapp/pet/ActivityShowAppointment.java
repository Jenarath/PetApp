package com.lovepetapp.pet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityShowAppointment extends AppCompatActivity {

    TextView txt_point_time,txt_point_name,txt_point_data;
    String pointTime,pointName,pointData,pointID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment);

        Toolbar toolbar = this.findViewById(R.id.toolbar_app_detail);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txt_point_time = (TextView) findViewById(R.id.txtTime);
        txt_point_name = (TextView) findViewById(R.id.txtAName);
        txt_point_data = (TextView) findViewById(R.id.txtData);

        Intent iGet = getIntent();
        pointName = iGet.getStringExtra("point_name");
        pointData = iGet.getStringExtra("point_data");
        pointTime = iGet.getStringExtra("point_time");

        txt_point_time.setText(pointTime);
        txt_point_name.setText(pointName);
        txt_point_data.setText(pointData);

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

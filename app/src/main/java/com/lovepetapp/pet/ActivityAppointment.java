package com.lovepetapp.pet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityAppointment extends AppCompatActivity {

    // declare static int variables to store date and time
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;

    public static final String TIME_DIALOG_ID = "timePicker";
    public static final String DATE_DIALOG_ID = "datePicker";

    EditText edtNamePet, edtData;
    Button btnSavePoint;
    static Button btnDate,btnTime;

    String point_name, point_date, point_time, point_data;

    String Result;
    String DateAPI = "http://159.65.10.157/php_web_services/api_add_appointment.php";
    int IOConnect = 0;

    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Toolbar toolbar = this.findViewById(R.id.toolbar_appoint);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnDate = (Button) findViewById(R.id.btnDate);
        btnTime = (Button) findViewById(R.id.btnTime);
        edtNamePet = (EditText) findViewById(R.id.petAddName);
        edtData = (EditText) findViewById(R.id.petDetail);
        btnSavePoint = (Button) findViewById(R.id.btnSaveAppoint);

        btnDate.setText(new StringBuilder()
                .append(year).append("-")
                .append(month + 1).append("-")
                .append(day).append(" "));

        btnTime.setText(new StringBuilder()
                .append(pad(hour)).append(":")
                .append(pad(minute)).append(":")
                .append("00"));

        btnDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show date picker dialog
                DialogFragment newFragment1 = new DatePickerApp();
                newFragment1.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });

        // event listener to handle time button when pressed
        btnTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
                DialogFragment newFragment2 = new TimePickerFragment();
                newFragment2.show(getSupportFragmentManager(), TIME_DIALOG_ID);
            }
        });

        btnSavePoint.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                point_name = edtNamePet.getText().toString();
                point_date = btnDate.getText().toString();
                point_time = btnTime.getText().toString();
                point_data = edtData.getText().toString();

                new sendData().execute();

            }
        });
    }


    // method to create date picker dialog
    public static class DatePickerApp extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear = year;
            mMonth = month;
            mDay = day;

            // show selected date to date button
            btnDate.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
        }
    }



    // method to create time picker dialog
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of DatePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // get selected time
            mHour = hourOfDay;
            mMinute = minute;

            // show selected time to time button
            btnTime.setText(new StringBuilder()
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)).append(":")
                    .append("00"));
        }
    }


    // asynctask class to send data to server in background
    public class sendData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        // show progress dialog
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog= ProgressDialog.show(ActivityAppointment.this, "",
                    getString(R.string.upload_success), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // send data to server and store result to variable
            System.out.println("point_datee " + point_date );
            System.out.println("point_dataa " + point_data );
            System.out.println("point_namee " + point_name );
            System.out.println("point_timee " + point_time );
            Result = getRequest(point_name,point_data,point_time,point_date);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // if finish, dismis progress dialog and show toast message
            dialog.dismiss();
            resultAlert(Result);


        }
    }

    // method to show toast message
    public void resultAlert(String HasilProses){

            Intent intent = new Intent(ActivityAppointment.this, ActivityShowAppointment.class);

            intent.putExtra("point_time", point_time);
            intent.putExtra("point_data", point_data);
            intent.putExtra("point_name", point_name);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
    }

    // method to post data to server
    public String getRequest(String point_name, String point_data, String point_time, String point_date){
        String result = "";

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(DateAPI);

        System.out.println("point_date " + point_date );
        System.out.println("point_data " + point_data );
        System.out.println("point_name " + point_name );
        System.out.println("point_time " + point_time );

        try{
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("point_name", point_name));
            nameValuePairs.add(new BasicNameValuePair("point_data", point_data));
            nameValuePairs.add(new BasicNameValuePair("point_date", point_date));
            nameValuePairs.add(new BasicNameValuePair("point_time", point_time));

            request.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            result = request(response);
        }catch(Exception ex){
            result = "Unable to connect.";
        }
        return result;
    }

    public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        return result;
    }











    // method to format date
    private static String pad(int c) {
        if (c >= 10){
            return String.valueOf(c);
        }else{
            return "0" + String.valueOf(c);
        }
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

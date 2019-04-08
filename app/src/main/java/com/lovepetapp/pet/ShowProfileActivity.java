package com.lovepetapp.pet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lovepetapp.asyncTask.LoadProfile;
import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.utils.Constant;
import com.lovepetapp.utils.Methods;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

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


public class ShowProfileActivity extends AppCompatActivity {

    TextView txt_name, txt_email, txt_phone, txt_address;
    Toolbar toolbar;
    private RoundedImageView imageView_profile;
    Button btn_edit_profile;
    LoadProfile loadProfile;
    Methods methods;
    ProgressDialog progressDialog;
    String user_image = "";
    int IOConnect = 0;
    String UserAPI;
    String uname = "",umail = "", uphone="",uadd="",uimage ="";
    String uID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

         uID = Constant.itemUser.getId();

        System.out.println("user_id" + uID);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        toolbar = this.findViewById(R.id.toolbar_show_profile);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txt_name = findViewById(R.id.txtName);
        txt_email = findViewById(R.id.txtEmail);
        txt_phone = findViewById(R.id.txtPhone);
        txt_address = findViewById(R.id.txtAddress);
        imageView_profile = findViewById(R.id.iv_profile);


        btn_edit_profile = findViewById(R.id.button_edit);

        UserAPI = "http://159.65.10.157/php_web_services/api_get_user.php?id="+uID;
        System.out.println("UserAPI" + UserAPI);



        new ShowProfileActivity.getDataTask().execute();

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowProfileActivity.this, ProfileEditActivity.class);
                intent.putExtra("user_image", uimage);
                intent.putExtra("uname", uname);
                intent.putExtra("umail", umail);
                intent.putExtra("uadd", uadd);
                intent.putExtra("uphone", uphone);

                startActivity(intent);

            }
        });

//        loadProfileMethod();


    }

    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask() {

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

            // if internet connection and data available show data on list
            // otherwise, show alert text
            System.out.println("IOConnect" + IOConnect);
            System.out.println("uname" + uname);

            if ((uname != null) && IOConnect == 0) {
                txt_name.setText(uname);
                txt_phone.setText(uphone);
                txt_email.setText(umail);
                txt_address.setText(uadd);

                Picasso.get().load(uimage).into(imageView_profile);
            }




        }
    }

    public void parseJSONData() {


        try {
            // request data from Category API
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(UserAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));

            String line;
            String str = "";
            while ((line = in.readLine()) != null) {
                str += line;
            }

            // parse json data and store into arraylist variables
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);

                JSONObject menu = object.getJSONObject("Menus");

                uname = menu.getString("name");
                uphone = menu.getString("phone");
                uimage = menu.getString("user_image");
                uadd = menu.getString("address");
                umail = menu.getString("email");



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
//        listPets.setAdapter(null);
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }


//    public void setVariables() {
//
//        txt_name.setText(Constant.itemUser.getName());
//        txt_phone.setText(Constant.itemUser.getMobile());
//
//        txt_email.setText(Constant.itemUser.getEmail());
//        txt_address.setText(Constant.itemUser.getAddress());
//
//
//        try {
//            Picasso.get()
//                    .load(user_image)
//                    .placeholder(R.drawable.placeholder_profile)
//                    .into(imageView_profile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void loadProfileMethod() {
//        loadProfile = new LoadProfile(ShowProfileActivity.this, new LoginListener() {
//            @Override
//            public void onStart() {
////                progressDialog.show();
//            }
//            @Override
//            public void onEnd(String success, String message) {
//                    if (success.equals("1")) {
//                        if (message.equals("0")) {
//                            Toast.makeText(ShowProfileActivity.this, getResources().getString(R.string.no_user_found), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    setVariables();
//            }
//        });
//
//        loadProfile.execute(Constant.URL_PROFILE + Constant.itemUser.getId());
//    }

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
//    @Override
//    public void onResume() {
//        if (Constant.isLogged) {
//            if (Constant.isUpdate) {
//                Constant.isUpdate = false;
////                setVariables();
//            }
////            textView_notlog.setVisibility(View.GONE);
//        }
//        super.onResume();
//    }
//}

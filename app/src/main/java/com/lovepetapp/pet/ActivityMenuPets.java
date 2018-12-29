package com.lovepetapp.pet;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;

public class ActivityMenuPets extends AppCompatActivity {

    AdapterListPet cla;
    GridView listPets;
    ProgressBar prgLoading;
    TextView txtAlert;
    SwipeRefreshLayout swipeRefreshLayout = null;


    String PetAPI;
    int IOConnect = 0;

//    String PetID,PetImage;
    public static ArrayList<Long> PetID = new ArrayList<Long>();
    public static ArrayList<String> petName = new ArrayList<String>();
    public static ArrayList<String> PetImage = new ArrayList<String>();
    public static ArrayList<String> petWeight = new ArrayList<String>();
    public static ArrayList<String> petHeight = new ArrayList<String>();
    public static ArrayList<String> petColor = new ArrayList<String>();
    public static ArrayList<String> petDoctor = new ArrayList<String>();
    public static ArrayList<String> petGender = new ArrayList<String>();
    public static ArrayList<String> petTypee = new ArrayList<String>();
    public static ArrayList<String> petCategory = new ArrayList<String>();
    public static ArrayList<String> petBirthDate = new ArrayList<String>();
    public static ArrayList<String> petDateOwner = new ArrayList<String>();

//    public static ArrayList<String> PromotionDetail = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        Toolbar toolbar = this.findViewById(R.id.toolbar_pet);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        clearData();

        PetAPI = "http://159.65.10.157/php_web_services/api_get_pet.php";

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutt);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.red);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoadingPet);
        listPets = (GridView) findViewById(R.id.listPets);
        txtAlert = (TextView) findViewById(R.id.txtAlertPet);

        cla = new AdapterListPet(ActivityMenuPets.this);


        new ActivityMenuPets.getDataTask().execute();

        listPets.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listPets != null && listPets.getChildCount() > 0) {
                    boolean firstItemVisible = listPets.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listPets.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

        listPets.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // go to detail page
                Intent iMenuList = new Intent(ActivityMenuPets.this, ActivityAddPetDetail.class);
                Intent iList = new Intent(ActivityMenuPets.this, ActivityShowPetDetail.class);
                iMenuList.putExtra("pet_id", PetID.get(position));

                if(PetID.get(position)==1) {
                    startActivity(iMenuList);
                }
                else {
                    iList.putExtra("pet_id", PetID.get(position));
                    iList.putExtra("pet_name", petName.get(position));
                    iList.putExtra("pet_image", PetImage.get(position));
                    iList.putExtra("pet_gender", petGender.get(position));
                    iList.putExtra("pet_weight", petWeight.get(position));
                    iList.putExtra("pet_type", petTypee.get(position));
                    iList.putExtra("pet_catagory", petCategory.get(position));
                    iList.putExtra("pet_birthday", petBirthDate.get(position));
                    iList.putExtra("pet_height", petHeight.get(position));
                    iList.putExtra("pet_colors", petColor.get(position));
                    iList.putExtra("pet_doctor", petDoctor.get(position));
                    iList.putExtra("pet_owner", petDateOwner.get(position));
                    startActivity(iList);
//                    Toast.makeText(ActivityMenuPets.this, "Cattttt" + PetID.get(position), Toast.LENGTH_SHORT).show();

                }
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
            if((PetID.size() > 0) && (IOConnect == 0)){
                listPets.setVisibility(View.VISIBLE);
                listPets.setAdapter(cla);
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
            HttpUriRequest request = new HttpGet(PetAPI);
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

                JSONObject Promotion = object.getJSONObject("Menus");

                PetID.add(Long.parseLong(Promotion.getString("pet_id")));
                petName.add(Promotion.getString("pet_name"));
                PetImage.add(Promotion.getString("pet_image"));
                petBirthDate.add(Promotion.getString("pet_birthday"));
                petCategory.add(Promotion.getString("pet_catagory"));
                petColor.add(Promotion.getString("pet_colors"));
                petDateOwner.add(Promotion.getString("pet_owner"));
                petDoctor.add(Promotion.getString("pet_doctor"));
                petGender.add(Promotion.getString("pet_gender"));
                petTypee.add(Promotion.getString("pet_type"));
                petHeight.add(Promotion.getString("pet_height"));
                petWeight.add(Promotion.getString("pet_weight"));

//                PromotionDetail.add(Promotion.getString("promotion_data"));

//                Log.d("listMenu name", Image.get(i));

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
        PetID.clear();
        PetImage.clear();
        petWeight.clear();
        petHeight.clear();
        petTypee.clear();
        petName.clear();
        petGender.clear();
        petCategory.clear();
        petColor.clear();
        petDoctor.clear();
        petBirthDate.clear();
        petDateOwner.clear();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //cla.imageLoader.clearCache();
        listPets.setAdapter(null);
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

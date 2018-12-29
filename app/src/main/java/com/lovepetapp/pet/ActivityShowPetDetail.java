package com.lovepetapp.pet;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
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

public class ActivityShowPetDetail extends AppCompatActivity {

    TextView TxtPetName, TxtPetGender, TxtPetType, TxtPetCatagory, TxtPetBirthDate, TxtPetW, TxtPetH, TxtpetC, TxtPetD, TxtPetOwner;
    ImageView PetImage;
    Button buttonEditPet;


    long PetID;

    String petName = "", petWeight = "", petHeight = "", petColor = "", petDoctor = "", petType = "dog", petGender = "male", petCategory = "เกรตเดน", petBirthDate, petDateOwner, imagePath = "";
    String petThaiType = "" ,petThaiGender ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pet_detail);

        Toolbar toolbar = this.findViewById(R.id.toolbar_pet_show);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getIntentData();
        TxtPetName = (TextView) findViewById(R.id.txtPetName);
        TxtPetGender = (TextView) findViewById(R.id.txtPetGender);
        TxtPetType = (TextView) findViewById(R.id.txtPetType);
        TxtPetCatagory = (TextView) findViewById(R.id.txtPetCatagory);
        TxtPetBirthDate = (TextView) findViewById(R.id.txtPetBirth);
        TxtPetW = (TextView) findViewById(R.id.txtPetWeight);
        TxtPetH = (TextView) findViewById(R.id.txtPetHeight);
        TxtpetC = (TextView) findViewById(R.id.txtPetColor);
        TxtPetD = (TextView) findViewById(R.id.txtPetDoc);
        TxtPetOwner = (TextView) findViewById(R.id.txtPetOwner);
        PetImage = (ImageView) findViewById(R.id.petPhoto);
        buttonEditPet = findViewById(R.id.button_edit_pet);


        loadData();

        buttonEditPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent list = new Intent(ActivityShowPetDetail.this, ActivityPetEditDetail.class);
                list.putExtra("pet_id", PetID);
                list.putExtra("pet_name", petName);
                list.putExtra("pet_image", imagePath);
                list.putExtra("pet_gender", petGender);
                list.putExtra("pet_weight", petWeight);
                list.putExtra("pet_type", petType);
                list.putExtra("pet_catagory", petCategory);
                list.putExtra("pet_birthday", petBirthDate);
                list.putExtra("pet_height", petHeight);
                list.putExtra("pet_colors", petColor);
                list.putExtra("pet_doctor", petDoctor);
                list.putExtra("pet_owner", petDateOwner);
                startActivity(list);

            }
        });


    }


    public void getIntentData() {

        Intent iGet = getIntent();
        PetID = iGet.getLongExtra("pet_id", 0);
        petName = iGet.getStringExtra("pet_name");
        petBirthDate = iGet.getStringExtra("pet_birthday");
        petCategory = iGet.getStringExtra("pet_catagory");
        petColor = iGet.getStringExtra("pet_colors");
        petDateOwner = iGet.getStringExtra("pet_owner");
        petDoctor = iGet.getStringExtra("pet_doctor");
        petGender = iGet.getStringExtra("pet_gender");
        petHeight = iGet.getStringExtra("pet_height");
        petType = iGet.getStringExtra("pet_type");
        petWeight = iGet.getStringExtra("pet_weight");
        imagePath = iGet.getStringExtra("pet_image");
//        Toast.makeText(ActivityShowPetDetail.this, "pet_name " + petName, Toast.LENGTH_SHORT).show();

        if(petGender.equals("male"))
        {
            petThaiGender = "เพศผู้";
        }
        else {
            petThaiGender = "เพศเมีย";

        }
        if(petType.equals("dog"))
        {
            petThaiType = "สุนัข";
        }
        else {
            petThaiType = "แมว";

        }

    }

    public void loadData() {
        Picasso.get().load(imagePath).into(PetImage);
        TxtPetBirthDate.setText(petBirthDate);
        TxtPetOwner.setText(petDateOwner);
        TxtPetType.setText(petThaiType);
        TxtpetC.setText(petColor);
        TxtPetD.setText(petDoctor);
        TxtPetH.setText(petHeight);
        TxtPetW.setText(petWeight);
        TxtPetName.setText(petName);
        TxtPetCatagory.setText(petCategory);
        TxtPetGender.setText(petThaiGender);

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


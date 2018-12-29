package com.lovepetapp.pet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button btn_pet,btn_promo,btn_vac,btn_adate,btn_know,btn_hos,btn_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btn_pet = findViewById(R.id.m01);
        btn_promo = findViewById(R.id.m02);
        btn_vac = findViewById(R.id.m03);
        btn_adate = findViewById(R.id.m04);
        btn_know = findViewById(R.id.m05);
        btn_hos = findViewById(R.id.m06);

        btn_setting = findViewById(R.id.btnset);



        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        btn_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ActivityMenuPets.class);
                startActivity(intent);
            }
        });
        btn_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ActivityPromotion.class);
                startActivity(intent);
            }
        });
        btn_vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ActivityVaccine.class);
                startActivity(intent);
            }
        });
        btn_hos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ActivityHospital.class);
                startActivity(intent);
            }
        });
        btn_adate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ActivityCalendar.class);
                startActivity(intent);
            }
        });


    }


}

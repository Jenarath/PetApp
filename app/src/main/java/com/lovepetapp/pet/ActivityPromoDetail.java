package com.lovepetapp.pet;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class ActivityPromoDetail extends AppCompatActivity {
    ImageView imgPreview;
    TextView txtPromoDetail;
    TextView txtAlert;

    long PromotionID;
    String Image,Detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_detail);

        Toolbar toolbar = this.findViewById(R.id.toolbar_pro);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imgPreview = (ImageView) findViewById(R.id.promoimg);
        txtPromoDetail = (TextView) findViewById(R.id.txtDetail);

        Intent iGet = getIntent();

        PromotionID = iGet.getLongExtra("promo_id", 1);
        Detail = iGet.getStringExtra("detail");
        Image = iGet.getStringExtra("image");



        if(Image != null || Detail != null) {
            Picasso.get().load(Image).into(imgPreview);
            txtPromoDetail.setText(Detail);
        }
        else
        {
            Toast.makeText(ActivityPromoDetail.this, "Not connect internet",
                        Toast.LENGTH_LONG).show();
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


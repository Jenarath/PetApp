package com.lovepetapp.pet;

import android.app.ProgressDialog;
import android.content.Intent;
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


public class ShowProfileActivity extends AppCompatActivity {

    TextView txt_name,txt_email,txt_phone,txt_address;
    Toolbar toolbar;
    private RoundedImageView imageView_profile;
    Button btn_edit_profile;
    LoadProfile loadProfile;
    Methods methods;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
//        progressDialog.setCancelable(false);

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

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(ShowProfileActivity.this, ProfileEditActivity.class);
                    startActivity(intent);

            }
        });

        loadProfileMethod();

    }


    public void setVariables() {

        txt_name.setText(Constant.itemUser.getName());
        txt_phone.setText(Constant.itemUser.getMobile());

        txt_email.setText(Constant.itemUser.getEmail());
        txt_address.setText(Constant.itemUser.getAddress());


        try {
            Picasso.get()
                    .load(Constant.itemUser.getImage())
                    .placeholder(R.drawable.placeholder_profile)
                    .into(imageView_profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfileMethod() {
        loadProfile = new LoadProfile(ShowProfileActivity.this, new LoginListener() {
            @Override
            public void onStart() {
//                progressDialog.show();
            }
            @Override
            public void onEnd(String success, String message) {
                    if (success.equals("1")) {
                        if (message.equals("0")) {
                            Toast.makeText(ShowProfileActivity.this, getResources().getString(R.string.no_user_found), Toast.LENGTH_SHORT).show();
                        }
                    }
                    setVariables();
            }
        });

        loadProfile.execute(Constant.URL_PROFILE + Constant.itemUser.getId());
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
    @Override
    public void onResume() {
        if (Constant.isLogged) {
            if (Constant.isUpdate) {
                Constant.isUpdate = false;
                setVariables();
            }
//            textView_notlog.setVisibility(View.GONE);
        }
        super.onResume();
    }
}

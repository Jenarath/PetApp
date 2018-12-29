package com.lovepetapp.pet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lovepetapp.asyncTask.LoadProfileUpdate;
import com.lovepetapp.utils.Constant;
import com.makeramen.roundedimageview.RoundedImageView;
import com.lovepetapp.asyncTask.LoadRegister;
import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.utils.Methods;
import com.squareup.picasso.Picasso;
import android.Manifest;


import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    LoadRegister loadRegister;
    EditText editText_name, editText_email, editText_pass, editText_address, editText_phone;
    Button button_register,button_photo;
    private LoadProfileUpdate loadProfileUpdate;
    ProgressDialog progressDialog;
    private String name, email, phone, password = "", cpass = "", address = "", imagePath = "";
    Methods methods;
    private RoundedImageView image_profile;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestStoragePermission();


        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());
        methods.setStatusColor(getWindow(), null);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.register));

        image_profile = findViewById(R.id.iv_profile_edit);
        button_register = findViewById(R.id.button_register);
        editText_name = findViewById(R.id.et_regis_name);
        editText_email = findViewById(R.id.et_regis_email);
        editText_pass = findViewById(R.id.et_regis_password);
        editText_phone = findViewById(R.id.et_regis_phone);
        editText_address = findViewById(R.id.et_regis_address);

        button_register.setTypeface(button_register.getTypeface(), Typeface.BOLD);


        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (methods.isNetworkAvailable()) {
                        loadRegister();
                    } else {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }



    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    private Boolean validate() {
        if (editText_name.getText().toString().trim().isEmpty()) {
            editText_name.setError(getResources().getString(R.string.enter_name));
            editText_name.requestFocus();
            return false;
        } else if (editText_email.getText().toString().trim().isEmpty()) {
            editText_email.setError(getResources().getString(R.string.enter_email));
            editText_email.requestFocus();
            return false;
        } else if (!isEmailValid(editText_email.getText().toString().trim())) {
            editText_email.setError(getString(R.string.error_invalid_email));
            editText_email.requestFocus();
            return false;
        } else if (editText_pass.getText().toString().trim().isEmpty()) {
            editText_pass.setError(getResources().getString(R.string.enter_password));
            editText_pass.requestFocus();
            return false;
        } else if (editText_address.getText().toString().trim().isEmpty()) {
            editText_address.setError(getResources().getString(R.string.enter_address));
            editText_address.requestFocus();
            return false;
        } else if (editText_phone.getText().toString().trim().isEmpty()) {
            editText_phone.setError(getResources().getString(R.string.enter_phone));
            editText_phone.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void loadRegister() {
        loadRegister = new LoadRegister(new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                progressDialog.dismiss();

                System.out.println("success " + success);
                System.out.println("message " + message);

                if (success.equals("1")) {
                    if (message.contains("already")) {
                        editText_email.setError(getResources().getString(R.string.email_already_regis));
                        editText_email.requestFocus();
                    } else if (message.contains("Invalid email format")) {
                        editText_email.setError(getResources().getString(R.string.error_invalid_email));
                        editText_email.requestFocus();
                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.error_registering), Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadRegister.execute(editText_name.getText().toString(), editText_email.getText().toString(), editText_pass.getText().toString(), editText_phone.getText().toString(),editText_address.getText().toString(),imagePath);
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            imagePath = methods.getPathImage(uri);

            try {
                Bitmap bitmap_upload = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image_profile.setImageBitmap(bitmap_upload);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
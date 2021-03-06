package com.lovepetapp.pet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.lovepetapp.asyncTask.LoadProfileUpdate;
import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.utils.Constant;
import com.lovepetapp.utils.Methods;
import android.Manifest;


import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileEditActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Toolbar toolbar;
    Methods methods;
    private LoadProfileUpdate loadProfileUpdate;
    private EditText editText_name, editText_email, editText_phone, editText_pass, editText_address;
    private AppCompatButton button_update;
    private RoundedImageView imageView_profile;
    private ImageView imageView_editpropic;
    private String name, email, phone, password = "", cpass = "", address = "", imagePath = "";
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String check = "0";
    String user_image = "";
    String uname = "",umail = "", uphone="",uadd="",uimage ="";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        requestStoragePermission();
        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());

        progressDialog = new ProgressDialog(ProfileEditActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.updating));

        toolbar = findViewById(R.id.toolbar_proedit);
        toolbar.setTitle(getResources().getString(R.string.profile_edit));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        button_update = findViewById(R.id.button_prof_update);
        imageView_profile = findViewById(R.id.iv_profile_edit);
        imageView_editpropic = findViewById(R.id.iv_edit_pro_ic);
        editText_name = findViewById(R.id.et_prof_edit_fname);
        editText_email = findViewById(R.id.et_prof_edit_email);
        editText_phone = findViewById(R.id.et_prof_edit_mobile);
        editText_address = findViewById(R.id.et_prof_edit_address);
        editText_pass = findViewById(R.id.et_prof_edit_pass);


        Intent iGet = getIntent();
        uimage = iGet.getStringExtra("user_image");
        uname = iGet.getStringExtra("uname");
        uadd = iGet.getStringExtra("uadd");
        uphone = iGet.getStringExtra("uphone");
        umail = iGet.getStringExtra("umail");

        System.out.println("image " + uimage);

//        setProfileVar();

        getData();

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (methods.isNetworkAvailable()) {
//                        setVariables();
                        loadProfileEdit();
                    } else {
                        Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        imageView_editpropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = "1";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProfileEdit() {
        loadProfileUpdate = new LoadProfileUpdate(ProfileEditActivity.this, new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

//                setVariables();
                System.out.println("successE " + success);
                System.out.println("messageE " + message);

                if (success.equals("1")) {
//                    if (message.equals("1")) {
//                        updateArray();
//                        Constant.isUpdate = true;
//                        finish();
                        Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.update_prof_succ), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileEditActivity.this, ShowProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                    } else {
//                        Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.email_already_regis), Toast.LENGTH_SHORT).show();
//                   }
                } else if (success.equals("0")) {
                    Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.update_prof_not_succ), Toast.LENGTH_SHORT).show();
                }
            }
        });

        System.out.println("imagePath Update" + imagePath);
        System.out.println("imagePathh" + uimage);
        System.out.println("check " + check);
        System.out.println("check name " + editText_name.getText().toString());


        if(check.equals("0")) {

            loadProfileUpdate.execute(editText_name.getText().toString(), editText_email.getText().toString(), editText_phone.getText().toString(), editText_pass.getText().toString(), editText_address.getText().toString(),uimage,check);

        }
        else {
            loadProfileUpdate.execute(editText_name.getText().toString(), editText_email.getText().toString(), editText_phone.getText().toString(), editText_pass.getText().toString(), editText_address.getText().toString(),imagePath,check);

        }
    }

    private Boolean validate() {
        editText_name.setError(null);
        editText_email.setError(null);
        View focusView;
        if (editText_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
            editText_name.setError(getString(R.string.error_invalid_password));
            focusView = editText_name;
            focusView.requestFocus();
            return false;
        } else if (editText_email.getText().toString().trim().isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.email_empty), Toast.LENGTH_SHORT).show();
            editText_email.setError(getString(R.string.error_field_required));
            focusView = editText_email;
            focusView.requestFocus();
            return false;

        } else {
            return true;
        }
    }

//    private void setVariables() {
//        name = editText_name.getText().toString();
//        email = editText_email.getText().toString();
//        phone = editText_phone.getText().toString();
//        password = editText_pass.getText().toString();
//        address = editText_address.getText().toString();
//
//        if (!password.equals("")) {
//            methods.changeRemPass();
//        }
//    }

    private void updateArray() {
        Constant.itemUser.setName(name);
        Constant.itemUser.setEmail(email);
        Constant.itemUser.setMobile(phone);
        Constant.itemUser.setAddress(address);
        Constant.itemUser.setImage(imagePath);

    }

    public void getData() {
        editText_name.setText(uname);
        editText_phone.setText(uphone);
        editText_email.setText(umail);
        editText_address.setText(uadd);
        Picasso.get().load(uimage).into(imageView_profile);

//        System.out.println("getImage " + Constant.itemUser.getImage());

//        try {
//            Picasso.get().load(image.placeholder(R.drawable.placeholder_profile).into(imageView_profile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
                imageView_profile.setImageBitmap(bitmap_upload);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}